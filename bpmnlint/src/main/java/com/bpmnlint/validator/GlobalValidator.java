package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.util.*;

import static com.bpmnlint.Util.issue;

public class GlobalValidator {

    // Helper class to manage BPMN namespaces for XPath queries
    private static class BPMNNamespaceContext implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            if (prefix.equals("bpmn")) {
                return "http://www.omg.org/spec/BPMN/20100524/MODEL";
            }
            return null;
        }

        @Override
        public String getPrefix(String namespaceURI) { return null; }
        @Override
        public Iterator<String> getPrefixes(String namespaceURI) { return null; }
    }

    private static final XPath XPATH;
    static {
        // Initialize and configure a static XPath object for reuse
        XPathFactory factory = XPathFactory.newInstance();
        XPATH = factory.newXPath();
        XPATH.setNamespaceContext(new BPMNNamespaceContext());
    }

    private static final String[] GLOBAL_TYPES = {
            "Error", "Escalation", "Message", "Signal"
    };

    /**
     * Main validation method using standard Java DOM and XPath.
     */
    public static List<Issue> validate(Document doc) {
        List<Issue> issues = new ArrayList<>();
        NodeList rootElements;

        try {
            // XPath to select the <bpmn:definitions> element
            Node definitionsNode = (Node) XPATH.evaluate(
                "//bpmn:definitions", 
                doc, 
                XPathConstants.NODE
            );
            
            if (definitionsNode == null || definitionsNode.getNodeType() != Node.ELEMENT_NODE) {
                return issues;
            }
            Element definitions = (Element) definitionsNode;

            // XPath to select all relevant global elements (direct children of <definitions>)
            String typesForXPath = String.join(" | ", 
                Arrays.stream(GLOBAL_TYPES)
                    .map(t -> "bpmn:" + t)
                    .toArray(String[]::new)
            );
            String rootElementsXPath = "./" + typesForXPath; 

            rootElements = (NodeList) XPATH.evaluate(
                rootElementsXPath, 
                definitions, 
                XPathConstants.NODESET
            );

        } catch (XPathExpressionException e) {
            System.err.println("XPath error during initial element selection: " + e.getMessage());
            return issues;
        }

        // Use a Map to track names for the uniqueness check (replaces isUnique method)
        Map<String, List<Element>> nameRegistry = new HashMap<>();

        for (int i = 0; i < rootElements.getLength(); i++) {
            Element root = (Element) rootElements.item(i);
            
            String localType = root.getLocalName();
            String name = root.hasAttribute("name") ? root.getAttribute("name").trim() : "";
            
            // 1. Must have a name
            if (name.isEmpty()) {
                issues.add(issue(root, "Element is missing name"));
            }

            // --- Uniqueness Check Preparation ---
            if (!name.isEmpty()) {
                String key = localType + ":" + name;
                nameRegistry.putIfAbsent(key, new ArrayList<>());
                nameRegistry.get(key).add(root);
            }
            
            // 2. Must be referenced at least once
            try {
                if (!isReferenced(root, doc)) {
                    issues.add(issue(root, "Element is unused"));
                }
            } catch (XPathExpressionException e) {
                 System.err.println("XPath error during reference check for ID " + root.getAttribute("id") + ": " + e.getMessage());
            }
        }
        
        // 3. Final Name Uniqueness Check (replaces isUnique method)
        for(List<Element> elements : nameRegistry.values()){
            if(elements.size() > 1){
                // Since the name is non-unique, add an issue for every instance found
                for(Element element : elements){
                    issues.add(issue(element, "Element name is not unique"));
                }
            }
        }

        return issues;
    }

    private static Issue issue(Element root, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'issue'");
    }

    /**
     * Checks if the global element is referenced elsewhere in the document using XPath.
     */
    private static boolean isReferenced(Element root, Document doc) throws XPathExpressionException {
        String id = root.getAttribute("id");
        if (id.isEmpty()) {
            return true; // Cannot check reference without an ID
        }

        String localType = root.getLocalName();
        String checkXPath = "";

        // Determine the XPath query based on the global element type and its required reference attribute
        switch (localType) {
            case "Error":
                checkXPath = "count(//bpmn:errorEventDefinition[@errorRef='" + id + "']) > 0";
                break;
            case "Escalation":
                checkXPath = "count(//bpmn:escalationEventDefinition[@escalationRef='" + id + "']) > 0";
                break;
            case "Message":
                // Message can be referenced by Message Event Definitions, Message Flows, Receive/Send Tasks
                checkXPath = "count(//bpmn:messageEventDefinition[@messageRef='" + id + "'] | "
                           + "//bpmn:messageFlow[@messageRef='" + id + "'] | "
                           + "//bpmn:receiveTask[@messageRef='" + id + "'] | "
                           + "//bpmn:sendTask[@messageRef='" + id + "']) > 0";
                break;
            case "Signal":
                checkXPath = "count(//bpmn:signalEventDefinition[@signalRef='" + id + "']) > 0";
                break;
            default:
                return true; 
        }

        // Execute the COUNT XPath expression and explicitly cast the Object result to String
        String result = (String) XPATH.evaluate(checkXPath, doc, XPathConstants.STRING);
        
        // Return true if the evaluation of 'count() > 0' returns "true" or "1.0"
        return Boolean.parseBoolean(result) || result.equals("1.0");
    }
}
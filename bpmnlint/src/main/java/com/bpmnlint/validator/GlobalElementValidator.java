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

import static com.bpmnlint.Util.*; 

public class GlobalElementValidator {

    // Helper class to manage BPMN namespaces for XPath queries
    private static class BPMNNamespaceContext implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            // Standard BPMN 2.0 Namespace URI
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
    
    // XPath to select all relevant global elements
    private static final String GLOBAL_TYPES_XPATH = "bpmn:error | bpmn:escalation | bpmn:message | bpmn:signal";

    /**
     * Validates a BPMN Document for rules concerning global elements (Error, Escalation, Message, Signal).
     * * @param doc The BPMN XML document (org.w3c.dom.Document) to validate.
     * @return A list of Issue objects found.
     */
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        NodeList globals;

        try {
            // 1. Select all global elements using XPath
            globals = (NodeList) XPATH.evaluate(
                "//" + GLOBAL_TYPES_XPATH, 
                doc, 
                XPathConstants.NODESET
            );
        } catch (XPathExpressionException e) {
            System.err.println("XPath error during global element selection: " + e.getMessage());
            return result;
        }

        // Track names per type to check uniqueness
        Map<String, Set<String>> nameRegistry = new HashMap<>();

        for (int i = 0; i < globals.getLength(); i++) {
            Node node = globals.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;
            
            Element global = (Element) node;
            
            // Get local name (e.g., "error" instead of "bpmn:error")
            String type = global.getLocalName().toLowerCase(); 
            // Get attribute value and trim it
            String name = global.getAttribute("name").trim();
            
            // 1. Must have a name
            if (name.isEmpty()) {
                result.add(issue(global, "Global element <" + type + "> is missing a name"));
            }

            // 2. Must be unique by name per type
            nameRegistry.putIfAbsent(type, new HashSet<>());
            if (!name.isEmpty() && !nameRegistry.get(type).add(name)) {
                result.add(issue(global, "Global element <" + type + "> has a non-unique name: \"" + name + "\""));
            }

            // 3. Must be referenced
            try {
                if (!isReferenced(global, doc)) {
                    result.add(issue(global, "Global element <" + type + "> is unused"));
                }
            } catch (XPathExpressionException e) {
                 System.err.println("XPath error during reference check for ID " + global.getAttribute("id") + ": " + e.getMessage());
            }
        }

        return result;
    }

    private static Issue issue(Element global, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'issue'");
    }

    /**
     * Checks if the global element is referenced elsewhere in the document using XPath.
     * @param global The global element (Error, Message, etc.)
     * @param doc The entire BPMN Document
     * @return True if a reference is found, false otherwise.
     */
    private static boolean isReferenced(Element global, Document doc) throws XPathExpressionException {
        String id = global.getAttribute("id");
        if (id.isEmpty()) {
            // If the element is missing an ID, we cannot check references.
            return true; 
        }

        String checkXPath = "";

        // Determine the XPath query based on the global element type and its required reference attribute
        switch (global.getLocalName()) {
            case "error":
                checkXPath = "count(//bpmn:errorEventDefinition[@errorRef='" + id + "']) > 0";
                break;
            case "escalation":
                checkXPath = "count(//bpmn:escalationEventDefinition[@escalationRef='" + id + "']) > 0";
                break;
            case "message":
                // Check for reference in Message Event Definitions OR Message Flows
                checkXPath = "count(//bpmn:messageEventDefinition[@messageRef='" + id + "'] | //bpmn:messageFlow[@messageRef='" + id + "']) > 0";
                break;
            case "signal":
                checkXPath = "count(//bpmn:signalEventDefinition[@signalRef='" + id + "']) > 0";
                break;
            default:
                return true; 
        }

        // Execute the COUNT XPath expression and explicitly cast the Object result to String
        String result = (String) XPATH.evaluate(checkXPath, doc, XPathConstants.STRING);
        
        // Return true if the evaluation of 'count() > 0' returns "true" (or "1.0" for some engines)
        return Boolean.parseBoolean(result) || result.equals("1.0");
    }
}
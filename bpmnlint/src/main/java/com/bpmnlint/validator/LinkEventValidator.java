package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Assuming these utility methods and classes exist:
import static com.bpmnlint.Util.getLineNumber; 
// Assuming the Issue constructor is: new Issue(elementId, lineNumber, message)

public class LinkEventValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Maps to track link event names
        Set<String> throwLinks = new HashSet<>();
        Set<String> catchLinks = new HashSet<>();

    /**
     * Helper class to manage BPMN namespaces for XPath queries.
     */
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
        // Initialize and configure a static XPath object for reuse with BPMN namespace
        XPathFactory factory = XPathFactory.newInstance();
        XPATH = factory.newXPath();
        XPATH.setNamespaceContext(new BPMNNamespaceContext());
    }

    // --- Public Validation Method ---

    /**
     * 🔎 Validates a BPMN Document to ensure that all linkEventDefinition elements 
     * have a non-empty 'name' attribute.
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        List<Issue> result = new ArrayList<>();

        // 🌟 SIMPLIFIED XPATH: Selects ALL bpmn:linkEventDefinition elements.
        String linkEventDefinitionXPath = "//bpmn:linkEventDefinition";

        NodeList linkDefinitionNodes = (NodeList) XPATH.evaluate(
            linkEventDefinitionXPath, 
            doc, 
            XPathConstants.NODESET
        );

        for (int i = 0; i < linkDefinitionNodes.getLength(); i++) {
            Element linkDefinitionElement = (Element) linkDefinitionNodes.item(i);
            
            // **Validation done in Java, not XPath, for maximum reliability**
            String linkName = linkDefinitionElement.getAttribute("name");
            
            if (linkName == null || linkName.trim().isEmpty()) {
                
                // Get the parent event node to report the issue ID
                Node eventNode = linkDefinitionElement.getParentNode();
                
                if (eventNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eventElement = (Element) eventNode;
                    String eventId = eventElement.getAttribute("id");
                    String eventLocalName = eventElement.getLocalName();

                    String message = String.format(
                        "Link Event Definition Missing Name: The '%s' (%s) link definition must have a non-empty name attribute to function correctly.",
                        eventId,
                        eventLocalName
                    );

                    // Report the issue against the parent event node
                    // Note: Line number should be derived from the parent element if possible
                    result.add(new Issue(eventId, getLineNumber(eventElement), message)); 
                }
            }
        }
        
        return result;
    }
}
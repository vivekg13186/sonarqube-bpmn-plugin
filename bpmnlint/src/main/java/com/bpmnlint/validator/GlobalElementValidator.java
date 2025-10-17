package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.*;

// NOTE: The 'issue' utility method must now be compatible with W3C org.w3c.dom.Node
import static com.bpmnlint.Util.issue;

public class GlobalElementValidator {

    // XPath factories (initialized once for efficiency)
    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();
    private static final XPathExpression XPATH_GLOBAL_SELECT;

    // Static block to compile the main XPath expression once
    static {
        try {
            // XPath to select ALL global elements, regardless of namespace prefix
            String xpathQuery = "//*[local-name()='error' or local-name()='escalation' or local-name()='message' or local-name()='signal']";
            XPATH_GLOBAL_SELECT = XPATH.compile(xpathQuery);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Error compiling static XPath expression", e);
        }
    }


    /**
     * Validates global BPMN elements in a W3C DOM Document using XPath.
     * @param w3cDoc The W3C DOM Document containing the BPMN model.
     * @return A list of validation issues.
     */
    public static List<Issue> validate(Document w3cDoc) {
        List<Issue> result = new ArrayList<>();

        // Track names per type to check uniqueness
        Map<String, Set<String>> nameRegistry = new HashMap<>();

        try {
            // Execute XPath to select all global elements
            NodeList globalNodes = (NodeList) XPATH_GLOBAL_SELECT.evaluate(w3cDoc, XPathConstants.NODESET);

            for (int i = 0; i < globalNodes.getLength(); i++) {
                Node globalNode = globalNodes.item(i);
                
                String type = globalNode.getLocalName().toLowerCase();
                String name = getAttributeValue(globalNode, "name").trim();

                // 1. Must have a name
                if (name.isEmpty()) {
                    result.add(issue(globalNode, "Global element <" + type + "> is missing a name"));
                }

                // 2. Must be unique by name per type
                nameRegistry.putIfAbsent(type, new HashSet<>());
                if (!name.isEmpty() && !nameRegistry.get(type).add(name)) {
                    result.add(issue(globalNode, "Global element <" + type + "> has a non-unique name: \"" + name + "\""));
                }

                // 3. Must be referenced
                if (!isReferencedXPath(globalNode, w3cDoc, type)) {
                    result.add(issue(globalNode, "Global element <" + type + "> is unused"));
                }
            }

        } catch (XPathExpressionException e) {
            result.add(issue(null, "XPath error during validation: " + e.getMessage()));
        }

        return result;
    }

    // --- XPath Reference Check Method ---

    private static boolean isReferencedXPath(Node globalNode, Document w3cDoc, String type) throws XPathExpressionException {
        String id = getAttributeValue(globalNode, "id");
        if (id.isEmpty()) return false;

        String xpathQuery;

        // Define the XPath query based on the element type
        switch (type) {
            case "error":
                xpathQuery = "//*[local-name()='errorEventDefinition'][@errorRef='" + id + "']";
                break;
            case "escalation":
                xpathQuery = "//*[local-name()='escalationEventDefinition'][@escalationRef='" + id + "']";
                break;
            case "message":
                xpathQuery = "//*[local-name()='messageEventDefinition'][@messageRef='" + id + "']" +
                             " | //*[local-name()='messageFlow'][@messageRef='" + id + "']";
                break;
            case "signal":
                xpathQuery = "//*[local-name()='signalEventDefinition'][@signalRef='" + id + "']";
                break;
            default:
                return false;
        }

        // Use XPath to check if any node matches the reference query
        XPathExpression expr = XPATH.compile(xpathQuery);
        NodeList nodes = (NodeList) expr.evaluate(w3cDoc, XPathConstants.NODESET);

        return nodes.getLength() > 0;
    }

    // --- Helper Method ---

    /**
     * Retrieves the value of an attribute from a W3C Node.
     */
    private static String getAttributeValue(Node node, String attributeName) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) node;
            // Uses getAttribute which is namespace-agnostic for attributes (standard practice)
            return element.getAttribute(attributeName); 
        }
        return "";
    }
}
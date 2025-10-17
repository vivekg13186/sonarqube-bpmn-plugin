package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.*;

import static com.bpmnlint.Util.issue;
// Note: Util.issue is assumed to handle org.w3c.dom.Element or be updated

public class GlobalElementValidator {

    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();
    
    // Note: The XPath expression uses the * wildcard for namespaces, similar to Jsoup's *|
    private static final String GLOBAL_ELEMENTS_XPATH = 
        "//*[local-name()='error' or local-name()='escalation' or local-name()='message' or local-name()='signal']";

    // --- Core XPath Execution Helper ---

    /**
     * Executes an XPath expression and returns a NodeList.
     * @param node The context node for the evaluation (usually the Document).
     * @param expression The XPath query string.
     * @return A NodeList containing the matching elements.
     */
    private static NodeList evaluateXPath(Object node, String expression) {
        try {
            XPathExpression expr = XPATH.compile(expression);
            return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            // In a real application, you'd handle this more gracefully
            throw new RuntimeException("XPath evaluation failed: " + expression, e);
        }
    }

    // --- Validation Method ---

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all global elements using XPath
        NodeList globals = evaluateXPath(doc, GLOBAL_ELEMENTS_XPATH);

        // Track names per type to check uniqueness
        Map<String, Set<String>> nameRegistry = new HashMap<>();

        for (int i = 0; i < globals.getLength(); i++) {
            Element global = (Element) globals.item(i);
            
            // Use localName for the type, as it ignores the namespace prefix (e.g., bpmn:error -> error)
            String type = global.getLocalName().toLowerCase(); 
            String name = global.getAttribute("name").trim();
            String id = global.getAttribute("id");

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
            if (!isReferenced(global, doc)) {
                result.add(issue(global, "Global element <" + type + "> is unused"));
            }
        }

        return result;
    }

    // --- Reference Check Method ---

    private static boolean isReferenced(Element global, Document doc) {
        String id = global.getAttribute("id");

        // Reference XPath expressions
        String refXPath;
        
        // Use getLocalName() to get the element type without the prefix
        switch (global.getLocalName()) {
            case "error":
                // XPath: Find any element that is an 'errorEventDefinition' AND has an 'errorRef' attribute 
                // equal to the ID of the current global error.
                refXPath = "//*[local-name()='errorEventDefinition' and @errorRef='" + id + "']";
                break;
            case "escalation":
                refXPath = "//*[local-name()='escalationEventDefinition' and @escalationRef='" + id + "']";
                break;
            case "message":
                // Checks two possible referencing elements
                refXPath = "//*[local-name()='messageEventDefinition' and @messageRef='" + id + "']"
                         + " | " 
                         + "//*[local-name()='messageFlow' and @messageRef='" + id + "']";
                break;
            case "signal":
                refXPath = "//*[local-name()='signalEventDefinition' and @signalRef='" + id + "']";
                break;
            default:
                return false;
        }

        // Evaluate the XPath. If the resulting NodeList has a length > 0, the element is referenced.
        return evaluateXPath(doc, refXPath).getLength() > 0;
    }
}
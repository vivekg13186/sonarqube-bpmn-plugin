package com.bpmnlint.validator;
 
import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;
 
/**
 * Validates that any process or sub-process has at most one blank start event.
 * using the standard JAXP/XPath implementation.
 */
public class SingleBlankStartEventValidator {
 
    /**
     * Validates the given XML document.
     *
     * @param doc The parsed org.w3c.dom.Document to validate.
     * @return A list of issues found.
     */
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
 
        try {
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
 
            // 1. Find all process and subProcess elements
            String containerExpr = "//*[local-name()='process' or local-name()='subProcess']";
            NodeList containers = (NodeList) xpath.compile(containerExpr).evaluate(doc, XPathConstants.NODESET);
 
            // 2. For each container, check its start events
            for (int i = 0; i < containers.getLength(); i++) {
                Node container = containers.item(i);
 
                // Find all start events that are direct children of the container
                String startEventExpr = "*[local-name()='startEvent']";
                NodeList startEvents = (NodeList) xpath.compile(startEventExpr).evaluate(container, XPathConstants.NODESET);
 
                int blankCount = 0;
                for (int j = 0; j < startEvents.getLength(); j++) {
                    Node startEvent = startEvents.item(j);
                   
                    // A blank start event has no eventDefinition children.
                    String eventDefExpr = "*[local-name()='eventDefinition']";
                    NodeList definitions = (NodeList) xpath.compile(eventDefExpr).evaluate(startEvent, XPathConstants.NODESET);
                   
                    if (definitions.getLength() == 0) {
                        blankCount++;
                    }
                }
 
                if (blankCount > 1) {
                    String type = "subProcess".equalsIgnoreCase(container.getLocalName()) ? "Sub process" : "Process";
                    result.add(issue(container, type + " has multiple blank start events"));
                }
            }
 
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
 
        return result;
    }
 
    /**
     * Helper method to create a new Issue from a DOM Node.
     *
     * @param node    The XML node that has an issue.
     * @param message The validation message.
     * @return A new Issue object.
     */
    private static Issue issue(Node node, String message) {
        String nodeId = "unknown";
        if (node.hasAttributes() && node.getAttributes().getNamedItem("id") != null) {
            nodeId = node.getAttributes().getNamedItem("id").getNodeValue();
        }
        return new Issue(nodeId, 1, message);
    }
 
    /**
     * Attempts to get a line number from a node's user data.
     *
     * @param node The node to inspect.
     * @return The line number, or -1 if not available.
     */
    private static int getLineNumber(Node node) {
        if (node == null) {
            return -1;
        }
        Object lineNumber = node.getUserData("lineNumber");
        if (lineNumber instanceof Integer) {
            return (Integer) lineNumber;
        }
        return -1;
    }
}
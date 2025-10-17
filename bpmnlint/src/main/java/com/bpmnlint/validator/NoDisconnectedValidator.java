package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.issue;
// Note: Util.issue is assumed to handle org.w3c.dom.Element or be updated

public class NoDisconnectedValidator {

    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();
    
    // XPath component for flow elements that should be checked for connectivity
    private static final String FLOW_ELEMENTS_LOCAL_NAMES = 
        "local-name()='task' or local-name()='subProcess' or local-name()='exclusiveGateway' or " +
        "local-name()='parallelGateway' or local-name()='inclusiveGateway' or local-name()='startEvent' or " +
        "local-name()='endEvent' or local-name()='intermediateCatchEvent' or local-name()='intermediateThrowEvent' or " +
        "local-name()='boundaryEvent'";
    
    private static final String FLOW_ELEMENTS_XPATH = "//*[" + FLOW_ELEMENTS_LOCAL_NAMES + "]";


    /**
     * Executes an XPath expression and returns a NodeList.
     * @param node The context node for the evaluation.
     * @param expression The XPath query string.
     * @return A NodeList containing the matching elements.
     */
    private static NodeList evaluateXPath(Object node, String expression) {
        try {
            XPathExpression expr = XPATH.compile(expression);
            return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("XPath evaluation failed: " + expression, e);
        }
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all relevant flow elements
        NodeList elements = evaluateXPath(doc, FLOW_ELEMENTS_XPATH);

        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            String id = element.getAttribute("id");

            // --- Skip Logic ---

            // Skip event sub-processes
            // Jsoup: "true".equals(element.attr("triggeredByEvent"))
            if ("true".equals(element.getAttribute("triggeredByEvent"))) {
                continue;
            }

            // Skip ad-hoc sub-process children
            // Jsoup: parent != null && "true".equals(parent.attr("triggeredByEvent")) && parent.tagName().endsWith(":subProcess")
            Node parentNode = element.getParentNode();
            if (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element parent = (Element) parentNode;
                String parentLocalName = parent.getLocalName();
                String parentTriggeredByEvent = parent.getAttribute("triggeredByEvent");

                if (parentLocalName.equals("subProcess") && "true".equals(parentTriggeredByEvent)) {
                    continue;
                }
            }

            // Skip compensation boundary events and activities
            if (isCompensationLinked(element, doc)) {
                continue;
            }

            // --- Connectivity Check ---

            // Jsoup: !doc.select("*|sequenceFlow[targetRef=" + id + "]").isEmpty()
            String incomingXPath = "count(//*[local-name()='sequenceFlow' and @targetRef='" + id + "'])";
            // Jsoup: !doc.select("*|sequenceFlow[sourceRef=" + id + "]").isEmpty()
            String outgoingXPath = "count(//*[local-name()='sequenceFlow' and @sourceRef='" + id + "'])";
            
            boolean hasIncoming = countXPathResult(doc, incomingXPath) > 0;
            boolean hasOutgoing = countXPathResult(doc, outgoingXPath) > 0;
            
            // Check for incoming/outgoing sequence flows
            if (!hasIncoming && !hasOutgoing) {
                result.add(issue(element, "Element <" + element.getTagName() + "> is not connected to any sequence flow"));
            }
        }

        return result;
    }

    private static double countXPathResult(Document doc, String expression) {
        try {
            // Compile and evaluate the XPath expression as a number (count)
            return (Double) XPATH.compile(expression).evaluate(doc, XPathConstants.NUMBER);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("XPath counting failed: " + expression, e);
        }
    }

    private static boolean isCompensationLinked(Element element, Document doc) {
        String localTag = element.getLocalName();

        // Check if it's a boundary event with compensate definition
        // Jsoup: element.tagName().endsWith(":boundaryEvent")
        if (localTag.equals("boundaryEvent")) {
            // Jsoup: element.select("*|compensateEventDefinition")
            String defsXPath = "*[local-name()='compensateEventDefinition']";
            NodeList defs = evaluateXPath(element, defsXPath);
            return defs.getLength() > 0;
        }

        // Check if it's a compensation activity
        // Jsoup: "true".equals(element.attr("isForCompensation"))
        return "true".equals(element.getAttribute("isForCompensation"));
    }
}
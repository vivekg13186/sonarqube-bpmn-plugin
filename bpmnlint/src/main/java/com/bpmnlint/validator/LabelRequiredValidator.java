package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.issue;
// Note: Util.issue is assumed to handle org.w3c.dom.Element or be updated

public class LabelRequiredValidator {

    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();

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
            // Log or handle the exception appropriately
            throw new RuntimeException("XPath evaluation failed: " + expression, e);
        }
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // XPath to select all elements in the document
        NodeList elements = evaluateXPath(doc, "//*");

        for (int i = 0; i < elements.getLength(); i++) {
            // Cast to Element for attribute access
            Element element = (Element) elements.item(i);
            
            // Use getLocalName() to get the tag name without the namespace prefix
            String localTag = element.getLocalName(); 

            // --- Skip Logic (Based on Original Jsoup Code) ---

            // Skip parallel and event-based gateways (localName check)
            if (localTag.equals("parallelGateway") || localTag.equals("eventBasedGateway")) {
                continue;
            }

            // Skip sub-processes (localName check)
            if (localTag.equals("subProcess")) {
                continue;
            }

            // Skip gateways that are not forking
            if (localTag.endsWith("Gateway")) { // Checks for exclusiveGateway, inclusiveGateway, complexGateway
                String id = element.getAttribute("id");
                
                // XPath check: count outgoing sequenceFlows with sourceRef=current ID
                String outgoingXPath = "count(//*[local-name()='sequenceFlow' and @sourceRef='" + id + "'])";
                
                try {
                    // Evaluate as a number
                    Double count = (Double) XPATH.compile(outgoingXPath).evaluate(doc, XPathConstants.NUMBER);
                    
                    // If outgoing flow size is 1 or less, skip (not a split/fork)
                    if (count <= 1) {
                        continue;
                    }
                } catch (XPathExpressionException e) {
                    throw new RuntimeException("XPath evaluation failed for gateway count.", e);
                }
            }

            // Skip sequence flows without condition
            if (localTag.equals("sequenceFlow")) {
                // XPath check: find child element with local-name='conditionExpression'
                String conditionXPath = "*[local-name()='conditionExpression']";
                NodeList conditionNodes = evaluateXPath(element, conditionXPath);
                if (conditionNodes.getLength() == 0) {
                    continue;
                }
            }

            // --- Label Required Check ---

            // Check if the current element requires a label
            boolean requiresLabel = localTag.equals("startEvent") ||
                                    localTag.equals("endEvent") ||
                                    localTag.equals("intermediateCatchEvent") ||
                                    localTag.equals("intermediateThrowEvent") ||
                                    localTag.equals("boundaryEvent") ||
                                    localTag.equals("task") ||
                                    localTag.equals("callActivity") ||
                                    localTag.equals("userTask") ||
                                    localTag.equals("manualTask") ||
                                    localTag.equals("scriptTask") ||
                                    localTag.equals("receiveTask") ||
                                    localTag.equals("sendTask") ||
                                    localTag.equals("businessRuleTask") ||
                                    localTag.equals("sequenceFlow") ||
                                    localTag.equals("participant") ||
                                    localTag.equals("lane") ||
                                    localTag.equals("exclusiveGateway") ||
                                    localTag.equals("inclusiveGateway") ||
                                    localTag.equals("complexGateway");


            if (requiresLabel) {
                // Use standard DOM getAttribute
                String name = element.getAttribute("name").trim();
                if (name.isEmpty()) {
                    result.add(issue(element, "Element is missing label/name"));
                }
            }
        }

        return result;
    }
}
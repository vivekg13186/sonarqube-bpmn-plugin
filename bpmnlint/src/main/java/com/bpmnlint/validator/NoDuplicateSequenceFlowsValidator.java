package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.xpath.*;
import java.util.*;

import static com.bpmnlint.Util.issue;
// Note: Util.issue is assumed to handle org.w3c.dom.Element or be updated

public class NoDuplicateSequenceFlowsValidator {

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
            throw new RuntimeException("XPath evaluation failed: " + expression, e);
        }
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Map key is source#target#condition; value is the first flow element encountered
        Map<String, Element> seenFlows = new HashMap<>();
        Set<String> reportedSources = new HashSet<>();
        Set<String> reportedTargets = new HashSet<>();

        // Select all sequenceFlow elements using XPath
        NodeList sequenceFlows = evaluateXPath(doc, "//*[local-name()='sequenceFlow']");

        for (int i = 0; i < sequenceFlows.getLength(); i++) {
            Element flow = (Element) sequenceFlows.item(i);
            
            // Get attributes using standard DOM method
            String source = flow.getAttribute("sourceRef");
            String target = flow.getAttribute("targetRef");
            String condition = "";

            // Find conditionExpression child
            // Jsoup: flow.select("*|conditionExpression")
            NodeList conditionExpr = evaluateXPath(flow, "*[local-name()='conditionExpression']");
            
            if (conditionExpr.getLength() > 0) {
                Element conditionElement = (Element) conditionExpr.item(0);
                
                // Get inner text content of the element
                // Jsoup: conditionExpr.first().text().trim()
                String textContent = conditionElement.getTextContent();
                if (textContent != null) {
                     condition = textContent.trim();
                }
            }

            String key = source + "#" + target + "#" + condition;

            if (seenFlows.containsKey(key)) {
                result.add(issue(flow, "SequenceFlow is a duplicate"));

                // --- Report Duplicate Source Element ---
                // Jsoup: doc.select("*[id=" + source + "]").first()
                if (!reportedSources.contains(source)) {
                    // XPath to select element by ID
                    NodeList sourceNodes = evaluateXPath(doc, "//*[@id='" + source + "']");
                    if (sourceNodes.getLength() > 0) {
                        Element sourceElement = (Element) sourceNodes.item(0);
                        result.add(issue(sourceElement, "Duplicate outgoing sequence flows"));
                        reportedSources.add(source);
                    }
                }

                // --- Report Duplicate Target Element ---
                // Jsoup: doc.select("*[id=" + target + "]").first()
                if (!reportedTargets.contains(target)) {
                    // XPath to select element by ID
                    NodeList targetNodes = evaluateXPath(doc, "//*[@id='" + target + "']");
                    if (targetNodes.getLength() > 0) {
                        Element targetElement = (Element) targetNodes.item(0);
                        result.add(issue(targetElement, "Duplicate incoming sequence flows"));
                        reportedTargets.add(target);
                    }
                }
            } else {
                seenFlows.put(key, flow);
            }
        }
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

public class NoGatewayJoinForkValidator {

    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();

    // XPath component for all gateway types
    private static final String GATEWAY_ELEMENTS_LOCAL_NAMES =
        "local-name()='exclusiveGateway' or local-name()='parallelGateway' or local-name()='inclusiveGateway' or " +
        "local-name()='complexGateway' or local-name()='eventBasedGateway'";
    
    private static final String GATEWAYS_XPATH = "//*[" + GATEWAY_ELEMENTS_LOCAL_NAMES + "]";


    /**
     * Executes an XPath expression and returns the result as a number (count).
     * @param node The context node for the evaluation (usually the Document).
     * @param expression The XPath query string containing a 'count(...)' function.
     * @return The numerical result of the count query.
     */
    private static double countXPathResult(Object node, String expression) {
        try {
            // Compile and evaluate the XPath expression as a number
            return (Double) XPATH.compile(expression).evaluate(node, XPathConstants.NUMBER);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("XPath counting failed: " + expression, e);
        }
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all gateway elements using XPath
        NodeList gateways = evaluateXPath(doc, GATEWAYS_XPATH);

        for (int i = 0; i < gateways.getLength(); i++) {
            Element gateway = (Element) gateways.item(i);
            String id = gateway.getAttribute("id");

            // --- Count incoming and outgoing sequence flows using XPath ---
            
            // XPath to count incoming sequence flows (join)
            String incomingXPath = "count(//*[local-name()='sequenceFlow' and @targetRef='" + id + "'])";
            double incomingCount = countXPathResult(doc, incomingXPath);
            
            // XPath to count outgoing sequence flows (fork)
            String outgoingXPath = "count(//*[local-name()='sequenceFlow' and @sourceRef='" + id + "'])";
            double outgoingCount = countXPathResult(doc, outgoingXPath);

            // Check if the gateway is both joining (incoming > 1) and forking (outgoing > 1)
            if (incomingCount > 1 && outgoingCount > 1) {
                result.add(issue(gateway, "Gateway forks and joins simultaneously, which may lead to ambiguous logic"));
            }
        }

        return result;
    }
    
    /**
     * Helper to evaluate an XPath expression and return a NodeList.
     * Included here to satisfy the `countXPathResult` helper.
     */
    private static NodeList evaluateXPath(Object node, String expression) {
        try {
            XPathExpression expr = XPATH.compile(expression);
            return (NodeList) expr.evaluate(node, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("XPath evaluation failed: " + expression, e);
        }
    }
}
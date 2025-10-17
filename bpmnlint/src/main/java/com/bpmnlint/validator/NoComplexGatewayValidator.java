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

public class NoComplexGatewayValidator {

    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();

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
            throw new RuntimeException("XPath evaluation failed: " + expression, e);
        }
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all complex gateways using XPath
        // XPath: Selects all elements whose local name is 'complexGateway', regardless of namespace prefix.
        NodeList complexGateways = evaluateXPath(doc, "//*[local-name()='complexGateway']");

        for (int i = 0; i < complexGateways.getLength(); i++) {
            Element gateway = (Element) complexGateways.item(i);
            
            // Create an issue for each found Complex Gateway
            result.add(issue(gateway, "Use of <Complex Gateway> is discouraged"));
        }

        return result;
    }
}
package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.util.*;

// Assuming these utility methods and classes exist:
import static com.bpmnlint.Util.getLineNumber; 
// Assuming the Issue constructor is: new Issue(elementId, lineNumber, message)

public class NoGatewayJoinForkValidator {

    // --- Static XPath Setup ---

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
        XPathFactory factory = XPathFactory.newInstance();
        XPATH = factory.newXPath();
        XPATH.setNamespaceContext(new BPMNNamespaceContext());
    }

    // --- Public Validation Method ---

    /**
     * 🔎 Validates a BPMN Document to ensure that no single Gateway is used for both
     * joining (converging) and splitting (forking/diverging) sequence flows.
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        List<Issue> result = new ArrayList<>();

        // Use translate for case-insensitive XPath 1.0 selection of all Gateways.
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        // XPath to select all elements whose local name contains 'GATEWAY'
        String gatewaySelectionXPath = "//bpmn:process//*[" +
            "contains(translate(local-name(), '"+lowercase+"', '"+uppercase+"'), 'GATEWAY')" + 
        "]";
        
        NodeList gateways = (NodeList) XPATH.evaluate(
            gatewaySelectionXPath, 
            doc, 
            XPathConstants.NODESET
        );

        for (int i = 0; i < gateways.getLength(); i++) {
            Element gatewayElement = (Element) gateways.item(i);

            // Count incoming and outgoing flows using XPath
            NodeList incomingFlows = (NodeList) XPATH.evaluate(
                "bpmn:incoming", 
                gatewayElement, 
                XPathConstants.NODESET
            );
            
            NodeList outgoingFlows = (NodeList) XPATH.evaluate(
                "bpmn:outgoing", 
                gatewayElement, 
                XPathConstants.NODESET
            );

            int incomingCount = incomingFlows.getLength();
            int outgoingCount = outgoingFlows.getLength();
            
            // Check for Gateway Join/Fork violation: 
            // More than 1 incoming flow AND more than 1 outgoing flow
            if (incomingCount > 1 && outgoingCount > 1) {
                String elementId = gatewayElement.getAttribute("id");
                String message = String.format(
                    "Gateway '%s' performs both joining (%d incoming flows) and forking (%d outgoing flows). A single gateway should only perform one function (join OR fork).",
                    elementId, 
                    incomingCount, 
                    outgoingCount
                );
                // The issue in File 2 (ExclusiveGateway_1) falls here.
                result.add(new Issue(elementId, getLineNumber(gatewayElement), message)); 
            }
        }
        
        return result;
    }
}
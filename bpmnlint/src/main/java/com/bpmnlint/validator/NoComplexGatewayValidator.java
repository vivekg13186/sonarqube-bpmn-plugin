package com.bpmnlint.validator;

import com.bpmnlint.Issue; // Assumed class for validation results
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import java.util.*;

// NOTE: These utility methods are commonly provided by the linting framework 
// to extract context (like line number) from a standard DOM Node.
// If your framework doesn't provide these, you might need to adjust the Issue instantiation.
import static com.bpmnlint.Util.getLineNumber; 

public class NoComplexGatewayValidator {

    // --- Static XPath Setup ---

    /**
     * Helper class to manage BPMN namespaces for XPath queries.
     */
    private static class BPMNNamespaceContext implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            // Standard BPMN 2.0 Model Namespace
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
     * 🔎 Validates a BPMN Document to ensure it does not contain any <bpmn:complexGateway> elements.
     * * @param doc The org.w3c.dom.Document representing the BPMN file.
     * @return A list of validation issues found (i.e., one issue for every complex gateway found).
     * @throws XPathExpressionException if an error occurs during XPath processing.
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        List<Issue> result = new ArrayList<>();

        // XPath to select all Complex Gateway elements anywhere in the document
        String xpathExpression = "//bpmn:complexGateway";
        
        NodeList complexGateways = (NodeList) XPATH.evaluate(
            xpathExpression, 
            doc, 
            XPathConstants.NODESET
        );
        
        // Iterate through each Complex Gateway found and create an issue
        for (int i = 0; i < complexGateways.getLength(); i++) {
            Element gateway = (Element) complexGateways.item(i);
            String gatewayId = gateway.getAttribute("id");
            
            String message = String.format("The use of Complex Gateway is forbidden. Found gateway with ID '%s'.", gatewayId);
            
            // FIX: Instantiate the Issue directly using the Element ID and a utility 
            // to get the line number, avoiding the problematic issue(Element, String) method call.
            // Assumes Issue constructor is: new Issue(elementId, lineNumber, message)
            result.add(new Issue(gatewayId, getLineNumber(gateway), message)); 
        }
        
        return result;
    }
}
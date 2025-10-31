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

public class FakeJoinValidator {

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
        // Initialize and configure a static XPath object for reuse with BPMN namespace
        XPathFactory factory = XPathFactory.newInstance();
        XPATH = factory.newXPath();
        XPATH.setNamespaceContext(new BPMNNamespaceContext());
    }

    // --- Public Validation Method ---

    /**
     * 🔎 Validates a BPMN Document to detect "Fake Joins," where multiple sequence 
     * flows converge directly into a non-Gateway element (like a Task).
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        List<Issue> result = new ArrayList<>();

        // Use translate for case-insensitive XPath 1.0 selection.
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        // XPath to select all flow elements (Tasks, Events, SubProcesses) that are NOT Gateways.
        String elementSelectionXPath = "//bpmn:process//*[" +
            // 1. Exclude all Gateway types (case-insensitive check)
            "not(contains(translate(local-name(), '"+lowercase+"', '"+uppercase+"'), 'GATEWAY')) and (" +
            // 2. AND ensure it's a common flow element type that can have incoming flows
            "contains(translate(local-name(), '"+lowercase+"', '"+uppercase+"'), 'TASK') or " +       
            "contains(translate(local-name(), '"+lowercase+"', '"+uppercase+"'), 'EVENT') or " +
            "contains(translate(local-name(), '"+lowercase+"', '"+uppercase+"'), 'SUBPROCESS') or " +
            "contains(translate(local-name(), '"+lowercase+"', '"+uppercase+"'), 'CALLACTIVITY')" + 
            ")]";
        
        NodeList nonGatewayFlowNodes = (NodeList) XPATH.evaluate(
            elementSelectionXPath, 
            doc, 
            XPathConstants.NODESET
        );

        for (int i = 0; i < nonGatewayFlowNodes.getLength(); i++) {
            Element element = (Element) nonGatewayFlowNodes.item(i);
            String elementId = element.getAttribute("id");

            // Count incoming flows using XPath
            NodeList incomingFlows = (NodeList) XPATH.evaluate(
                "bpmn:incoming", 
                element, 
                XPathConstants.NODESET
            );
            
            int incomingCount = incomingFlows.getLength();
            
            // The violation occurs if a non-Gateway element has more than one incoming flow.
            if (incomingCount > 1) {
                
                String message = String.format(
                    "Fake Join detected: Element '%s' (%s) is not a Gateway but has %d incoming sequence flows. Use a Gateway for merging paths.",
                    elementId,
                    element.getLocalName(),
                    incomingCount
                );

                // The issue in File 2 (Task_2) falls here.
                result.add(new Issue(elementId, getLineNumber(element), message)); 
            }
        }
        
        return result;
    }
}
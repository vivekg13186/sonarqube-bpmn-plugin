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

public class NoDuplicateSequenceFlowsValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        Map<String, Element> seenFlows = new HashMap<>();
        Set<String> reportedSources = new HashSet<>();
        Set<String> reportedTargets = new HashSet<>();

        Elements sequenceFlows = doc.select("*|sequenceFlow");

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
     * 🔎 Validates a BPMN Document to ensure no two sequence flows connect the same
     * sourceRef and targetRef elements.
     * * @param doc The org.w3c.dom.Document representing the BPMN file.
     * @return A list of validation issues found (i.e., the duplicate sequence flows).
     * @throws XPathExpressionException if an error occurs during XPath processing.
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        List<Issue> result = new ArrayList<>();

        // XPath to select all sequence flows within the process
        String flowSelectionXPath = "//bpmn:process/bpmn:sequenceFlow";
        
        NodeList sequenceFlows = (NodeList) XPATH.evaluate(
            flowSelectionXPath, 
            doc, 
            XPathConstants.NODESET
        );

        // Map to store combinations of sourceRef and targetRef
        // Key: "sourceRef|targetRef"
        // Value: List of sequence flow elements sharing that combination
        Map<String, List<Element>> flowCombinations = new HashMap<>();
        
        // 1. Group sequence flows by their source and target
        for (int i = 0; i < sequenceFlows.getLength(); i++) {
            Element flowElement = (Element) sequenceFlows.item(i);
            String sourceRef = flowElement.getAttribute("sourceRef");
            String targetRef = flowElement.getAttribute("targetRef");
            
            // Create a unique key for the source/target pair
            String key = sourceRef + "|" + targetRef;

            flowCombinations.computeIfAbsent(key, k -> new ArrayList<>()).add(flowElement);
        }

        // 2. Iterate through the groups and report duplicates
        for (List<Element> flows : flowCombinations.values()) {
            if (flows.size() > 1) {
                // We found duplicates. Report every flow after the first one as an issue.
                // The first flow is considered the "original."
                for (int i = 1; i < flows.size(); i++) {
                    Element duplicateFlow = flows.get(i);
                    String elementId = duplicateFlow.getAttribute("id");
                    
                    // Retrieve the ID of the original flow for better context in the message
                    String originalFlowId = flows.get(0).getAttribute("id");

                    String message = String.format(
                        "Duplicate sequence flow: A flow already exists from %s to %s (Original flow ID: %s).",
                        duplicateFlow.getAttribute("sourceRef"),
                        duplicateFlow.getAttribute("targetRef"),
                        originalFlowId
                    );

                    result.add(new Issue(elementId, getLineNumber(duplicateFlow), message)); 
                }
            }
        }
        
        return result;
    }
}
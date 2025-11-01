package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;public class LabelRequiredValidator {

    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all BPMN elements
        Elements elements = doc.select("*");

        for (Element element : elements) {
            String tag = element.tagName();

            // Skip parallel and event-based gateways
            if (tag.endsWith(":parallelGateway") || tag.endsWith(":eventBasedGateway")) continue;

            // Skip sub-processes
            if (tag.endsWith(":subProcess")) continue;

            // Skip gateways that are not forking
            if (tag.endsWith(":gateway")) {
                String id = element.attr("id");
                Elements outgoing = doc.select("*|sequenceFlow[sourceRef=" + id + "]");
                if (outgoing.size() <= 1) continue;
            }

            // Skip sequence flows without condition
            if (tag.endsWith(":sequenceFlow")) {
                if (element.select("*|conditionExpression").isEmpty()) continue;
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
     * 🔎 Validates a BPMN Document to ensure that all core flow elements 
     * (Tasks, Events, Gateways, SubProcesses) have a descriptive label (name attribute).
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        List<Issue> result = new ArrayList<>();

        // 🌟 REVISED XPATH: Target all flow nodes using their specific BPMN tags.
        // This is more precise than checking if the name contains 'TASK' or 'EVENT'.
        String elementSelectionXPath = "//bpmn:process//*[" +
            "self::bpmn:task or self::bpmn:userTask or self::bpmn:manualTask or self::bpmn:serviceTask or self::bpmn:scriptTask or self::bpmn:receiveTask or self::bpmn:sendTask or " + // Tasks
            "self::bpmn:startEvent or self::bpmn:endEvent or self::bpmn:intermediateCatchEvent or self::bpmn:intermediateThrowEvent or " + // Events
            "self::bpmn:exclusiveGateway or self::bpmn:parallelGateway or self::bpmn:inclusiveGateway or " + // Gateways
            "self::bpmn:callActivity or self::bpmn:subProcess" + // Activities
        "]";
        
        NodeList flowNodes = (NodeList) XPATH.evaluate(
            elementSelectionXPath, 
            doc, 
            XPathConstants.NODESET
        );

        for (int i = 0; i < flowNodes.getLength(); i++) {
            Element element = (Element) flowNodes.item(i);
            String elementId = element.getAttribute("id");
            String elementName = element.getAttribute("name");
            String localName = element.getLocalName();
            
            // Check if the 'name' attribute is missing or empty/whitespace-only
            // Note: The getAttribute returns an empty string "" if the attribute is missing.
            if (elementName == null || elementName.trim().isEmpty()) {
                
                String message = String.format(
                    "Label Required: Element '%s' (%s) must have a descriptive name attribute.",
                    elementId,
                    localName // Use localName which is 'startEvent', 'task', etc.
                );

                // Add the issue, assuming the line number defaults to 1
                result.add(new Issue(elementId, getLineNumber(element), message)); 
            }
        }
        
        return result;
    }
}
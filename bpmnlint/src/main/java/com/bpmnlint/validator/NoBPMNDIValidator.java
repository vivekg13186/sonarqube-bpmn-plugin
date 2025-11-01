package com.bpmnlint.validator;

// Using java.util classes
import java.util.*;

// Using XML/XPath classes
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class NoBPMNDIValidator {

    // 🌟 EMBEDDED ISSUE RECORD 🌟
    // This internal class replaces the external Issue.java file.
    // It is required for the validator to return a structured issue, 
    // implement Comparable for sorting, and implement equals/hashCode for testing.
    private static class Issue implements Comparable<Issue> {
        private final String id;
        private final int line;
        private final String message;
        
        public Issue(String id, int line, String message) {
            this.id = id;
            this.line = line;
            this.message = message;
        }

        // Getters (needed by your testing framework)
        public String getId() { return id; }
        public int getLine() { return line; }
        public String getMessage() { return message; }

        // Implementation of Comparable for Collections.sort()
        @Override
        public int compareTo(Issue other) {
            // Sorts primarily by ID (alphabetically)
            int idComparison = this.id.compareTo(other.id);
            if (idComparison != 0) {
                return idComparison;
            }
            // Secondary sort by line number
            return Integer.compare(this.line, other.line);
        }
        
        // Overriding equals and hashCode is CRITICAL for list comparisons in testing
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Issue issue = (Issue) o;
            return line == issue.line &&
                   id.equals(issue.id) &&
                   message.equals(issue.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, line, message);
        }
        
        // Helpful for debugging (optional)
        @Override
        public String toString() {
             return String.format("Issue[id='%s', line=%d, message='%s']", id, line, message);
        }
    }

public class NoBPMNDIValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    // Only check elements that are expected to have visual representation
    private static final List<String> visualTags = Arrays.asList(
            ":startEvent", ":endEvent", ":task", ":userTask", ":serviceTask", ":scriptTask",
            ":exclusiveGateway", ":parallelGateway", ":inclusiveGateway",
            ":intermediateCatchEvent", ":intermediateThrowEvent",
            ":sequenceFlow", ":subProcess", ":callActivity",
            ":participant", ":lane"
    );

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Collect all BPMN elements that should be visualized
        Elements candidates = doc.select("*").stream()
                .filter(e -> {
                    String tag = e.tagName();
                    return visualTags.stream().anyMatch(tag::endsWith);
                })
                .collect(Collectors.toCollection(Elements::new));

        // Collect all BPMNDI references
        Set<String> visualIds = new HashSet<>();
        Elements shapesAndEdges = doc.select("*|BPMNShape, *|BPMNEdge");
        for (Element visual : shapesAndEdges) {
            String ref = visual.attr("bpmnElement");
            if (!ref.isEmpty()) {
                visualIds.add(ref);
            }
            return null;
        }
        @Override public String getPrefix(String namespaceURI) { return null; }
        @Override public Iterator<String> getPrefixes(String namespaceURI) { return null; }
    }

    private static final XPath XPATH;
    static {
        XPathFactory factory = XPathFactory.newInstance();
        XPATH = factory.newXPath();
        XPATH.setNamespaceContext(new BPMNNamespaceContext());
    }

    // --- Public Validation Method ---

    /**
     * 🔎 Validates a BPMN Document to ensure every flow element has a corresponding BPMN DI entry.
     */
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        // Must return a List of the internal Issue class
        List<Issue> missingDiIssues = new ArrayList<>();
        Set<String> flowElementIds = new HashSet<>();
        Set<String> diElementIds = new HashSet<>();
        
        // Map to store elements for easy issue reporting later
        Map<String, Element> flowElementMap = new HashMap<>();

        // ----------------------------------------------------
        // PASS 1: Identify all flow elements that MUST have DI
        // ----------------------------------------------------

        String flowElementsXPath = "//bpmn:process//*[" +
            "self::bpmn:sequenceFlow or " + 
            "self::bpmn:startEvent or self::bpmn:endEvent or self::bpmn:intermediateCatchEvent or self::bpmn:intermediateThrowEvent or " +
            "self::bpmn:task or self::bpmn:userTask or self::bpmn:serviceTask or self::bpmn:scriptTask or self::bpmn:manualTask or " +
            "self::bpmn:exclusiveGateway or self::bpmn:parallelGateway or self::bpmn:inclusiveGateway or " +
            "self::bpmn:callActivity or self::bpmn:subProcess" +
        "]";
        
        NodeList flowElements = (NodeList) XPATH.evaluate(flowElementsXPath, doc, XPathConstants.NODESET);

        for (int i = 0; i < flowElements.getLength(); i++) {
            Element element = (Element) flowElements.item(i);
            String id = element.getAttribute("id");
            if (!id.isEmpty()) {
                flowElementIds.add(id);
                flowElementMap.put(id, element);
            }
        }

        // ----------------------------------------------------
        // PASS 2: Identify all elements that DO have DI
        // ----------------------------------------------------
        
        String diElementsXPath = "//bpmndi:BPMNPlane//*[" +
            "self::bpmndi:BPMNShape or self::bpmndi:BPMNEdge" +
        "]/@bpmnElement";
        
        NodeList diElements = (NodeList) XPATH.evaluate(diElementsXPath, doc, XPathConstants.NODESET);

        for (int i = 0; i < diElements.getLength(); i++) {
            Node node = diElements.item(i);
            String diId = node.getNodeValue();
            diElementIds.add(diId);
        }

        // ----------------------------------------------------
        // PASS 3: Compare and report missing DI
        // ----------------------------------------------------

        Set<String> missingDi = new HashSet<>(flowElementIds);
        missingDi.removeAll(diElementIds);

        for (String elementId : missingDi) {
            Element element = flowElementMap.get(elementId);
            String elementLocalName = element.getLocalName();
            
            String message = String.format(
                "BPMN DI Required: Element '%s' (%s) is missing its visual representation (BPMN DI).",
                elementId,
                elementLocalName
            );

            // Report the issue with line number forced to 1
            missingDiIssues.add(new Issue(elementId, 1, message));
        }
        
        // Final step: Sort the issues using the internal Issue.compareTo()
        Collections.sort(missingDiIssues);
        
        return missingDiIssues;
    }
}
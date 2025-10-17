package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.*;

import static com.bpmnlint.Util.issue;
// Note: Util.issue is assumed to handle org.w3c.dom.Element or be updated

public class NoBPMNDIValidator {

    private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    private static final XPath XPATH = XPATH_FACTORY.newXPath();

    // Only check elements that are expected to have visual representation
    // These are the local names of the BPMN elements
    private static final List<String> visualTags = Arrays.asList(
            "startEvent", "endEvent", "task", "userTask", "serviceTask", "scriptTask",
            "exclusiveGateway", "parallelGateway", "inclusiveGateway",
            "intermediateCatchEvent", "intermediateThrowEvent",
            "sequenceFlow", "subProcess", "callActivity",
            "participant", "lane"
    );

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

        // --- 1. Collect all BPMN elements that should be visualized (Candidates) ---

        // Construct XPath to select all BPMN elements whose local name is in visualTags
        // Example: //*[local-name()='startEvent' or local-name()='endEvent' or ...]
        String tagsXPath = visualTags.stream()
                .map(tag -> "local-name()='" + tag + "'")
                .collect(Collectors.joining(" or "));
        String candidatesXPath = "//*[" + tagsXPath + "]";
        
        NodeList candidates = evaluateXPath(doc, candidatesXPath);
        
        // Convert NodeList to a List<Element> for easier reporting later
        List<Element> candidateElements = new ArrayList<>();
        for (int i = 0; i < candidates.getLength(); i++) {
            candidateElements.add((Element) candidates.item(i));
        }


        // --- 2. Collect all BPMNDI references (Visual IDs) ---

        // XPath: Select all BPMNShape and BPMNEdge elements anywhere in the document
        // Note: Assumes BPMNDI elements do not have a namespace prefix or the XPath environment 
        // handles the namespace correctly. Using local-name()='...' for safety.
        String shapesAndEdgesXPath = "//*[local-name()='BPMNShape' or local-name()='BPMNEdge']";
        NodeList shapesAndEdges = evaluateXPath(doc, shapesAndEdgesXPath);
        
        Set<String> visualIds = new HashSet<>();
        for (int i = 0; i < shapesAndEdges.getLength(); i++) {
            Element visual = (Element) shapesAndEdges.item(i);
            
            // Get the 'bpmnElement' attribute using standard DOM method
            String ref = visual.getAttribute("bpmnElement");
            
            if (!ref.isEmpty()) {
                visualIds.add(ref);
            }
        }

        // --- 3. Compare and report missing visuals ---

        for (Element element : candidateElements) {
            // Get the 'id' attribute using standard DOM method
            String id = element.getAttribute("id");
            
            if (!id.isEmpty() && !visualIds.contains(id)) {
                
                // Get the full tag name including prefix (e.g., bpmn:task) for the report message
                String fullTag = element.getTagName();
                
                result.add(issue(element, "Element <" + fullTag + "> with id \"" + id + "\" is missing BPMNDI visual representation"));
            }
        }

        return result;
    }
}
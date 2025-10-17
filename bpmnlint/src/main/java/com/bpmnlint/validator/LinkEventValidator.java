package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;
import java.util.*;

import static com.bpmnlint.Util.issue;
// Note: Util.issue is assumed to handle org.w3c.dom.Element or be updated

public class LinkEventValidator {

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

        // Maps to track link event names
        Set<String> throwLinks = new HashSet<>();
        Set<String> catchLinks = new HashSet<>();

        // --- 1. Process Throw Link Events ---
        
        // XPath: Select all intermediateThrowEvent elements
        NodeList throwEvents = evaluateXPath(doc, "//*[local-name()='intermediateThrowEvent']");
        
        for (int i = 0; i < throwEvents.getLength(); i++) {
            Element event = (Element) throwEvents.item(i);
            
            // XPath: Select child linkEventDefinition elements (context is the 'event' element)
            NodeList linkDefs = evaluateXPath(event, "*[local-name()='linkEventDefinition']");
            
            for (int j = 0; j < linkDefs.getLength(); j++) {
                Element def = (Element) linkDefs.item(j);
                
                // Get name attribute using standard DOM method
                String name = def.getAttribute("name").trim();
                
                if (!name.isEmpty()) {
                    throwLinks.add(name);
                } else {
                    result.add(issue(event, "Throw link event is missing a name"));
                }
            }
        }

        // --- 2. Process Catch Link Events ---

        // XPath: Select all intermediateCatchEvent elements
        NodeList catchEvents = evaluateXPath(doc, "//*[local-name()='intermediateCatchEvent']");
        
        for (int i = 0; i < catchEvents.getLength(); i++) {
            Element event = (Element) catchEvents.item(i);
            
            // XPath: Select child linkEventDefinition elements (context is the 'event' element)
            NodeList linkDefs = evaluateXPath(event, "*[local-name()='linkEventDefinition']");
            
            for (int j = 0; j < linkDefs.getLength(); j++) {
                Element def = (Element) linkDefs.item(j);
                
                // Get name attribute using standard DOM method
                String name = def.getAttribute("name").trim();
                
                if (!name.isEmpty()) {
                    catchLinks.add(name);
                } else {
                    result.add(issue(event, "Catch link event is missing a name"));
                }
            }
        }

        // --- 3. Check for Unmatched Links ---
        
        // Check for unmatched throw events
        for (String name : throwLinks) {
            if (!catchLinks.contains(name)) {
                // Cannot pass the original event element easily, so pass null as in the original Jsoup code
                result.add(issue(null, "Throw link event \"" + name + "\" has no matching catch event"));
            }
        }

        // Check for unmatched catch events
        for (String name : catchLinks) {
            if (!throwLinks.contains(name)) {
                // Cannot pass the original event element easily, so pass null as in the original Jsoup code
                result.add(issue(null, "Catch link event \"" + name + "\" has no matching throw event"));
            }
        }

        return result;
    }
}
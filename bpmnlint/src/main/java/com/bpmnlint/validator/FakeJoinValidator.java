package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Import static methods for 'issue' and any other utility
import static com.bpmnlint.Util.issue;

/**
 * Validates BPMN for "Fake Joins" using XPath.
 * A Fake Join occurs when an Activity or Event has multiple incoming flows
 * without an explicit Gateway for joining.
 */
public class FakeJoinValidator {

    // Define the BPMN namespace for use in XPath queries
    private static final String BPMN_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    private static final String BPMN_PREFIX = "bpmn"; // Arbitrary prefix for XPath

    // --- Custom NamespaceContext for handling BPMN namespaces in XPath ---
    private static class BpmnNamespaceContext implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            if (prefix == null) throw new NullPointerException("Null prefix");
            if (BPMN_PREFIX.equals(prefix)) return BPMN_NS;
            // Handle other namespaces if necessary (e.g., di, dc, modeler, etc.)
            return XMLConstants.NULL_NS_URI; // Placeholder, not ideal for full BPMN
        }

        @Override
        public String getPrefix(String namespaceURI) {
            if (BPMN_NS.equals(namespaceURI)) return BPMN_PREFIX;
            return null;
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            // Not strictly needed for this simple validator
            return null;
        }
    }

    /**
     * Executes the Fake Join validation using XPath.
     * @param doc The XML Document representing the BPMN model.
     * @return A list of validation issues found.
     */
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new BpmnNamespaceContext());

        try {
            // XPath to select all BPMN Activities (using the defined prefix)
            // Note: This needs to be comprehensive, similar to your Jsoup selector.
            String activityXPath = "//" + BPMN_PREFIX + ":*Activity | " +
                                   "//" + BPMN_PREFIX + ":*Task | " +
                                   "//" + BPMN_PREFIX + ":subProcess | " +
                                   "//" + BPMN_PREFIX + ":transaction";

            NodeList activities = (NodeList) xpath.evaluate(activityXPath, doc, XPathConstants.NODESET);

            for (int i = 0; i < activities.getLength(); i++) {
                Node node = activities.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element activity = (Element) node;

                    // Check for multiple incoming flows using a relative XPath
                    // Count how many 'incoming' elements are children of the current activity
                    String incomingCountXPath = "count(" + BPMN_PREFIX + ":incoming)";
                    Double incomingCount = (Double) xpath.evaluate(incomingCountXPath, activity, XPathConstants.NUMBER);

                    if (incomingCount > 1) {
                        result.add(issue(activity, "Incoming flows do not join (Fake Join)"));
                    }
                }
            }

            // XPath to select all BPMN Events
            String eventXPath = "//" + BPMN_PREFIX + ":*Event";

            NodeList events = (NodeList) xpath.evaluate(eventXPath, doc, XPathConstants.NODESET);

            for (int i = 0; i < events.getLength(); i++) {
                Node node = events.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element event = (Element) node;

                    // Check for multiple incoming flows using a relative XPath
                    String incomingCountXPath = "count(" + BPMN_PREFIX + ":incoming)";
                    Double incomingCount = (Double) xpath.evaluate(incomingCountXPath, event, XPathConstants.NUMBER);

                    if (incomingCount > 1) {
                        result.add(issue(event, "Incoming flows do not join (Fake Join)"));
                    }
                }
            }

        } catch (XPathExpressionException e) {
            // Handle error during XPath processing
            System.err.println("XPath error: " + e.getMessage());
        }

        return result;
    }
}
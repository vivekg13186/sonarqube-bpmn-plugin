package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoImplicitEndValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all flow nodes that could be implicit ends
        Elements candidates = doc.select(
                "*|startEvent, *|endEvent, *|intermediateCatchEvent, *|intermediateThrowEvent, *|boundaryEvent, " +
                        "*|task, *|subProcess, *|callActivity, *|receiveTask, *|sendTask, *|userTask, *|manualTask, *|scriptTask, *|businessRuleTask, " +
                        "*|exclusiveGateway, *|inclusiveGateway, *|parallelGateway, *|complexGateway, *|eventBasedGateway"
        );


        for (Element node : candidates) {
            String id = node.attr("id");

            // Skip if it's an EndEvent
            if (node.tagName().endsWith(":endEvent")) continue;

            // Skip if it's a Link Throw Event
            if (node.tagName().endsWith(":intermediateThrowEvent") &&
                    !node.select("*|linkEventDefinition").isEmpty()) continue;

            // Skip if it's a boundary event with compensation and has association
            if (node.tagName().endsWith(":boundaryEvent") &&
                    !node.select("*|compensateEventDefinition").isEmpty() &&
                    hasCompensationAssociation(doc, id)) continue;

            // Skip if it's a compensation activity
            if ("true".equals(node.attr("isForCompensation"))) continue;

            // Skip if it's inside an ad-hoc sub-process
            Element parent = node.parent();
            if (parent != null && parent.tagName().endsWith(":adHocSubProcess")) continue;

            // Skip if it's an event sub-process
            if (parent != null && parent.tagName().endsWith(":subProcess") &&
                    "true".equals(parent.attr("triggeredByEvent"))) continue;


            // Check outgoing sequence flows
            Elements outgoing = doc.select("*|sequenceFlow[sourceRef=" + id + "]");
            if (outgoing.isEmpty()) {
                result.add(issue(node, "Element is an implicit end (no outgoing sequence flow)"));
            }
        }

        return result;
    }

    private static boolean hasCompensationAssociation(Document doc, String sourceId) {
        Elements associations = doc.select("*|association[sourceRef=" + sourceId + "]");
        return !associations.isEmpty();
    }
}
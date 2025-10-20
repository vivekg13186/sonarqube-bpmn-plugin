package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoDisconnectedValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all relevant flow elements
        Elements elements = doc.select("*|task, *|subProcess, *|exclusiveGateway, *|parallelGateway, *|inclusiveGateway, *|startEvent, *|endEvent, *|intermediateCatchEvent, *|intermediateThrowEvent, *|boundaryEvent");

        for (Element element : elements) {
            String id = element.attr("id");

            // Skip event sub-processes
            if ("true".equals(element.attr("triggeredByEvent"))) {
                continue;
            }

            // Skip ad-hoc sub-process children
            Element parent = element.parent();
            if (parent != null && "true".equals(parent.attr("triggeredByEvent")) && parent.tagName().endsWith(":subProcess")) {
                continue;
            }

            // Skip compensation boundary events and activities
            if (isCompensationLinked(element)) {
                continue;
            }

            // Check for incoming/outgoing sequence flows
            boolean hasIncoming = !doc.select("*|sequenceFlow[targetRef=" + id + "]").isEmpty();
            boolean hasOutgoing = !doc.select("*|sequenceFlow[sourceRef=" + id + "]").isEmpty();

            if (!hasIncoming && !hasOutgoing) {
                result.add(issue(element, "Element <" + element.tagName() + "> is not connected to any sequence flow"));
            }
        }

        return result;
    }

    private static boolean isCompensationLinked(Element element) {
        // Check if it's a boundary event with compensate definition
        if (element.tagName().endsWith(":boundaryEvent")) {
            Elements defs = element.select("*|compensateEventDefinition");
            return !defs.isEmpty();
        }

        // Check if it's a compensation activity
        return "true".equals(element.attr("isForCompensation"));
    }
}
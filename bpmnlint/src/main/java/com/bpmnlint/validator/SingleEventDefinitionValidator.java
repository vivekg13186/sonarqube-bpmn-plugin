package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;
public class SingleEventDefinitionValidator {

    private static final String[] EVENT_DEFINITION_TAGS = {
            "messageEventDefinition",
            "timerEventDefinition",
            "signalEventDefinition",
            "linkEventDefinition",
            "errorEventDefinition",
            "terminateEventDefinition",
            "escalationEventDefinition",
            "compensateEventDefinition",
            "conditionalEventDefinition"
    };
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        Elements events = doc.select("*|startEvent, *|endEvent, *|intermediateCatchEvent, *|intermediateThrowEvent, *|boundaryEvent");

        for (Element event : events) {
            int count = 0;

            for (String tag : EVENT_DEFINITION_TAGS) {
                count += event.select("*|" + tag).size();
            }

            if (count > 1) {
                result.add(issue(event, "Event has multiple event definitions"));
            }
        }

        return result;
    }


}
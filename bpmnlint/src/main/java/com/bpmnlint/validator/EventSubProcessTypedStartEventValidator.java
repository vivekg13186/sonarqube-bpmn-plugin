package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class EventSubProcessTypedStartEventValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all event sub-processes
        Elements eventSubProcesses = doc.select("*|subProcess[triggeredByEvent=\"true\"]");

        for (Element subProcess : eventSubProcesses) {

            // Find start events inside the event sub-process
            Elements startEvents = subProcess.select("*|startEvent");

            for (Element startEvent : startEvents) {
                // Check if it has a defined event definition
                boolean hasTypedTrigger = !startEvent.select("*|messageEventDefinition, *|timerEventDefinition, *|signalEventDefinition, *|conditionalEventDefinition, *|errorEventDefinition, *|escalationEventDefinition, *|compensateEventDefinition").isEmpty();

                if (!hasTypedTrigger) {
                    result.add(issue(startEvent, "Start event is missing event definition"));
                }
            }


        }
        return result;
    }
}
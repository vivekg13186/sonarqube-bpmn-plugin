package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class SubProcessBlankStartEventValidator {
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

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        Elements subProcesses = doc.select("*|subProcess");

        for (Element sub : subProcesses) {
            if ("true".equals(sub.attr("triggeredByEvent"))) continue;

            Elements startEvents = sub.select("*|startEvent");
            for (Element start : startEvents) {
                int count = 0;

                for (String tag : EVENT_DEFINITION_TAGS) {
                    count += start.select("*|" + tag).size();
                }


                if (count>0) {
                    result.add(issue(start, "Start event must be blank"));
                }
            }
        }

        return result;
    }
}
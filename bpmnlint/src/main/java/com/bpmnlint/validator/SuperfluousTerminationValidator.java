package com.bpmnlint.validator;
import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;
public class SuperfluousTerminationValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        Elements endEvents = doc.select("*|endEvent");

        for (Element end : endEvents) {
            Elements defs = end.select("*|terminateEventDefinition");
            if (defs.isEmpty()) continue;

            // Check if this terminate event is inside an event sub-process
            Element parent = end.parent();
            if (parent != null && parent.tagName().endsWith("subProcess") &&
                    "true".equals(parent.attr("triggeredByEvent"))) {

                // Check if the main process has a normal end event
                Elements processEnds = doc.select("*|process > *|endEvent");
                for (Element procEnd : processEnds) {
                    if (procEnd.select("*|terminateEventDefinition").isEmpty()) {
                        result.add(issue(end, "Termination is superfluous."));
                        break;
                    }
                }
            }
        }

        return result;
    }
}
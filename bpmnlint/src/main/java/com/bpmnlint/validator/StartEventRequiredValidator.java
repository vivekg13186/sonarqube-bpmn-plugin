package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class StartEventRequiredValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        Elements containers = doc.select("*|process, *|subProcess");

        for (Element container : containers) {
            if (container.tagName().endsWith(":adHocSubProcess")) continue;

            Elements startEvents = container.select("*|startEvent");
            if (startEvents.isEmpty()) {
                String type = container.tagName().endsWith(":subProcess") ? "Sub process" : "Process";
                result.add(issue(container, type + " is missing start event"));
            }
        }

        return result;
    }
}
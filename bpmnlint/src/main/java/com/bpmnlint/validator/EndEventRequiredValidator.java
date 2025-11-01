package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class EndEventRequiredValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all processes and sub-processes
        Elements containers = doc.select("*|process, *|subProcess,*|transaction");

        for (Element container : containers) {
            // Find all end events within this container
            Elements endEvents = container.select("*|endEvent");
            if (endEvents.isEmpty()) {
                String t =container.tagName();
                String message = t.endsWith("subProcess") || t.endsWith("transaction") ? "Sub process is missing end event"
                :"Process is missing end event";
                result.add(issue(container, message));
            }
        }

        return result;
    }
}
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
        Elements containers = doc.select("*|process, *|subProcess");

        for (Element container : containers) {
            String containerId = container.attr("id");

            // Find all end events within this container
            Elements endEvents = container.select("*|endEvent");

            if (endEvents.isEmpty()) {
                result.add(issue(container, "Process is missing end event"));
            }
        }

        return result;
    }
}
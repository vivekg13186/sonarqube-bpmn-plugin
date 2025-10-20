package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class SingleBlankStartEventValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        Elements containers = doc.select("*|process, *|subProcess");

        for (Element container : containers) {
            Elements startEvents = container.select("*|startEvent");
            int blankCount = 0;

            for (Element start : startEvents) {
                if (start.select("*|eventDefinition").isEmpty()) {
                    blankCount++;
                }
            }

            if (blankCount > 1) {
                String type = container.tagName().endsWith(":subProcess") ? "Sub process" : "Process";
                result.add(issue(container, type + " has multiple blank start events"));
            }
        }

        return result;
    }
}
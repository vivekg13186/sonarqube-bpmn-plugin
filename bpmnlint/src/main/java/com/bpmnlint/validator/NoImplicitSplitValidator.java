package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoImplicitSplitValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all elements that could have outgoing flows
        Elements candidates = doc.select("*|task, *|subProcess, *|callActivity, *|sendTask, *|receiveTask, *|userTask, *|manualTask, *|scriptTask, *|businessRuleTask");

        for (Element el : candidates) {
            String id = el.attr("id");
            Elements outgoing = doc.select("*|sequenceFlow[sourceRef=" + id + "]");

            if (outgoing.size() > 1) {
                result.add(issue(el, "Element has multiple outgoing flows but is not a gateway—implicit split detected"));
            }
        }

        return result;
    }
}
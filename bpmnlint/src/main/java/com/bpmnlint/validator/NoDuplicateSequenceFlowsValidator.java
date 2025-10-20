package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.bpmnlint.Util.*;

public class NoDuplicateSequenceFlowsValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        Map<String, Element> seenFlows = new HashMap<>();
        Set<String> reportedSources = new HashSet<>();
        Set<String> reportedTargets = new HashSet<>();

        Elements sequenceFlows = doc.select("*|sequenceFlow");

        for (Element flow : sequenceFlows) {
            String source = flow.attr("sourceRef");
            String target = flow.attr("targetRef");
            String condition = "";

            Elements conditionExpr = flow.select("*|conditionExpression");
            if (!conditionExpr.isEmpty()) {
                condition = conditionExpr.first().text().trim();
            }

            String key = source + "#" + target + "#" + condition;

            if (seenFlows.containsKey(key)) {
                result.add(issue(flow, "SequenceFlow is a duplicate"));

                if (!reportedSources.contains(source)) {
                    Element sourceElement = doc.select("*[id=" + source + "]").first();
                    if (sourceElement != null) {
                        result.add(issue(sourceElement, "Duplicate outgoing sequence flows"));
                        reportedSources.add(source);
                    }
                }

                if (!reportedTargets.contains(target)) {
                    Element targetElement = doc.select("*[id=" + target + "]").first();
                    if (targetElement != null) {
                        result.add(issue(targetElement, "Duplicate incoming sequence flows"));
                        reportedTargets.add(target);
                    }
                }
            } else {
                seenFlows.put(key, flow);
            }
        }

        return result;
    }
}
package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class ConditionalFlowsValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all gateways and activities that can have conditional flows
        Elements nodes = doc.select("*|exclusiveGateway, *|inclusiveGateway, *|activity");

        for (Element node : nodes) {
            Elements outgoingFlows = doc.select("*|sequenceFlow[sourceRef=" + node.attr("id") + "]");

            for (Element flow : outgoingFlows) {
                boolean hasCondition = !flow.select("*|conditionExpression").isEmpty();
                boolean isDefault = node.hasAttr("default") && node.attr("default").equals(flow.attr("id"));

                if (!hasCondition && !isDefault) {
                    result.add(issue(flow, "Sequence flow is missing condition"));
                }
            }
        }

        return result;
    }
}
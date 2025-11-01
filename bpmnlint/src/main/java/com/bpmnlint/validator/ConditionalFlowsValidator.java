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

        // Select all gateways and tasks
        Elements nodes = doc.select("*|exclusiveGateway, *|inclusiveGateway, *|task");

        for (Element node : nodes) {
            String nodeId = node.attr("id");
            Elements outgoingFlows = doc.select("*|sequenceFlow[sourceRef=" + nodeId + "]");

            // Skip if only one outgoing flow
            if (outgoingFlows.size() <= 1) {
                continue;
            }

            boolean isConditionalForking = node.hasAttr("default") ||
                    outgoingFlows.stream().anyMatch(flow -> !flow.select("*|conditionExpression").isEmpty());

            if (!isConditionalForking) {
                continue;
            }

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
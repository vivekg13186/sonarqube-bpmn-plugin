package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;
public class LabelRequiredValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all BPMN elements
        Elements elements = doc.select("*");


        for (Element element : elements) {
            String tag = element.tagName();


            if(!element.tag().normalName().startsWith("bpmn:")) continue;
              // Skip parallel and event-based gateways
            if (tag.endsWith("parallelGateway") || tag.endsWith("eventBasedGateway")) continue;

            // Skip sub-processes
            if (tag.endsWith("subProcess")) continue;

            // Skip gateways that are not forking all gateway nodes
            if (tag.endsWith("Gateway")) {
                String id = element.attr("id");
                Elements outgoing = doc.select("*|sequenceFlow[sourceRef=" + id + "]");
                if (outgoing.size() <= 1) continue;
            }

            // Skip sequence flows without condition
            if (tag.endsWith("sequenceFlow")) {
                if (element.select("*|conditionExpression").isEmpty()) continue;
            }


            // Check for label on relevant BPMN elements
            boolean requiresLabel = tag.endsWith("startEvent") ||
                    tag.endsWith("endEvent") ||
                    tag.endsWith("intermediateCatchEvent") ||
                    tag.endsWith("intermediateThrowEvent") ||
                    tag.endsWith("boundaryEvent") ||
                    tag.endsWith("task") ||
                    tag.endsWith("callActivity") ||
                    tag.endsWith("userTask") ||
                    tag.endsWith("manualTask") ||
                    tag.endsWith("scriptTask") ||
                    tag.endsWith("receiveTask") ||
                    tag.endsWith("sendTask") ||
                    tag.endsWith("businessRuleTask") ||
                    tag.endsWith("sequenceFlow") ||
                    tag.endsWith("participant") ||
                    tag.endsWith("lane") ||
                    tag.endsWith("exclusiveGateway") ||
                    tag.endsWith("inclusiveGateway") ||
                    tag.endsWith("complexGateway");

            if (requiresLabel) {

                String name = element.attr("name").trim();

                if (name.isEmpty()) {
                    String type = element.tagName();
                    if(type.contains(":")){
                        type =type.split(":")[1];
                    }
                    String id = element.id();
                    String message = String.format("Label Required: Element '%s' (%s) must have a descriptive name attribute.",id,type);
                    result.add(issue(element, message));
                }
            }
        }

        return result;
    }
}
package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.bpmnlint.Util.*;

public class GlobalElementValidator {

    private static final List<String> GLOBAL_TYPES = Arrays.asList(
            "error", "escalation", "message", "signal"
    );

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all global elements
        Elements globals = doc.select("*|error, *|escalation, *|message, *|signal");

        // Track names per type to check uniqueness
        Map<String, Set<String>> nameRegistry = new HashMap<>();

        for (Element global : globals) {
            String type = global.tagName().replace("bpmn:", "").toLowerCase();
            String name = global.attr("name").trim();
            String id = global.attr("id");

            // 1. Must have a name
            if (name.isEmpty()) {
                result.add(issue(global, "Global element <" + type + "> is missing a name"));
            }

            // 2. Must be unique by name per type
            nameRegistry.putIfAbsent(type, new HashSet<>());
            if (!name.isEmpty() && !nameRegistry.get(type).add(name)) {
                result.add(issue(global, "Global element <" + type + "> has a non-unique name: \"" + name + "\""));
            }

            // 3. Must be referenced
            if (!isReferenced(global, doc)) {
                result.add(issue(global, "Global element <" + type + "> is unused"));
            }
        }

        return result;
    }

    private static boolean isReferenced(Element global, Document doc) {
        String id = global.attr("id");

        switch (global.tagName()) {
            case "bpmn:error":
                return !doc.select("*|errorEventDefinition[errorRef=" + id + "]").isEmpty();
            case "bpmn:escalation":
                return !doc.select("*|escalationEventDefinition[escalationRef=" + id + "]").isEmpty();
            case "bpmn:message":
                return !doc.select("*|messageEventDefinition[messageRef=" + id + "], *|messageFlow[messageRef=" + id + "]").isEmpty();
            case "bpmn:signal":
                return !doc.select("*|signalEventDefinition[signalRef=" + id + "]").isEmpty();
            default:
                return false;
        }
    }
}
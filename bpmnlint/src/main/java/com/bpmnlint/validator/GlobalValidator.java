package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.issue;

public class GlobalValidator {

    private static final String[] GLOBAL_TYPES = {
            "Error",
            "Escalation",
            "Message",
            "Signal"
    };

    public static List<Issue> validate(Document doc) {
        List<Issue> issues = new ArrayList<>();

        Element definitions = doc.selectFirst("*|definitions");
        if (definitions == null) {
            return issues;
        }

        List<Element> rootElements = getRootElements(definitions);
        List<Element> referencingElements = getReferencingElements(definitions);

        for (Element root : rootElements) {
            String id   = root.attr("id");
            String name = root.hasAttr("name") ? root.attr("name").trim() : "";

            // 1. must have a name
            if (name.isEmpty()) {
                issues.add(issue(root, "Element is missing name"));
            }

            // 2. must be referenced at least once
            if (!isReferenced(root, referencingElements)) {
                issues.add(issue(root, "Element is unused"));
            }

            // 3. name must be unique among same type
            if (!isUnique(root, rootElements)) {
                issues.add(issue(root, "Element name is not unique"));
            }
        }

        return issues;
    }

    // gather global elements directly under <definitions>
    private static List<Element> getRootElements(Element definitions) {
        List<Element> roots = new ArrayList<>();
        for (String type : GLOBAL_TYPES) {
            roots.addAll(definitions.select("> *|" + type));
        }
        return roots;
    }

    // collect all places that could reference a global element
    private static List<Element> getReferencingElements(Element definitions) {
        List<Element> refs = new ArrayList<>();

        // event definitions
        refs.addAll(definitions.select("*|errorEventDefinition"));
        refs.addAll(definitions.select("*|escalationEventDefinition"));
        refs.addAll(definitions.select("*|messageEventDefinition"));
        refs.addAll(definitions.select("*|signalEventDefinition"));

        // message flows and tasks
        refs.addAll(definitions.select("*|messageFlow"));
        refs.addAll(definitions.select("*|receiveTask"));
        refs.addAll(definitions.select("*|sendTask"));

        return refs;
    }

    private static boolean isReferenced(Element root, List<Element> refs) {
        String rootId   = root.attr("id");
        String localType = root.tagName().substring(root.tagName().indexOf(':') + 1);

        switch (localType) {
            case "Error":
                return refs.stream()
                        .filter(e -> e.tagName().endsWith("errorEventDefinition"))
                        .anyMatch(e -> rootId.equals(e.attr("errorRef")));
            case "Escalation":
                return refs.stream()
                        .filter(e -> e.tagName().endsWith("escalationEventDefinition"))
                        .anyMatch(e -> rootId.equals(e.attr("escalationRef")));
            case "Message":
                return refs.stream()
                        .filter(e -> {
                            String t = e.tagName();
                            return t.endsWith("messageEventDefinition")
                                    || t.endsWith("messageFlow")
                                    || t.endsWith("receiveTask")
                                    || t.endsWith("sendTask");
                        })
                        .anyMatch(e -> rootId.equals(e.attr("messageRef")));
            case "Signal":
                return refs.stream()
                        .filter(e -> e.tagName().endsWith("signalEventDefinition"))
                        .anyMatch(e -> rootId.equals(e.attr("signalRef")));
            default:
                return false;
        }
    }

    private static boolean isUnique(Element root, List<Element> roots) {
        String name      = root.attr("name");
        String localType = root.tagName().substring(root.tagName().indexOf(':') + 1);

        long count = roots.stream()
                .filter(r -> r.tagName().endsWith(localType))
                .filter(r -> name.equals(r.attr("name")))
                .count();

        return count == 1;
    }
}

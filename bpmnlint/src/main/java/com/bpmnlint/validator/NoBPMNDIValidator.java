package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import static com.bpmnlint.Util.*;




import java.util.*;
import java.util.stream.Collectors;



public class NoBPMNDIValidator {

    // Only check elements that are expected to have visual representation
    private static final List<String> visualTags = Arrays.asList(
            ":startEvent", ":endEvent", ":task", ":userTask", ":serviceTask", ":scriptTask",
            ":exclusiveGateway", ":parallelGateway", ":inclusiveGateway",
            ":intermediateCatchEvent", ":intermediateThrowEvent",
            ":sequenceFlow", ":subProcess", ":callActivity",
            ":participant", ":lane"
    );

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Collect all BPMN elements that should be visualized
        Elements candidates = doc.select("*").stream()
                .filter(e -> {
                    String tag = e.tagName();
                    return visualTags.stream().anyMatch(tag::endsWith);
                })
                .collect(Collectors.toCollection(Elements::new));

        // Collect all BPMNDI references
        Set<String> visualIds = new HashSet<>();
        Elements shapesAndEdges = doc.select("*|BPMNShape, *|BPMNEdge");
        for (Element visual : shapesAndEdges) {
            String ref = visual.attr("bpmnElement");
            if (!ref.isEmpty()) {
                visualIds.add(ref);
            }
        }

        // Compare and report missing visuals
        for (Element element : candidates) {
            String id = element.attr("id");
            if (!id.isEmpty() && !visualIds.contains(id)) {
                result.add(issue(element, "Element <" + element.tagName() + "> with id \"" + id + "\" is missing BPMNDI visual representation"));
            }
        }

        return result;
    }
}
package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.bpmnlint.Util.*;

public class LinkEventValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Maps to track link event names
        Set<String> throwLinks = new HashSet<>();
        Set<String> catchLinks = new HashSet<>();

        // Find all intermediate throw events with linkEventDefinition
        Elements throwEvents = doc.select("*|intermediateThrowEvent");
        for (Element event : throwEvents) {
            Elements linkDefs = event.select("*|linkEventDefinition");
            for (Element def : linkDefs) {
                String name = def.attr("name").trim();
                if (!name.isEmpty()) {
                    throwLinks.add(name);
                } else {
                    result.add(issue(event, "Throw link event is missing a name"));
                }
            }
        }

        // Find all intermediate catch events with linkEventDefinition
        Elements catchEvents = doc.select("*|intermediateCatchEvent");
        for (Element event : catchEvents) {
            Elements linkDefs = event.select("*|linkEventDefinition");
            for (Element def : linkDefs) {
                String name = def.attr("name").trim();
                if (!name.isEmpty()) {
                    catchLinks.add(name);
                } else {
                    result.add(issue(event, "Catch link event is missing a name"));
                }
            }
        }

        // Check for unmatched throw events
        for (String name : throwLinks) {
            if (!catchLinks.contains(name)) {
                result.add(issue(null, "Throw link event \"" + name + "\" has no matching catch event"));
            }
        }

        // Check for unmatched catch events
        for (String name : catchLinks) {
            if (!throwLinks.contains(name)) {
                result.add(issue(null, "Catch link event \"" + name + "\" has no matching throw event"));
            }
        }

        return result;
    }
}
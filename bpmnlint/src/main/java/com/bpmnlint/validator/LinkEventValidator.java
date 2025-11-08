package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import static com.bpmnlint.Util.issue;

public class LinkEventValidator {

    public static List<Issue> validate(Document doc) {

        List<Issue> issues = new ArrayList<>();

        // name -> throw elements
        Map<String, List<Element>> throwsByName = new HashMap<>();
        // name -> catch elements
        Map<String, List<Element>> catchesByName = new HashMap<>();


        /*********************
         * collect definitions
         *********************/
        Elements linkDefs = doc.select("*|linkEventDefinition");

        for (Element def : linkDefs) {

            String name = def.attr("name").trim();
            Element event = def.parent();

            if (name.isEmpty()) {

                issues.add(issue(event,
                        "Link Event Definition Missing Name: The '"+event.id()+"' ("+event.tagName().split(":")[1]+") link definition must have a non-empty name attribute to function correctly."));
                continue;
            }

            String type = event.tagName();

            if (type.endsWith("intermediateThrowEvent")) {
                throwsByName.computeIfAbsent(name, k -> new ArrayList<>()).add(event);
            }

            if (type.endsWith("intermediateCatchEvent")) {
                catchesByName.computeIfAbsent(name, k -> new ArrayList<>()).add(event);
            }
        }


        /****************************
         * mismatched throw / catch
         ****************************/
        /*for (String name : throwsByName.keySet()) {
            if (!catchesByName.containsKey(name)) {
                for (Element e : throwsByName.get(name)) {
                    issues.add(issue(e,
                            "Throw link \"" + name + "\" missing matching catch link in scope"));
                }
            }
        }

        for (String name : catchesByName.keySet()) {
            if (!throwsByName.containsKey(name)) {
                for (Element e : catchesByName.get(name)) {
                    issues.add(issue(e,
                            "Catch link \"" + name + "\" missing matching throw link in scope"));
                }
            }
        }*/


        /*****************
         * duplicate name
         *****************/
        for (Map.Entry<String, List<Element>> entry : catchesByName.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (Element e : entry.getValue()) {
                    issues.add(issue(e,
                            "Duplicate link catch event with link name \"" + entry.getKey() + "\""));
                }
            }
        }

        for (Map.Entry<String, List<Element>> entry : throwsByName.entrySet()) {
            if (entry.getValue().size() > 1) {
                for (Element e : entry.getValue()) {
                    issues.add(issue(e,
                            "Duplicate link throw event with link name \"" + entry.getKey() + "\""));
                }
            }
        }


        /*********************************************
         * cardinality rule - EXACTLY one incoming /
         * EXACTLY one outgoing
         *********************************************/

        // throw must have exactly 1 incoming sequence Flow
        for (List<Element> list : throwsByName.values()) {
            for (Element e : list) {
                Elements incoming = e.select("> *|incoming");
                if (incoming.size() != 1) {
                    issues.add(issue(e,
                            "Link throw event must have exactly 1 incoming sequenceFlow (found " + incoming.size() + ")"));
                }
            }
        }

        // catch must have exactly 1 outgoing sequence Flow
        for (List<Element> list : catchesByName.values()) {
            for (Element e : list) {
                Elements outgoing = e.select("> *|outgoing");
                if (outgoing.size() != 1) {
                    issues.add(issue(e,
                            "Link catch event must have exactly 1 outgoing sequenceFlow (found " + outgoing.size() + ")"));
                }
            }
        }

        return issues;
    }
}

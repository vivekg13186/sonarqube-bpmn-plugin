package com.bpmnlint.validator;
import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;
public class SuperfluousGatewayValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        Elements gateways = doc.select("*|exclusiveGateway, *|parallelGateway");

        for (Element gateway : gateways) {
            String id = gateway.attr("id");
            Elements incoming = doc.select("*|sequenceFlow[targetRef=" + id + "]");
            Elements outgoing = doc.select("*|sequenceFlow[sourceRef=" + id + "]");

            if (incoming.size() == 1 && outgoing.size() == 1) {
                result.add(issue(gateway, "Gateway is superfluous (1 incoming, 1 outgoing)"));
            }
        }

        return result;
    }
}
package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoGatewayJoinForkValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all gateway elements
        Elements gateways = doc.select("*|exclusiveGateway, *|parallelGateway, *|inclusiveGateway, *|complexGateway, *|eventBasedGateway");

        for (Element gateway : gateways) {
            String id = gateway.attr("id");

            // Count incoming and outgoing sequence flows
            Elements incoming = doc.select("*|sequenceFlow[targetRef=" + id + "]");
            Elements outgoing = doc.select("*|sequenceFlow[sourceRef=" + id + "]");

            if (incoming.size() > 1 && outgoing.size() > 1) {
                result.add(issue(gateway, "Gateway forks and joins simultaneously, which may lead to ambiguous logic"));
            }
        }

        return result;
    }
}
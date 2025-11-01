package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoInclusiveGatewayValidator {
    public static List<Issue> validate(org.w3c.dom.Document doc) {
        List<Issue> result = new ArrayList<>();
        return null;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        Elements gateways = doc.select("*|inclusiveGateway");

        for (Element gateway : gateways) {
            result.add(issue(gateway, "Inclusive gateways are discouraged"));
        }

        return result;
    }
}
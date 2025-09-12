package com.bpmnlint.validator;


import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoComplexGatewayValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all complex gateways
        Elements complexGateways = doc.select("*|complexGateway");

        for (Element gateway : complexGateways) {
            result.add(issue(gateway, "Use of <Complex Gateway> is discouraged"));
        }

        return result;
    }
}
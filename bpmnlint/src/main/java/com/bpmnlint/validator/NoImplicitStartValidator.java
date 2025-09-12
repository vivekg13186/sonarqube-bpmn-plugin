package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;
public class NoImplicitStartValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();
        Elements elements = doc.select(
                "*|startEvent, *|endEvent, *|intermediateCatchEvent, *|intermediateThrowEvent, *|boundaryEvent, " +
                        "*|task, *|subProcess, *|callActivity, *|receiveTask, *|sendTask, *|userTask, *|manualTask, *|scriptTask, *|businessRuleTask, " +
                        "*|exclusiveGateway, *|inclusiveGateway, *|parallelGateway, *|complexGateway, *|eventBasedGateway"
        );

        ;

        for (Element el : elements) {
            String id = el.attr("id");
            Elements incoming = doc.select("*|sequenceFlow[targetRef=" + id + "]");

            boolean isStartEvent = el.tagName().endsWith(":startEvent");
            boolean isBoundaryEvent = el.tagName().endsWith(":boundaryEvent");
            boolean isCompensation = "true".equals(el.attr("isForCompensation"));
            boolean isTriggeredByEvent = "true".equals(el.attr("triggeredByEvent"));
            boolean isAdHoc = el.parent() != null && el.parent().tagName().endsWith(":adHocSubProcess");

            if (incoming.isEmpty() && !isStartEvent && !isBoundaryEvent && !isCompensation && !isTriggeredByEvent && !isAdHoc) {
                result.add(issue(el, "Element is an implicit start"));
            }
        }

        return result;
    }
}
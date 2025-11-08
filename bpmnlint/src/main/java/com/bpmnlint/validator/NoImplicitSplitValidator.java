package com.bpmnlint.validator;

import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class NoImplicitSplitValidator {

    public static boolean isDefault(Element root,Element outgoing){
        String id = outgoing.text();
        if(root.hasAttr("default")){
             return id.equals(root.attr("default"));
        }
        return false;
    }

    public static boolean hasConditionExpression(Elements seq){
        if(!seq.isEmpty()){
            return !seq.select("*|conditionExpression").isEmpty();
        }
        return false;
    }
    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all elements that could have outgoing flows
        Elements candidates = doc.select("*|startEvent,*|task, *|subProcess, *|callActivity, *|sendTask, *|receiveTask, *|userTask, *|manualTask, *|scriptTask, *|businessRuleTask");

        for (Element el : candidates) {
            Elements outgoings = el.select("*|outgoing");
            int outGoingWithoutCondition =0;
            for(Element o : outgoings){
                Elements seq = doc.select("#"+o.text());
                if(!isDefault(el,o) && !hasConditionExpression(seq)){
                    outGoingWithoutCondition++;
                }
            }

            if (outGoingWithoutCondition > 1) {
                result.add(issue(el, "Element has multiple outgoing flows but is not a gateway—implicit split detected"));
            }
        }

        return result;
    }
}
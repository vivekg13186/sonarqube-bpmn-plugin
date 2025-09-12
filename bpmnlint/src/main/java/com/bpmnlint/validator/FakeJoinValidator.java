package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class FakeJoinValidator {

    private static boolean isActivityOrEvent(Element element) {
        String tag = element.tagName();
        return tag.endsWith(":activity") || tag.endsWith(":event");
    }

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();


            String activitySelector = "*|adhocSubProcess," +
                    "*|businessRuleTask," +
                    "*|callActivity," +
                    "*|manualTask," +
                    "*|receiveTask," +
                    "*|scriptTask," +
                    "*|sendTask," +
                    "*|serviceTask," +
                    "*|subProcess," +
                    "*|task," +
                    "*|transaction," +
                    "*|userTask";
            Elements activities = doc.select(activitySelector);

            for (Element activity : activities) {
                if(activity.select("*|incoming").size()>1){
                    result.add(issue(activity,"Incoming flows do not join"));
                }
            }
            String eventSelector = "*|boundaryEvent," +
                    "*|catchEvent," +
                    "*|endEvent," +
                    "*|implicitThrowEvent," +
                    "*|intermediateCatchEvent," +
                    "*|intermediateThrowEvent," +
                    "*|startEvent," +
                    "*|throwEvent" ;
            Elements events = doc.select(eventSelector);

            for (Element event : events) {
                if(event.select("*|incoming").size()>1){
                    result.add(issue(event,"Incoming flows do not join"));
                }
            }


        return result;
    }
}
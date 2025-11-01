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
                int incomingCount=activity.select("*|incoming").size();
                if(incomingCount>1){
                    String message =String.format("Fake Join detected: Element '%s' (task) is not a Gateway but has %d incoming sequence flows. Use a Gateway for merging paths." ,activity.attr("id"),incomingCount);
                    result.add(issue(activity,message));
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
                int incomingCount=event.select("*|incoming").size();
                if(incomingCount>1){
                    String message =String.format("Fake Join detected: Element '%s' (event) is not a Gateway but has %d incoming sequence flows. Use a Gateway for merging paths." ,event.attr("id"),incomingCount);
                    result.add(issue(event,message));
                }
            }


        return result;
    }
}
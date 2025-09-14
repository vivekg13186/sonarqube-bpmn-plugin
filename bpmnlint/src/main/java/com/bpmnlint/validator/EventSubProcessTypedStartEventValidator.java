package com.bpmnlint.validator;



import com.bpmnlint.Issue;
import com.bpmnlint.Xpath;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

import static com.bpmnlint.Util.*;

public class EventSubProcessTypedStartEventValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all event sub-processes
        Elements eventSubProcesses = doc.select("*|subProcess[triggeredByEvent=\"true\"]");

        for (Element subProcess : eventSubProcesses) {

            // Find start events inside the event sub-process
            Elements startEvents = subProcess.select("*|startEvent");

            for (Element startEvent : startEvents) {
                // Check if it has a defined event definition
                boolean hasTypedTrigger = !startEvent.select("*|messageEventDefinition, *|timerEventDefinition, *|signalEventDefinition, *|conditionalEventDefinition, *|errorEventDefinition, *|escalationEventDefinition, *|compensateEventDefinition").isEmpty();

                if (!hasTypedTrigger) {
                    result.add(issue(startEvent, "Start event is missing event definition"));
                }
            }


        }
        return result;
    }
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        Xpath xpath = new Xpath();
        ArrayList<Issue> result = new ArrayList<>();

        // Select all SubProcesses that are triggeredByEvent=true
        NodeList eventSubProcesses = xpath.getNodeList("//bpmn:subProcess[@triggeredByEvent='true']", doc);

        for (int i = 0; i < eventSubProcesses.getLength(); i++) {
            Node subProcess = eventSubProcesses.item(i);
            //System.out.println(getAttr(subProcess,"id"));
            NodeList startEvents = xpath.getNodeList("//bpmn:startEvent", subProcess);

            for (int j = 0; j < startEvents.getLength(); j++) {
                Node startEvent = startEvents.item(j);
                String id = getAttr(startEvent, "id");
               // System.out.println("Start id "+id);
                NodeList children = startEvent.getChildNodes();
                boolean hasDefinition = false;
                for(int k=0;k<children.getLength();k++){
                    //System.out.println(children.item(k).getNodeName());
                    if(children.item(k).getNodeName().endsWith("Definition")){
                        hasDefinition = true;
                    }
                }


                if (!hasDefinition) {
                    result.add(new Issue(id, getLineNumber(startEvent), "Start event is missing event definition"));
                }
            }
        }

        return result;
    }
}
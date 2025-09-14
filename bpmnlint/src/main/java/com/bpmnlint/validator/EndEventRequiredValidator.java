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

public class EndEventRequiredValidator {

    public static List<Issue> validate(Document doc) {
        List<Issue> result = new ArrayList<>();

        // Select all processes and sub-processes
        Elements containers = doc.select("*|process, *|subProcess");

        for (Element container : containers) {
            String containerId = container.attr("id");

            // Find all end events within this container
            Elements endEvents = container.select("*|endEvent");

            if (endEvents.isEmpty()) {
                result.add(issue(container, "Process is missing end event"));
            }
        }

        return result;
    }
    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        Xpath xpath = new Xpath();
        ArrayList<Issue> result = new ArrayList<>();
        NodeList processes = xpath.getNodeList("//bpmn:process",doc);
        NodeList subProcesses = xpath.getNodeList("//bpmn:subProcess",doc);
        for(int i=0;i<processes.getLength();i++){
            if(!xpath.hasChild("bpmn:endEvent",processes.item(i))){
                result.add(new Issue(getAttr(processes.item(i), "id"),1, "Process is missing end event"));
            }
        }
        for(int i=0;i<subProcesses.getLength();i++){
            if(!xpath.hasChild("bpmn:endEvent",subProcesses.item(i))){
                result.add(new Issue(getAttr(subProcesses.item(i), "id"),1, "Process is missing end event"));
            }
        }

        return result;
    }

}
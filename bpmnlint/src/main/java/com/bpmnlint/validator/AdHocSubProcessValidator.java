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

public class AdHocSubProcessValidator {


    public static List<Issue> validate(Document doc) {
        ArrayList<Issue> result = new ArrayList<>();

        Elements adHocSubProcesses = doc.select("*|adHocSubProcess");

        for (Element subProcess : adHocSubProcesses) {
            Elements flowElements = subProcess.select("*"); // all child elements

            for (Element flowElement : flowElements) {
                String tagName = flowElement.tagName();

                if ("startEvent".equals(tagName)) {
                    result.add(issue(flowElement, "A <Start Event> is not allowed in <Ad Hoc Sub Process>"));
                }

                if ("endEvent".equals(tagName)) {
                    result.add(issue(flowElement, "An <End Event> is not allowed in <Ad Hoc Sub Process>"));
                }

                if ("intermediateCatchEvent".equals(tagName) || "intermediateThrowEvent".equals(tagName)) {
                    Elements outgoing = flowElement.select("*|outgoing");
                    if (outgoing.isEmpty()) {
                        result.add(issue(flowElement, "An intermediate catch event inside <Ad Hoc Sub Process> must have an outgoing sequence flow"));
                    }
                }
            }

        }
        return result;
    }

    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        Xpath xpath = new Xpath();
        ArrayList<Issue> result = new ArrayList<>();
        NodeList adHocSubProcesses = xpath.getNodeList("//bpmn:adHocSubProcess",doc);
        for(int i=0;i<adHocSubProcesses.getLength();i++){
            Node node = adHocSubProcesses.item(i);
            NodeList startNodeList =xpath.getNodeList("bpmn:startEvent",node);
            for(int j=0;j<startNodeList.getLength();j++){
                Node startNode = startNodeList.item(j);
                String id = getAttr(startNode,"id");
              result.add(new Issue(id,1,"A <Start Event> is not allowed in <Ad Hoc Sub Process>"));
            }
            NodeList endNodeList =xpath.getNodeList("bpmn:endEvent",node);
            for(int k=0;k<endNodeList.getLength();k++){
                Node startNode = endNodeList.item(k);
                String id = getAttr(startNode,"id");
                result.add(new Issue(id,1,"An <End Event> is not allowed in <Ad Hoc Sub Process>"));
            }
        }

        return result;
    }


}



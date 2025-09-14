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

public class ConditionalFlowsValidator {

    public static List<Issue> validate(Document doc) {

        List<Issue> result = new ArrayList<>();

        // Select all gateways and activities that can have conditional flows
        Elements nodes = doc.select(":has(> *|outgoing)");

        for (Element node : nodes) {

            if(node.id().isEmpty())continue;
            System.out.println("*|sequenceFlow[sourceRef=" + node.attr("id") + "]");
            Elements outgoingFlows = doc.select("*|sequenceFlow[sourceRef=" + node.attr("id") + "]");

            for (Element flow : outgoingFlows) {
                boolean hasCondition = !flow.select("*|conditionExpression").isEmpty();
                boolean isDefault = node.hasAttr("default") && node.attr("default").equals(flow.attr("id"));
                System.out.println("hasCondition "+hasCondition+" isDefault "+isDefault);
                if (!hasCondition && !isDefault) {
                    result.add(issue(flow, "Sequence flow is missing condition"));
                }
            }
        }

        return result;
    }

    public static boolean hasOneCondition(Xpath xpath, NodeList sequence) throws XPathExpressionException {
        for(int i=0;i<sequence.getLength();i++){
            if(xpath.getNodeList("bpmn:conditionExpression",sequence.item(i)).getLength()>0){
                return true;
            }
        }
        return false;
    }

    public static List<Issue> validate(org.w3c.dom.Document doc) throws XPathExpressionException {
        Xpath xpath = new Xpath();
        ArrayList<Issue> result = new ArrayList<>();
         NodeList nodeWithOutgoingChild = xpath.getNodeList("//*[bpmn:outgoing]",doc);
         for(int i=0;i<nodeWithOutgoingChild.getLength();i++){
             Node n1 = nodeWithOutgoingChild.item(i);
             String id = getAttr(n1,"id");
             if(id.isEmpty())continue;
             String defaultFlowId= getAttr(n1,"default");
             NodeList sequenceFlows = xpath.getNodeList(String.format("//bpmn:sequenceFlow[@sourceRef='%s']",id),doc);
             //has conditional forking
             if(hasOneCondition(xpath,sequenceFlows)||!defaultFlowId.isEmpty()) {
                 System.out.printf("//sequenceFlow[@sourceRef='%s'] %d\n", id, sequenceFlows.getLength());
                 for (int j = 0; j < sequenceFlows.getLength(); j++) {
                     Node sq = sequenceFlows.item(j);

                     String sid = getAttr(sq, "id");
                     boolean hasConditionExpression = xpath.getNodeList("bpmn:conditionExpression", sq).getLength() > 0;

                     boolean hasDefaultFlow = defaultFlowId.equals(sid);
                     System.out.printf("sid  %s has condition %s has default %s\n", sid, hasConditionExpression, hasDefaultFlow);
                     if (!hasConditionExpression && !hasDefaultFlow) {
                         result.add(new Issue(sid, getLineNumber(n1), "Sequence flow is missing condition"));
                     }
                 }
             }
         }
        return result;
    }



}
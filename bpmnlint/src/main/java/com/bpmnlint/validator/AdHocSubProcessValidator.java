package com.bpmnlint.validator;

import com.bpmn.model.*;
import com.bpmnlint.Issue;
import jakarta.xml.bind.JAXBElement;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    public static List<Issue> validate(TDefinitions definitions){
        ArrayList<Issue> result = new ArrayList<>();
        List<TAdHocSubProcess> adHocSubProcesses = getAllAdhocProcess(definitions);
        adHocSubProcesses.forEach(ap->{
            List<JAXBElement<? extends TFlowElement>> flowElements = ap.getFlowElement();
            if(has(flowElements, TStartEvent.class)){
                result.add(new Issue(ap.getId(),1, "A <Start Event> is not allowed in <Ad Hoc Sub Process>"));
            }
            if(has(flowElements, TEndEvent.class)){
                result.add(new Issue(ap.getId(),1, "An <End Event> is not allowed in <Ad Hoc Sub Process>"));
            };
        });
        return result;
    }


}



package com.bpmnlint.validator;

import com.bpmnlint.Issue;
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
            Elements flowElements = subProcess.children(); // all child elements

            for (Element fe : flowElements) {

                if(!fe.select("*|startEvent").isEmpty()){
                    result.add(issue(fe, "A <Start Event> is not allowed in <Ad Hoc Sub Process>"));
                }


                if (!fe.select("*|endEvent").isEmpty()) {
                    result.add(issue(fe, "An <End Event> is not allowed in <Ad Hoc Sub Process>"));
                }

                if(!fe.select("*|intermediateCatchEvent").isEmpty()){
                    Elements outgoing = fe.select("*|outgoing");
                    if (outgoing.isEmpty()) {
                        result.add(issue(fe, "An intermediate catch event inside <Ad Hoc Sub Process> must have an outgoing sequence flow"));
                    }
                }
                if(!fe.select("*|intermediateThrowEvent").isEmpty()){
                    Elements outgoing = fe.select("*|outgoing");
                    if (outgoing.isEmpty()) {
                        result.add(issue(fe, "An intermediate catch event inside <Ad Hoc Sub Process> must have an outgoing sequence flow"));
                    }
                }


            }

        }
        return result;
    }
}



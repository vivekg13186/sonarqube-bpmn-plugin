package com.bpmnlint;

import com.bpmn.model.TAdHocSubProcess;
import com.bpmn.model.TDefinitions;
import com.bpmn.model.TFlowElement;
import com.bpmn.model.TProcess;
import jakarta.xml.bind.JAXBElement;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static Issue issue(Element element, String message) {
        return new Issue(element.id(), element.sourceRange().start().lineNumber(), message);
    }

    public static List<TAdHocSubProcess> getAllAdhocProcess(TDefinitions definitions) {
        List<TAdHocSubProcess> result = new ArrayList<>();
        definitions.getRootElement().forEach(re -> {
            if (re.getValue() instanceof TProcess) {
                TProcess process = (TProcess) re.getValue();
                process.getFlowElement().forEach(fe -> {
                    if (fe.getValue() instanceof TAdHocSubProcess) {
                        result.add((TAdHocSubProcess) fe.getValue());
                    }
                });
            }
        });
        return result;
    }

    public static boolean has(List<JAXBElement<? extends TFlowElement>> nodes, Class<? extends TFlowElement> type) {

        for (JAXBElement<? extends TFlowElement> element : nodes) {
            TFlowElement e = element.getValue();
            if (type.isInstance(e)) {
                return true;
            }
        }
        return false;
    }


}

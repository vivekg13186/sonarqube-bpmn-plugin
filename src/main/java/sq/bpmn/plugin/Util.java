package sq.bpmn.plugin;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.util.HashMap;
import java.util.Objects;

public class Util {

    public boolean hasChild(ModelElementInstance instance , Class  type){
       return !instance.getChildElementsByType(type).isEmpty();
    }
    public boolean noChild(ModelElementInstance instance ,Class  type){
        return instance.getChildElementsByType(type).isEmpty();
    }

    public static int getLineNUmber(String id,HashMap<String, Integer> lineMap){
       return Objects.requireNonNullElse(lineMap.get(id),1);
    }
}

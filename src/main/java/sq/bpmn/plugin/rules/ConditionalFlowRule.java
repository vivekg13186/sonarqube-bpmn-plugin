package sq.bpmn.plugin.rules;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.IssueMaker;

import java.util.Collection;
import java.util.HashMap;

@Rule(key = ConditionalFlowRule.RULE_KEY, name = "Conditional Flows", description = "Ensures that conditions on sequence flows are consistently set. If a sequence flow outgoing from a conditional forking gateway or activity is default or any sequence flow has a condition attached, all others have to have to have respective condition meta-data attached, too.")
public class ConditionalFlowRule implements BpmnRule {
    public static final String RULE_KEY = "conditional-flows";

    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker)    {
        try{

            Elements elements =document.select(":has(> *|outgoing)");
            for(Element element:elements){
                Elements outgoings = element.select("*|outgoing");
                for(Element outgoing: outgoings) {
                    if (!isDefaultFlow(outgoing,element) && noCondition(outgoing,document)) {
                        issueMaker.newIssue(element, "Sequence flow is missing condition", file, sensorContext, ruleKey);
                    }
                }
            }

        } catch (Exception ignored) {
        }


    }


    private boolean isDefaultFlow(Element outgoing,Element parent){
        String defaultNode = parent.attr("default");
        String id = outgoing.text();
        return id.equals(defaultNode);
    }
    private boolean noCondition(Element outgoing,Document document){
        String id = outgoing.text();
        return document.select("#"+id+" > *|conditionExpression").isEmpty();
    }

    @Override
    public void execute(SensorContext sensorContext, BpmnModelInstance model, InputFile file, RuleKey ruleKey, IssueMaker issueMaker, HashMap<String, Integer> lineMap) {
        Collection<ModelElementInstance> instances = model.getModelElementsByType(ModelElementInstance.class);
         //
    }
}

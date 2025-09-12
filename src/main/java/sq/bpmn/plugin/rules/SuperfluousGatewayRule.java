package sq.bpmn.plugin.rules;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import sq.bpmn.plugin.BpmnRule;
import sq.bpmn.plugin.Helper;
import sq.bpmn.plugin.IssueMaker;

@Rule(key = SuperfluousGatewayRule.RULE_KEY, name = "Redundant Gateway", description = "A rule that disallows complex gateways.A rule that checks, whether a gateway has only one source and target. Those gateways are superfluous since they don't do anything.")

public class SuperfluousGatewayRule implements BpmnRule {
    public static final String RULE_KEY = "superfluous-gateway";
    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker) {

        Elements events = Helper.getAllGatewayElements(document);

        for (Element event : events) {
            if(event.select("*|incoming").size()==1&&event.select("*|outgoing").size()==1){
                issueMaker.newIssue(event,"Gateway is superfluous. It only has one source and target.",file,sensorContext,ruleKey);
            }
        }


    }
}

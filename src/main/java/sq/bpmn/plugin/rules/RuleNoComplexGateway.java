package sq.bpmn.plugin.rules;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = RuleNoComplexGateway.RULE_KEY, name = "No Complex Gateway", description = "A rule that disallows complex gateways.")

public class RuleNoComplexGateway implements BpmnRule{
    public static final String RULE_KEY = "no-complex-gateway";
    @Override
    public void execute(SensorContext sensorContext, Document document, InputFile file, RuleKey ruleKey, IssueMaker issueMaker) {
        Elements complexGateways = document.select("*|complexGateway");
        for(Element complexGateway : complexGateways){
            issueMaker.newIssue(complexGateway,"Element type <complexGateway> is discouraged",file,sensorContext,ruleKey);
        }

    }
}

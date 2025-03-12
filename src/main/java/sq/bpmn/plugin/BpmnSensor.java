package sq.bpmn.plugin;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

import sq.bpmn.plugin.languages.BpmnLanguage;
import sq.bpmn.plugin.rules.BpmnRule;
import sq.bpmn.plugin.rules.SingleBlankStartEventRule;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import sq.bpmn.plugin.rules.StartEventRequiredRule;

public class BpmnSensor implements Sensor {

    private final Checks<BpmnRule> checks;

    public BpmnSensor(CheckFactory checkFactory) {
        checks = checkFactory.create(BpmnRulesDefinition.REPO_KEY);
        checks.addAnnotatedChecks(SingleBlankStartEventRule.class);
        checks.addAnnotatedChecks(StartEventRequiredRule.class);
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name(BpmnRulesDefinition.REPO_KEY + "sensor");
        descriptor.onlyOnLanguages(BpmnLanguage.KEY);
        descriptor.createIssuesForRuleRepository(BpmnRulesDefinition.REPO_KEY);
    }

    @Override
    public void execute(SensorContext context) {
        FilePredicates p = context.fileSystem().predicates();
        for (InputFile inputFile : context.fileSystem().inputFiles(p.hasLanguages(BpmnLanguage.KEY))) {
            try{
                Document document = Jsoup.parse(inputFile.contents());
                checks.all().forEach(check -> check.execute(context, document,inputFile, checks.ruleKey(check)));
            } catch (Exception ignored) {
                ;
            }

        }
    }



}

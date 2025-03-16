package sq.bpmn.plugin.languages;




import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import sq.bpmn.plugin.BpmnRulesDefinition;
import sq.bpmn.plugin.rules.RuleAdHocSubProcessEvents;
import sq.bpmn.plugin.rules.RuleStartEventRequired;


/**
 * Default, BuiltIn Quality Profile for the projects having files of the language "foo"
 */
public final class BpmnQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("BPMN Rules", BpmnLanguage.KEY);
        profile.setDefault(true);


        NewBuiltInActiveRule adhocSubProcessEventRule = profile.activateRule(BpmnRulesDefinition.REPO_KEY, RuleAdHocSubProcessEvents.RULE_KEY);
        adhocSubProcessEventRule.overrideSeverity("BLOCKER");


        NewBuiltInActiveRule rule2 = profile.activateRule(BpmnRulesDefinition.REPO_KEY, RuleStartEventRequired.RULE_KEY);
        rule2.overrideSeverity("BLOCKER");




        profile.done();
    }

}
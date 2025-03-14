package sq.bpmn.plugin.languages;




import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import sq.bpmn.plugin.BpmnRulesDefinition;
import sq.bpmn.plugin.rules.AdHocSubProcessEvents;
import sq.bpmn.plugin.rules.StartEventRequiredRule;


/**
 * Default, BuiltIn Quality Profile for the projects having files of the language "foo"
 */
public final class BpmnQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("BPMN Rules", BpmnLanguage.KEY);
        profile.setDefault(true);


        NewBuiltInActiveRule adhocSubProcessEventRule = profile.activateRule(BpmnRulesDefinition.REPO_KEY, AdHocSubProcessEvents.RULE_KEY);
        adhocSubProcessEventRule.overrideSeverity("BLOCKER");


        NewBuiltInActiveRule rule2 = profile.activateRule(BpmnRulesDefinition.REPO_KEY, StartEventRequiredRule.RULE_KEY);
        rule2.overrideSeverity("BLOCKER");




        profile.done();
    }

}
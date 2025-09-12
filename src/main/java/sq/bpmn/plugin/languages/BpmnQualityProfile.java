package sq.bpmn.plugin.languages;




import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import sq.bpmn.plugin.BpmnRulesDefinition;
import sq.bpmn.plugin.rules.*;


public final class BpmnQualityProfile implements BuiltInQualityProfilesDefinition {

    private NewBuiltInQualityProfile profile;
    public static final String INFO = "INFO";
    public static final String MINOR = "MINOR";
    public static final String MAJOR = "MAJOR";
    public static final String CRITICAL = "CRITICAL";
    public static final String BLOCKER = "BLOCKER";

    @Override
    public void define(Context context) {
         profile = context.createBuiltInQualityProfile("BPMN Rules", BpmnLanguage.KEY);
        profile.setDefault(true);

        addRuleToProfile( RuleSingleBlankStartEvent.RULE_KEY, BLOCKER);
        addRuleToProfile( AdHocSubProcessEventsRule.RULE_KEY, BLOCKER);
        addRuleToProfile( ConditionalFlowRule.RULE_KEY, BLOCKER);
        addRuleToProfile( EndEventRequiredRule.RULE_KEY, BLOCKER);
        addRuleToProfile( EventSubProcessTypedStartEventRule.RULE_KEY, BLOCKER);
        addRuleToProfile( FakeJoinsRule.RULE_KEY,MINOR);
        addRuleToProfile(NoComplexGatewayRule.RULE_KEY,BLOCKER);
        addRuleToProfile(SingleEventDefinitionRule.RULE_KEY,BLOCKER);
        addRuleToProfile(SuperfluousGatewayRule.RULE_KEY,BLOCKER);


        profile.done();
    }

    private void addRuleToProfile(String ruleKey,String severity){
        NewBuiltInActiveRule rule = profile.activateRule(BpmnRulesDefinition.REPO_KEY, ruleKey);
        rule.overrideSeverity(severity);
    }
}
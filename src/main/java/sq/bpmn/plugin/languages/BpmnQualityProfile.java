package sq.bpmn.plugin.languages;




import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import sq.bpmn.plugin.BpmnRulesDefinition;
import sq.bpmn.plugin.rules.*;


/**
 * Default, BuiltIn Quality Profile for the projects having files of the language "foo"
 */
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
        addRuleToProfile( RuleAdHocSubProcessEvents.RULE_KEY, BLOCKER);
        addRuleToProfile(  ConditionalFlow.RULE_KEY, BLOCKER);
        addRuleToProfile(  RuleEndEventRequired.RULE_KEY, BLOCKER);
         addRuleToProfile( RuleEventSubProcessTypedStartEvent.RULE_KEY, BLOCKER);
        addRuleToProfile( RuleFakeJoins.RULE_KEY,MINOR);



        profile.done();
    }

    private void addRuleToProfile(String ruleKey,String severity){
        NewBuiltInActiveRule rule = profile.activateRule(BpmnRulesDefinition.REPO_KEY, ruleKey);
        rule.overrideSeverity(severity);
    }
}
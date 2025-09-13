package sq.bpmn.plugin;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import static sq.bpmn.plugin.Constants.*;

public class BpmnRecommendedProfile  implements BuiltInQualityProfilesDefinition {
    private NewBuiltInQualityProfile profile;
    public static final String INFO = "INFO";
    public static final String MINOR = "MINOR";
    public static final String MAJOR = "MAJOR";
    public static final String CRITICAL = "CRITICAL";
    public static final String BLOCKER = "BLOCKER";

    @Override
    public void define(Context context) {
        profile = context.createBuiltInQualityProfile("BPMN Recommended Profile", BpmnLanguage.KEY);
        profile.setDefault(true);
        addRuleToProfile(ID_AdHocSubProcess ,BLOCKER);
        addRuleToProfile(ID_ConditionalFlows ,BLOCKER);
        addRuleToProfile(ID_EndEventRequired ,BLOCKER);
        addRuleToProfile(ID_EventSubProcessTypedStartEvent ,BLOCKER);
        addRuleToProfile(ID_FakeJoin ,MINOR);
        //addRuleToProfile(ID_GlobalElement ,MINOR);
        addRuleToProfile(ID_LabelRequired ,BLOCKER);
        addRuleToProfile(ID_LinkEvent ,BLOCKER);
        addRuleToProfile(ID_NoBPMNDI ,BLOCKER);
        addRuleToProfile(ID_NoComplexGateway ,BLOCKER);
        addRuleToProfile(ID_NoDisconnected ,BLOCKER);
        addRuleToProfile(ID_NoDuplicateSequenceFlows ,BLOCKER);
        addRuleToProfile(ID_NoGatewayJoinFork ,BLOCKER);
        addRuleToProfile(ID_NoImplicitEnd ,BLOCKER);
        addRuleToProfile(ID_NoImplicitSplit ,BLOCKER);
        addRuleToProfile(ID_NoImplicitStart ,BLOCKER);
        addRuleToProfile(ID_NoInclusiveGateway ,BLOCKER);
        addRuleToProfile(ID_NoOverlappingElements ,MINOR);
        addRuleToProfile(ID_SingleBlankStartEvent ,BLOCKER);
        addRuleToProfile(ID_SingleEventDefinition ,BLOCKER);
        addRuleToProfile(ID_StartEventRequired ,BLOCKER);
        addRuleToProfile(ID_SubProcessBlankStartEvent ,BLOCKER);
        addRuleToProfile(ID_SuperfluousGateway ,MINOR);
        addRuleToProfile(ID_SuperfluousTermination ,MINOR);
        profile.done();
    }

    private void addRuleToProfile(String ruleKey,String severity){
        NewBuiltInActiveRule rule = profile.activateRule(BpmnRepo ,ruleKey);

        rule.overrideSeverity(severity);
    }
}

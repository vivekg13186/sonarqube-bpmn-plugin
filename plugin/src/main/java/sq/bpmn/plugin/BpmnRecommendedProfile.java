package sq.bpmn.plugin;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

import static sq.bpmn.plugin.Constants.*;
import static sq.bpmn.plugin.Constants.ID_EventSubProcessTypedStartEvent;
import static sq.bpmn.plugin.Constants.ID_FakeJoin;
import static sq.bpmn.plugin.Constants.ID_GlobalElement;
import static sq.bpmn.plugin.Constants.ID_LabelRequired;
import static sq.bpmn.plugin.Constants.ID_LinkEvent;
import static sq.bpmn.plugin.Constants.ID_NoBPMNDI;
import static sq.bpmn.plugin.Constants.ID_NoComplexGateway;
import static sq.bpmn.plugin.Constants.ID_NoDisconnected;
import static sq.bpmn.plugin.Constants.ID_NoDuplicateSequenceFlows;
import static sq.bpmn.plugin.Constants.ID_NoGatewayJoinFork;
import static sq.bpmn.plugin.Constants.ID_NoImplicitEnd;
import static sq.bpmn.plugin.Constants.ID_NoImplicitSplit;
import static sq.bpmn.plugin.Constants.ID_NoImplicitStart;
import static sq.bpmn.plugin.Constants.ID_NoInclusiveGateway;
import static sq.bpmn.plugin.Constants.ID_NoOverlappingElements;
import static sq.bpmn.plugin.Constants.ID_SingleBlankStartEvent;
import static sq.bpmn.plugin.Constants.ID_SingleEventDefinition;
import static sq.bpmn.plugin.Constants.ID_StartEventRequired;
import static sq.bpmn.plugin.Constants.ID_SubProcessBlankStartEvent;
import static sq.bpmn.plugin.Constants.ID_SuperfluousGateway;
import static sq.bpmn.plugin.Constants.ID_SuperfluousTermination;

public class BpmnRecommendedProfile  implements BuiltInQualityProfilesDefinition {
    private BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile profile;
    public static final String INFO = "INFO";
    public static final String MINOR = "MINOR";
    public static final String MAJOR = "MAJOR";
    public static final String CRITICAL = "CRITICAL";
    public static final String BLOCKER = "BLOCKER";

    @Override
    public void define(BuiltInQualityProfilesDefinition.Context context) {
        profile = context.createBuiltInQualityProfile("BPMN Rules", BpmnLanguage.KEY);
        profile.setDefault(true);
        addRuleToProfile(ID_AdHocSubProcess ,BLOCKER);
        addRuleToProfile(ID_ConditionalFlows ,BLOCKER);
        addRuleToProfile(ID_EndEventRequired ,BLOCKER);
        addRuleToProfile(ID_EventSubProcessTypedStartEvent ,BLOCKER);
        addRuleToProfile(ID_FakeJoin ,MINOR);
        addRuleToProfile(ID_GlobalElement ,MINOR);
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
        BuiltInQualityProfilesDefinition.NewBuiltInActiveRule rule = profile.activateRule("bpmn-repo" ,ruleKey);
        rule.overrideSeverity(severity);
    }
}

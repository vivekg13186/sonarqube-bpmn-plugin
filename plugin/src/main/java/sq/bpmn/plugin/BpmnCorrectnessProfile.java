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

public class BpmnCorrectnessProfile implements BuiltInQualityProfilesDefinition {
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
        addRuleToProfile(ID_ConditionalFlows ,MINOR);
        addRuleToProfile(ID_EndEventRequired ,MINOR);
        addRuleToProfile(ID_EventSubProcessTypedStartEvent ,BLOCKER);
        addRuleToProfile(ID_FakeJoin ,MINOR);
        addRuleToProfile(ID_GlobalElement ,MINOR);
        addRuleToProfile(ID_LabelRequired ,MINOR);
        addRuleToProfile(ID_LinkEvent ,BLOCKER);
        addRuleToProfile(ID_NoBPMNDI ,MINOR);
        addRuleToProfile(ID_NoComplexGateway ,MINOR);
        addRuleToProfile(ID_NoDisconnected ,MINOR);
        addRuleToProfile(ID_NoDuplicateSequenceFlows ,MAJOR);
        addRuleToProfile(ID_NoGatewayJoinFork ,MINOR);
        addRuleToProfile(ID_NoImplicitEnd ,MINOR);
        addRuleToProfile(ID_NoImplicitSplit ,MINOR);
        addRuleToProfile(ID_NoImplicitStart ,MINOR);
        addRuleToProfile(ID_NoInclusiveGateway ,MINOR);
        addRuleToProfile(ID_NoOverlappingElements ,MINOR);
        addRuleToProfile(ID_SingleBlankStartEvent ,BLOCKER);
        addRuleToProfile(ID_SingleEventDefinition ,MINOR);
        addRuleToProfile(ID_StartEventRequired ,MINOR);
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

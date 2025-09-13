package sq.bpmn.plugin;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.server.rule.RulesDefinition;


import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static sq.bpmn.plugin.Constants.*;

public class RuleTest {
    @Test
    public void testSensorDetectsIssue() throws IOException {
        SensorContextTester context = SensorContextTester.create(File.createTempFile("",""));

        InputFile inputFile = TestInputFileBuilder.create("moduleKey", "process.bpmn")
                .setLanguage(BpmnLanguage.KEY)
                .setType(InputFile.Type.MAIN)
                .setContents("<bpmn:process></bpmn:process>")
                .build();

        context.fileSystem().add(inputFile);

        new BpmnSensor().execute(context);

        assertThat(context.allIssues()).hasSize(1); // or whatever your rule expects
    }

    @Test
    public void testRulesAreDefined() {
        RulesDefinition.Context context = new RulesDefinition.Context();
        new BpmnRuleDefinition().define(context);

        RulesDefinition.Repository repo = context.repository("bpmn-repo");
        assertThat(repo.rules()).isNotEmpty();
        assertThat(repo.rule(ID_AdHocSubProcess)).isNotNull();
        assertThat(repo.rule(ID_ConditionalFlows)).isNotNull();
        assertThat(repo.rule(ID_EndEventRequired)).isNotNull();
        assertThat(repo.rule(ID_EventSubProcessTypedStartEvent)).isNotNull();
        assertThat(repo.rule(ID_FakeJoin)).isNotNull();
        //assertThat(repo.rule(ID_GlobalElement)).isNotNull();
        assertThat(repo.rule(ID_LabelRequired)).isNotNull();
        assertThat(repo.rule(ID_LinkEvent)).isNotNull();
        assertThat(repo.rule(ID_NoBPMNDI)).isNotNull();
        assertThat(repo.rule(ID_NoComplexGateway)).isNotNull();
        assertThat(repo.rule(ID_NoDisconnected)).isNotNull();
        assertThat(repo.rule(ID_NoDuplicateSequenceFlows)).isNotNull();
        assertThat(repo.rule(ID_NoGatewayJoinFork)).isNotNull();
        assertThat(repo.rule(ID_NoImplicitEnd)).isNotNull();
        assertThat(repo.rule(ID_NoImplicitSplit)).isNotNull();
        assertThat(repo.rule(ID_NoImplicitStart)).isNotNull();
        assertThat(repo.rule(ID_NoInclusiveGateway)).isNotNull();
        assertThat(repo.rule(ID_NoOverlappingElements)).isNotNull();
        assertThat(repo.rule(ID_SingleBlankStartEvent)).isNotNull();
        assertThat(repo.rule(ID_SingleEventDefinition)).isNotNull();
        assertThat(repo.rule(ID_StartEventRequired)).isNotNull();
        assertThat(repo.rule(ID_SubProcessBlankStartEvent)).isNotNull();
        assertThat(repo.rule(ID_SuperfluousGateway)).isNotNull();
        assertThat(repo.rule(ID_SuperfluousTermination)).isNotNull();
    }




}

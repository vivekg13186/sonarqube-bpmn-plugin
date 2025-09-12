package sq.bpmn.plugin;

import com.bpmnlint.Issue;
import com.bpmnlint.validator.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Condition;

import static sq.bpmn.plugin.Constants.*;

public class BpmnSensor implements Sensor {

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("BPMN Sensor").onlyOnLanguage(BpmnLanguage.KEY);
    }
    public void newIssue( int lno,String message, InputFile file, SensorContext sensorContext, String ruleKey) {
        NewIssue newIssue = sensorContext.newIssue();
        newIssue
                .forRule(RuleKey.of("bpmn-repo", ruleKey))
                .at(newIssue.newLocation()
                        .on(file).message(message)
                        .at(file.selectLine(lno)))

                .save();

    }

    public void validate(List<Issue> issues,String ruleId,InputFile inputFile,SensorContext context){
        for(Issue a : issues){
            newIssue(  a.getLine(),a.getMessage(), inputFile, context, ruleId );
        }
    }

    @Override
    public void execute(SensorContext context) {
        FileSystem fs = context.fileSystem();
        Iterable<InputFile> bpmnFiles = fs.inputFiles(fs.predicates().hasLanguage(BpmnLanguage.KEY));
        for (InputFile inputFile : bpmnFiles) {
            // Apply your custom rules here
            try {
                Document document = Jsoup.parse(inputFile.contents(), Parser.xmlParser().setTrackPosition(true));
                validate(AdHocSubProcessValidator.validate(document),ID_AdHocSubProcess,inputFile,context);
                validate(ConditionalFlowsValidator.validate(document),ID_ConditionalFlows,inputFile,context);
                validate(EndEventRequiredValidator.validate(document),ID_EndEventRequired,inputFile,context);
                validate(EventSubProcessTypedStartEventValidator.validate(document),ID_EventSubProcessTypedStartEvent,inputFile,context);
                validate(FakeJoinValidator.validate(document),ID_FakeJoin,inputFile,context);
               // validate(AdHocSubProcessValidator.validate(document),ID_GlobalElement,inputFile,context);
                validate(LabelRequiredValidator.validate(document),ID_LabelRequired,inputFile,context);
                validate(LinkEventValidator.validate(document),ID_LinkEvent,inputFile,context);
                validate(NoBPMNDIValidator.validate(document),ID_NoBPMNDI,inputFile,context);
                validate(NoComplexGatewayValidator.validate(document),ID_NoComplexGateway,inputFile,context);
                validate(NoDisconnectedValidator.validate(document),ID_NoDisconnected,inputFile,context);
                validate(NoDuplicateSequenceFlowsValidator.validate(document),ID_NoDuplicateSequenceFlows,inputFile,context);
                validate(NoGatewayJoinForkValidator.validate(document),ID_NoGatewayJoinFork,inputFile,context);
                validate(NoImplicitEndValidator.validate(document),ID_NoImplicitEnd,inputFile,context);
                validate(NoImplicitSplitValidator.validate(document),ID_NoImplicitSplit,inputFile,context);
                validate(NoImplicitStartValidator.validate(document),ID_NoImplicitStart,inputFile,context);
                validate(NoInclusiveGatewayValidator.validate(document),ID_NoInclusiveGateway,inputFile,context);
                validate(NoOverlappingElementsValidator.validate(document),ID_NoOverlappingElements,inputFile,context);
                validate(SingleBlankStartEventValidator.validate(document),ID_SingleBlankStartEvent,inputFile,context);
                validate(SingleEventDefinitionValidator.validate(document),ID_SingleEventDefinition,inputFile,context);
                validate(StartEventRequiredValidator.validate(document),ID_StartEventRequired,inputFile,context);
                validate(SubProcessBlankStartEventValidator.validate(document),ID_SubProcessBlankStartEvent,inputFile,context);
                validate(SuperfluousGatewayValidator.validate(document),ID_SuperfluousGateway,inputFile,context);
                validate(SuperfluousTerminationValidator.validate(document),ID_SuperfluousTermination,inputFile,context);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
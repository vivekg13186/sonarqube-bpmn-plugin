package sq.bpmn.plugin;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Range;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.rule.RuleKey;

public class TestIssueMaker implements IssueMaker {
    public boolean failed=false;
    @Override
    public void newIssue(Element element, String message, InputFile file, SensorContext context, RuleKey key) {
        failed = true;
        Range.Position start = element.sourceRange().start();
        Range.Position end = element.sourceRange().end();
        System.out.printf("New Issue : (%d,%d) (%d,%d) - %s\n",start.lineNumber(),start.columnNumber(),end.lineNumber(),end.columnNumber(),message);
    }
}

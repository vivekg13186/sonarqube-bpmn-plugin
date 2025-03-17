package sq.bpmn.plugin;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import sq.bpmn.plugin.languages.BpmnLanguage;
import sq.bpmn.plugin.rules.*;

public class BpmnRulesDefinition implements RulesDefinition {
    static final String KEY = "bpmn";
    public static final String REPO_KEY = BpmnLanguage.KEY + "-" + KEY;
    private static final String REPO_NAME = BpmnLanguage.KEY + "- " + KEY + " repo";

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPO_KEY, BpmnLanguage.KEY).setName(REPO_NAME);

        RulesDefinitionAnnotationLoader rulesDefinitionAnnotationLoader = new RulesDefinitionAnnotationLoader();
        rulesDefinitionAnnotationLoader.load(repository, RuleSingleBlankStartEvent.class, RuleStartEventRequired.class);
        rulesDefinitionAnnotationLoader.load(repository, RuleAdHocSubProcessEvents.class);
        rulesDefinitionAnnotationLoader.load(repository, ConditionalFlow.class);
        rulesDefinitionAnnotationLoader.load(repository, RuleEndEventRequired.class);
        rulesDefinitionAnnotationLoader.load(repository, RuleEventSubProcessTypedStartEvent.class);
        rulesDefinitionAnnotationLoader.load(repository, RuleFakeJoins.class);
        repository.done();
    }
}

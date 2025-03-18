# Sonar Qube plugin for camunda BPMN files

    This plugin provide support for .bpmn file.Includes default bpmnlint rules.


## How to install 
- Copy **"sonar-bpmn-plugin-1.0.jar"** from **"dist"** folder
- Paste the file in following "<sonar server path>/extensions/plugins"
- Restart the server



## bpmnlint rules migration
[https://github.com/bpmn-io/bpmnlint/tree/main/docs/rules](https://github.com/bpmn-io/bpmnlint/tree/main/docs/rules)

|Rule| Status |
|--|--------|
|ad-hoc-sub-process| ✅      |
|conditional-flows| ✅      |
|end-event-required| ✅      |
|event-sub-process-typed-start-event| ✅      |
|fake-join| ✅      |
|global|        |
|label-required|        |
|link-event|        |
|no-bpmndi|        |
|no-complex-gateway|        |
|no-disconnected|        |
|duplicate-sequence-flows|        |
|no-gateway-join-fork|        |
|no-implicit-end|        |
|no-implicit-split|        |
|no-implicit-start|        |
|no-inclusive-gateway|        |
|no-overlapping-elements|        |
|single-blank-start-event|        |
|single-event-definition|        |
|start-event-required|        |
|sub-process-blank-start-event|        |
|superfluous-gateway|        |
|superfluous-termination|        |
|ad-hoc-sub-process|        |
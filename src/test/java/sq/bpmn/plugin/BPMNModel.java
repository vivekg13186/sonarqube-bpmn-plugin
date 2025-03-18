package sq.bpmn.plugin;


public class BPMNModel{
    public interface  Interface
            extends  RootElement
    {}

    public interface  Operation
            extends  BaseElement
    {}

    public interface  EndPoint
            extends  RootElement
    {}

    public interface  Auditing
            extends  BaseElement
    {}

    public interface  GlobalTask
            extends  CallableElement
    {}

    public interface  Monitoring
            extends  BaseElement
    {}

    public interface  Performer
            extends  ResourceRole
    {}

    public interface  Process
            extends  FlowElementsContainer,CallableElement
    {}

    public interface  LaneSet
            extends  BaseElement
    {}

    public interface  Lane
            extends  BaseElement
    {}

    public interface  GlobalManualTask
            extends  GlobalTask
    {}

    public interface  ManualTask
            extends  Task
    {}

    public interface  UserTask
            extends  Task
    {}

    public interface  Rendering
            extends  BaseElement
    {}

    public interface  HumanPerformer
            extends  Performer
    {}

    public interface  PotentialOwner
            extends  HumanPerformer
    {}

    public interface  GlobalUserTask
            extends  GlobalTask
    {}

    public interface  Gateway
            extends  FlowNode
    {}

    public interface  EventBasedGateway
            extends  Gateway
    {}

    public interface  ComplexGateway
            extends  Gateway
    {}

    public interface  ExclusiveGateway
            extends  Gateway
    {}

    public interface  InclusiveGateway
            extends  Gateway
    {}

    public interface  ParallelGateway
            extends  Gateway
    {}

    public interface  RootElement
            extends  BaseElement
    {}

    public interface  Relationship
            extends  BaseElement
    {}

    public interface  BaseElement
    {}

    public interface  Extension
    {}

    public interface  ExtensionDefinition
    {}

    public interface  ExtensionAttributeDefinition
    {}

    public interface  ExtensionElements
    {}

    public interface  Documentation
            extends  BaseElement
    {}

    public interface  Event
            extends  FlowNode,InteractionNode
    {}

    public interface  IntermediateCatchEvent
            extends  CatchEvent
    {}

    public interface  IntermediateThrowEvent
            extends  ThrowEvent
    {}

    public interface  EndEvent
            extends  ThrowEvent
    {}

    public interface  StartEvent
            extends  CatchEvent
    {}

    public interface  ThrowEvent
            extends  Event
    {}

    public interface  CatchEvent
            extends  Event
    {}

    public interface  BoundaryEvent
            extends  CatchEvent
    {}
    public interface  Escalation
            extends  RootElement
    {}

    public interface  EventDefinition
            extends  RootElement
    {}

    public interface  CancelEventDefinition
            extends  EventDefinition
    {}

    public interface  ErrorEventDefinition
            extends  EventDefinition
    {}

    public interface  TerminateEventDefinition
            extends  EventDefinition
    {}

    public interface  EscalationEventDefinition
            extends  EventDefinition
    {}


    public interface  CompensateEventDefinition
            extends  EventDefinition
    {}

    public interface  TimerEventDefinition
            extends  EventDefinition
    {}

    public interface  LinkEventDefinition
            extends  EventDefinition
    {}

    public interface  MessageEventDefinition
            extends  EventDefinition
    {}

    public interface  ConditionalEventDefinition
            extends  EventDefinition
    {}

    public interface  SignalEventDefinition
            extends  EventDefinition
    {}

    public interface  Signal
            extends  RootElement
    {}

    public interface  ImplicitThrowEvent
            extends  ThrowEvent
    {}

    public interface  DataState
            extends  BaseElement
    {}

    public interface  ItemAwareElement
            extends  BaseElement
    {}

    public interface  DataAssociation
            extends  BaseElement
    {}

    public interface  DataInput
            extends  ItemAwareElement
    {}

    public interface  DataOutput
            extends  ItemAwareElement
    {}

    public interface  InputSet
            extends  BaseElement
    {}

    public interface  OutputSet
            extends  BaseElement
    {}

    public interface  Property
            extends  ItemAwareElement
    {}

    public interface  DataInputAssociation
            extends  DataAssociation
    {}

    public interface  DataOutputAssociation
            extends  DataAssociation
    {}

    public interface  InputOutputSpecification
            extends  BaseElement
    {}

    public interface  DataObject
            extends  FlowElement,ItemAwareElement
    {}

    public interface  InputOutputBinding
    {}

    public interface  Assignment
            extends  BaseElement
    {}

    public interface  DataStore
            extends  RootElement,ItemAwareElement
    {}

    public interface  DataStoreReference
            extends  ItemAwareElement,FlowElement
    {}

    public interface  DataObjectReference
            extends  ItemAwareElement,FlowElement
    {}

    public interface  ConversationLink
            extends  BaseElement
    {}

    public interface  ConversationAssociation
            extends  BaseElement
    {}

    public interface  CallConversation
            extends  ConversationNode
    {}

    public interface  Conversation
            extends  ConversationNode
    {}

    public interface  SubConversation
            extends  ConversationNode
    {}

    public interface  ConversationNode
            extends  InteractionNode,BaseElement
    {}

    public interface  GlobalConversation
            extends  Collaboration
    {}

    public interface  PartnerEntity
            extends  RootElement
    {}

    public interface  PartnerRole
            extends  RootElement
    {}

    public interface  CorrelationProperty
            extends  RootElement
    {}

    public interface  Error
            extends  RootElement
    {}

    public interface  CorrelationKey
            extends  BaseElement
    {}

    public interface  Expression
            extends  BaseElement
    {}

    public interface  FormalExpression
            extends  Expression
    {}

    public interface  Message
            extends  RootElement
    {}

    public interface  ItemDefinition
            extends  RootElement
    {}

    public interface  FlowElement
            extends  BaseElement
    {}

    public interface  SequenceFlow
            extends  FlowElement
    {}

    public interface  FlowElementsContainer
            extends  BaseElement
    {}

    public interface  CallableElement
            extends  RootElement
    {}

    public interface  FlowNode
            extends  FlowElement
    {}

    public interface  CorrelationPropertyRetrievalExpression
            extends  BaseElement
    {}

    public interface  CorrelationPropertyBinding
            extends  BaseElement
    {}

    public interface  Resource
            extends  RootElement
    {}

    public interface  ResourceParameter
            extends  BaseElement
    {}

    public interface  CorrelationSubscription
            extends  BaseElement
    {}

    public interface  MessageFlow
            extends  BaseElement
    {}

    public interface  MessageFlowAssociation
            extends  BaseElement
    {}

    public interface  InteractionNode
    {}

    public interface  Participant
            extends  InteractionNode,BaseElement
    {}

    public interface  ParticipantAssociation
            extends  BaseElement
    {}

    public interface  ParticipantMultiplicity
            extends  BaseElement
    {}

    public interface  Collaboration
            extends  RootElement
    {}

    public interface  ChoreographyActivity
            extends  FlowNode
    {}

    public interface  CallChoreography
            extends  ChoreographyActivity
    {}

    public interface  SubChoreography
            extends  ChoreographyActivity,FlowElementsContainer
    {}

    public interface  ChoreographyTask
            extends  ChoreographyActivity
    {}

    public interface  Choreography
            extends  Collaboration,FlowElementsContainer
    {}

    public interface  GlobalChoreographyTask
            extends  Choreography
    {}

    public interface  TextAnnotation
            extends  Artifact
    {}

    public interface  Group
            extends  Artifact
    {}

    public interface  Association
            extends  Artifact
    {}

    public interface  Category
            extends  RootElement
    {}

    public interface  Artifact
            extends  BaseElement
    {}

    public interface  CategoryValue
            extends  BaseElement
    {}

    public interface  Activity
            extends  FlowNode
    {}

    public interface  ServiceTask
            extends  Task
    {}

    public interface  SubProcess
            extends  Activity,FlowElementsContainer,InteractionNode
    {}

    public interface  LoopCharacteristics
            extends  BaseElement
    {}

    public interface  MultiInstanceLoopCharacteristics
            extends  LoopCharacteristics
    {}

    public interface  StandardLoopCharacteristics
            extends  LoopCharacteristics
    {}

    public interface  CallActivity
            extends  Activity,InteractionNode
    {}

    public interface  Task
            extends  Activity,InteractionNode
    {}

    public interface  SendTask
            extends  Task
    {}

    public interface  ReceiveTask
            extends  Task
    {}

    public interface  ScriptTask
            extends  Task
    {}

    public interface  BusinessRuleTask
            extends  Task
    {}

    public interface  AdHocSubProcess
            extends  SubProcess
    {}

    public interface  Transaction
            extends  SubProcess
    {}

    public interface  GlobalScriptTask
            extends  GlobalTask
    {}

    public interface  GlobalBusinessRuleTask
            extends  GlobalTask
    {}

    public interface  ComplexBehaviorDefinition
            extends  BaseElement
    {}

    public interface  ResourceRole
            extends  BaseElement
    {}

    public interface  ResourceParameterBinding
            extends  BaseElement
    {}

    public interface  ResourceAssignmentExpression
            extends  BaseElement
    {}

    public interface  Import
    {}

    public interface  Definitions
            extends  BaseElement
    {}

}
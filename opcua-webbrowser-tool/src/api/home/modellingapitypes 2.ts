export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
  /** The `JSON` scalar type represents JSON values as specified by [ECMA-404](http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf). */
  JSON: { input: any; output: any; }
  /** The `JSONObject` scalar type represents JSON objects as specified by [ECMA-404](http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf). */
  JSONObject: { input: any; output: any; }
};

export type Mutation = {
  __typename?: 'Mutation';
  addFBDiagramVariable: UaTopologyInterface;
  addFunctionBlock: UaTopologyObject;
  addInterface: UaTopologyInterface;
  addMethod: UaMethod;
  addObject: UaNode;
  addObjectType: UaType;
  addProcessObject: UaTopologyObject;
  addState: UaState;
  addTransition: UaTransition;
  addUaServer: UaServer;
  addUaService: UaService;
  addVariable: UaVariable;
  deleteNode: Scalars['String']['output'];
  deleteUaServer: Scalars['Int']['output'];
  deleteUaService: Scalars['String']['output'];
  downloadUaEngineeringData: Scalars['Boolean']['output'];
  setAccessLevel: Scalars['String']['output'];
  setActionMethod: Scalars['String']['output'];
  setDescription: Scalars['String']['output'];
  setHistorizing: Scalars['String']['output'];
  setInitialState: Scalars['String']['output'];
  setMethodArguments: Scalars['String']['output'];
  setModellingRule: Scalars['String']['output'];
  setName: Scalars['String']['output'];
  setSubStateMachine: Scalars['String']['output'];
  setUaServer: Scalars['Int']['output'];
  setUaService: Scalars['String']['output'];
  setVariableValues: Array<Maybe<Scalars['String']['output']>>;
  updateLinks: Scalars['Int']['output'];
  updateRuntimeDownloadProgress: Scalars['Int']['output'];
};


export type MutationAddFbDiagramVariableArgs = {
  browseName: Scalars['String']['input'];
  dataType: Scalars['String']['input'];
  display: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationAddFunctionBlockArgs = {
  browseName: Scalars['String']['input'];
  display: Scalars['String']['input'];
  libraryId: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  typeId: Scalars['String']['input'];
};


export type MutationAddInterfaceArgs = {
  browseName: Scalars['String']['input'];
  direction: UaDirection;
  display: Scalars['String']['input'];
  libraryId: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  typeId: Scalars['String']['input'];
};


export type MutationAddMethodArgs = {
  browseName: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationAddObjectArgs = {
  browseName: Scalars['String']['input'];
  libraryId: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  typeId: Scalars['String']['input'];
};


export type MutationAddObjectTypeArgs = {
  browseName: Scalars['String']['input'];
  isAbstract: Scalars['Boolean']['input'];
  name: Scalars['String']['input'];
  parentType: Scalars['String']['input'];
};


export type MutationAddProcessObjectArgs = {
  browseName: Scalars['String']['input'];
  display: Scalars['String']['input'];
  libraryId: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  typeId: Scalars['String']['input'];
};


export type MutationAddStateArgs = {
  browseName: Scalars['String']['input'];
  display: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  stateNumber: Scalars['Int']['input'];
};


export type MutationAddTransitionArgs = {
  fromStateId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  toStateId: Scalars['String']['input'];
  transitionNumber: Scalars['Int']['input'];
};


export type MutationAddUaServerArgs = {
  runningMode: UaServerRunningMode;
  serviceId: Scalars['Int']['input'];
};


export type MutationAddUaServiceArgs = {
  desc: Scalars['String']['input'];
  name: Scalars['String']['input'];
  type: Scalars['String']['input'];
};


export type MutationAddVariableArgs = {
  browseName: Scalars['String']['input'];
  dataType: Scalars['String']['input'];
  libraryId: Scalars['String']['input'];
  name: Scalars['String']['input'];
  parentId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
  typeId: Scalars['String']['input'];
};


export type MutationDeleteNodeArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationDeleteUaServerArgs = {
  serverId: Scalars['Int']['input'];
};


export type MutationDeleteUaServiceArgs = {
  serviceId: Scalars['Int']['input'];
};


export type MutationDownloadUaEngineeringDataArgs = {
  serverId: Scalars['Int']['input'];
};


export type MutationSetAccessLevelArgs = {
  accessLevel: UaAccessLevel;
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetActionMethodArgs = {
  actionId: Scalars['String']['input'];
  methodId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetDescriptionArgs = {
  desc: Scalars['String']['input'];
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetHistorizingArgs = {
  historizing: Scalars['Boolean']['input'];
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetInitialStateArgs = {
  sid: Scalars['Int']['input'];
  stateId: Scalars['String']['input'];
};


export type MutationSetMethodArgumentsArgs = {
  argumentMappings: UaMethodMappingInput;
  inputArguments: Array<InputMaybe<UaMethodArgumentInput>>;
  methodId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetModellingRuleArgs = {
  enabled: Scalars['Boolean']['input'];
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetNameArgs = {
  browseName: Scalars['String']['input'];
  name: Scalars['String']['input'];
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationSetSubStateMachineArgs = {
  sid: Scalars['Int']['input'];
  stateId: Scalars['String']['input'];
};


export type MutationSetUaServerArgs = {
  configure: UaServerConfigureInput;
  serverId: Scalars['Int']['input'];
};


export type MutationSetUaServiceArgs = {
  desc?: InputMaybe<Scalars['String']['input']>;
  name?: InputMaybe<Scalars['String']['input']>;
  serviceId: Scalars['Int']['input'];
};


export type MutationSetVariableValuesArgs = {
  sid: Scalars['Int']['input'];
  values: Array<UaVariableValue>;
};


export type MutationUpdateLinksArgs = {
  linksToAdd: Array<InputMaybe<UaTopologyLinkInput>>;
  linksToDelete: Array<InputMaybe<UaTopologyLinkInput>>;
  reference: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type MutationUpdateRuntimeDownloadProgressArgs = {
  complete: Scalars['Boolean']['input'];
  serverId: Scalars['Int']['input'];
};

export type Query = {
  __typename?: 'Query';
  browseNode: Array<UaNode>;
  findNodeByPath: Scalars['String']['output'];
  getAttributesAndProperties: UaAttributesAndProperties;
  getDataTypes: Array<UaDataType>;
  getFunctionBlockDiagram: UaTopology;
  getMembers: UaMembers;
  getObjectTypeConfigures: Array<UaObjectTypeConfigure>;
  getObjectTypeLibs: Array<UaObjectTypeLib>;
  getObjectTypeLibsFromTopology: Array<UaObjectTypeLib>;
  getPath: Array<Array<Maybe<Scalars['String']['output']>>>;
  getProcessTopology: UaTopology;
  getRootNode: UaNode;
  getRuntimeNamespaces: Array<UaNamespaceForRuntime>;
  getRuntimeNodesAndReferences: UaNodesAndReferencesForRuntime;
  getRuntimeNodesData: Array<Maybe<UaNodeDataForRuntime>>;
  getRuntimeServices: Array<UaServiceForRuntime>;
  getStateAction: Scalars['String']['output'];
  getStateMachine: UaStateMachine;
  getSubType: Array<UaType>;
  getTemplateLibs: Array<UaTemplateLib>;
  getTransitionGuard: Scalars['String']['output'];
  getUaServerState: UaServerState;
  getUaServers: Array<UaServer>;
  getUaServiceTypes: Array<UaServiceType>;
  getUaServices: Array<UaService>;
  getVariableTypeLibs: Array<UaVariableTypeLib>;
  readNodes: Array<UaAttributeValues>;
};


export type QueryBrowseNodeArgs = {
  isForward: Scalars['Boolean']['input'];
  limit?: InputMaybe<Scalars['Int']['input']>;
  nodeClassToReturn: Array<InputMaybe<UaNodeClass>>;
  nodeId: Scalars['String']['input'];
  page?: InputMaybe<Scalars['Int']['input']>;
  sid: Scalars['Int']['input'];
};


export type QueryFindNodeByPathArgs = {
  nodeId: Scalars['String']['input'];
  path: Array<InputMaybe<Scalars['String']['input']>>;
  sid: Scalars['Int']['input'];
};


export type QueryGetAttributesAndPropertiesArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetDataTypesArgs = {
  limit: Scalars['Int']['input'];
  page: Scalars['Int']['input'];
};


export type QueryGetFunctionBlockDiagramArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetMembersArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetObjectTypeConfiguresArgs = {
  nodeIds: Array<Scalars['String']['input']>;
};


export type QueryGetObjectTypeLibsArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetObjectTypeLibsFromTopologyArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetPathArgs = {
  nodeIds: Array<InputMaybe<Scalars['String']['input']>>;
  sid: Scalars['Int']['input'];
};


export type QueryGetProcessTopologyArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetRootNodeArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetRuntimeNamespacesArgs = {
  serviceId: Scalars['Int']['input'];
};


export type QueryGetRuntimeNodesAndReferencesArgs = {
  nsIndex: Scalars['Int']['input'];
  serviceId: Scalars['Int']['input'];
};


export type QueryGetRuntimeNodesDataArgs = {
  nodeIds: Array<InputMaybe<Scalars['String']['input']>>;
  serviceId: Scalars['Int']['input'];
};


export type QueryGetRuntimeServicesArgs = {
  serviceId: Scalars['Int']['input'];
};


export type QueryGetStateActionArgs = {
  sid: Scalars['Int']['input'];
  stateId: Scalars['String']['input'];
  stateMachineId: Scalars['String']['input'];
};


export type QueryGetStateMachineArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryGetSubTypeArgs = {
  nodeId: Scalars['String']['input'];
};


export type QueryGetTemplateLibsArgs = {
  serviceTypeId: Scalars['String']['input'];
};


export type QueryGetTransitionGuardArgs = {
  sid: Scalars['Int']['input'];
  transitionId: Scalars['String']['input'];
};


export type QueryGetUaServerStateArgs = {
  serverId: Scalars['Int']['input'];
};


export type QueryGetUaServersArgs = {
  serviceId: Scalars['Int']['input'];
};


export type QueryGetUaServicesArgs = {
  limit: Scalars['Int']['input'];
  page: Scalars['Int']['input'];
  type: Scalars['String']['input'];
};


export type QueryGetVariableTypeLibsArgs = {
  nodeId: Scalars['String']['input'];
  sid: Scalars['Int']['input'];
};


export type QueryReadNodesArgs = {
  nodeIds: Array<InputMaybe<Scalars['String']['input']>>;
  sid: Scalars['Int']['input'];
};

export enum UaAccessLevel {
  Na = 'NA',
  Ro = 'RO',
  Wo = 'WO'
}

export type UaAttribute = {
  __typename?: 'UaAttribute';
  desc?: Maybe<Scalars['String']['output']>;
  nodeClass?: Maybe<UaNodeClass>;
  nodeId?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
  typeName?: Maybe<Scalars['String']['output']>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export type UaAttributeValues = {
  __typename?: 'UaAttributeValues';
  dataType?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  nodeClass?: Maybe<UaNodeClass>;
  nodeId?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
  value?: Maybe<Scalars['JSON']['output']>;
  valueRank?: Maybe<Scalars['Int']['output']>;
};

export type UaAttributesAndProperties = {
  __typename?: 'UaAttributesAndProperties';
  attribute?: Maybe<UaAttribute>;
  properties?: Maybe<Array<Maybe<UaProperty>>>;
  subPropGroups?: Maybe<Array<Maybe<UaPropertyTypeGroup>>>;
};

export type UaDataType = {
  __typename?: 'UaDataType';
  browseName?: Maybe<Scalars['String']['output']>;
  dataType?: Maybe<UaValueType>;
  enumStrings?: Maybe<Array<Maybe<Scalars['String']['output']>>>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  specific?: Maybe<Scalars['Boolean']['output']>;
};

export enum UaDirection {
  In = 'In',
  InOut = 'InOut',
  None = 'None',
  Out = 'Out'
}

export enum UaEventNotifier {
  Event = 'Event',
  EventHistoryRead = 'EventHistoryRead',
  EventHistoryWrite = 'EventHistoryWrite',
  None = 'None'
}

export type UaMembers = {
  __typename?: 'UaMembers';
  methods?: Maybe<Array<Maybe<UaMethod>>>;
  variables?: Maybe<Array<Maybe<UaVariable>>>;
};

export type UaMethod = {
  __typename?: 'UaMethod';
  argumentMappings?: Maybe<UaMethodMappingStructure>;
  browseName?: Maybe<Scalars['String']['output']>;
  enabled?: Maybe<Scalars['Boolean']['output']>;
  executable?: Maybe<Scalars['Boolean']['output']>;
  inputArguments?: Maybe<Array<Maybe<UaMethodArgument>>>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  outputArguments?: Maybe<Array<Maybe<UaMethodArgument>>>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export type UaMethodArgument = {
  __typename?: 'UaMethodArgument';
  dataType?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  valueRank?: Maybe<Scalars['Int']['output']>;
};

export type UaMethodArgumentInput = {
  dataType?: InputMaybe<Scalars['String']['input']>;
  name?: InputMaybe<Scalars['String']['input']>;
  valueRank?: InputMaybe<Scalars['Int']['input']>;
};

export type UaMethodMappingInput = {
  inputArgumentTargetIds?: InputMaybe<Array<InputMaybe<Scalars['String']['input']>>>;
  methodTriggerId?: InputMaybe<Scalars['String']['input']>;
  triggerType?: InputMaybe<UaMethodTriggerType>;
};

export type UaMethodMappingStructure = {
  __typename?: 'UaMethodMappingStructure';
  inputArgumentTargetIds?: Maybe<Array<Maybe<Scalars['String']['output']>>>;
  methodTriggerId?: Maybe<Scalars['String']['output']>;
  triggerType?: Maybe<UaMethodTriggerType>;
};

export enum UaMethodTriggerType {
  None = 'None',
  Off = 'Off',
  OffPulse = 'OffPulse',
  On = 'On',
  OnPulse = 'OnPulse'
}

export type UaNamespaceForRuntime = {
  __typename?: 'UaNamespaceForRuntime';
  isInstanceNs?: Maybe<Scalars['Boolean']['output']>;
  nsIndex?: Maybe<Scalars['Int']['output']>;
  uri?: Maybe<Scalars['String']['output']>;
  version?: Maybe<Scalars['Int']['output']>;
};

export type UaNode = {
  __typename?: 'UaNode';
  browseName?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  nodeClass?: Maybe<UaNodeClass>;
  nodeId?: Maybe<Scalars['String']['output']>;
  orderNum?: Maybe<Scalars['Int']['output']>;
  reference?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export enum UaNodeClass {
  DataType = 'DataType',
  Method = 'Method',
  Object = 'Object',
  ObjectType = 'ObjectType',
  ReferenceType = 'ReferenceType',
  Unspecified = 'Unspecified',
  Variable = 'Variable',
  VariableType = 'VariableType',
  View = 'View'
}

export type UaNodeDataForRuntime = {
  __typename?: 'UaNodeDataForRuntime';
  ab?: Maybe<Scalars['JSONObject']['output']>;
  bn?: Maybe<Scalars['String']['output']>;
  dn?: Maybe<Scalars['String']['output']>;
  nc?: Maybe<Scalars['Int']['output']>;
  ni?: Maybe<Scalars['String']['output']>;
  pi?: Maybe<Scalars['String']['output']>;
  rp?: Maybe<Scalars['String']['output']>;
  vs?: Maybe<Scalars['Int']['output']>;
};

export type UaNodeInfoForRuntime = {
  __typename?: 'UaNodeInfoForRuntime';
  id?: Maybe<Scalars['String']['output']>;
  ver?: Maybe<Scalars['Int']['output']>;
};

export type UaNodeSpecificAttribute = {
  __typename?: 'UaNodeSpecificAttribute';
  notifier?: Maybe<Array<Maybe<UaEventNotifier>>>;
};

export type UaNodesAndReferencesForRuntime = {
  __typename?: 'UaNodesAndReferencesForRuntime';
  nodes?: Maybe<Array<Maybe<UaNodeInfoForRuntime>>>;
  references?: Maybe<Array<Maybe<UaReferenceForRuntime>>>;
};

export type UaObjectTypeConfigure = {
  __typename?: 'UaObjectTypeConfigure';
  configureMode?: Maybe<Scalars['String']['output']>;
  hasMethodLibrary?: Maybe<Scalars['Boolean']['output']>;
  hasObjectTypeLibrary?: Maybe<Scalars['Boolean']['output']>;
  hasVariableTypeLibrary?: Maybe<Scalars['Boolean']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
};

export type UaObjectTypeLib = {
  __typename?: 'UaObjectTypeLib';
  browseName?: Maybe<Scalars['String']['output']>;
  isAbstract?: Maybe<Scalars['Boolean']['output']>;
  isAddIn?: Maybe<Scalars['Boolean']['output']>;
  libraryId?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
};

export type UaProperty = {
  __typename?: 'UaProperty';
  browseName?: Maybe<Scalars['String']['output']>;
  dataType?: Maybe<Scalars['String']['output']>;
  enabled?: Maybe<Scalars['Boolean']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  value?: Maybe<Scalars['JSON']['output']>;
  valueRank?: Maybe<Scalars['Int']['output']>;
};

export type UaPropertyTypeGroup = {
  __typename?: 'UaPropertyTypeGroup';
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
};

export type UaReferenceForRuntime = {
  __typename?: 'UaReferenceForRuntime';
  r?: Maybe<Scalars['String']['output']>;
  s?: Maybe<Scalars['String']['output']>;
  t?: Maybe<Scalars['String']['output']>;
};

export type UaServer = {
  __typename?: 'UaServer';
  configure?: Maybe<UaServerConfigure>;
  id?: Maybe<Scalars['Int']['output']>;
  runningMode?: Maybe<UaServerRunningMode>;
};

export type UaServerConfigure = {
  __typename?: 'UaServerConfigure';
  netAddr?: Maybe<Scalars['String']['output']>;
  port?: Maybe<Scalars['Int']['output']>;
};

export type UaServerConfigureInput = {
  netAddr?: InputMaybe<Scalars['String']['input']>;
  port?: InputMaybe<Scalars['Int']['input']>;
};

export type UaServerForRuntime = {
  __typename?: 'UaServerForRuntime';
  id?: Maybe<Scalars['Int']['output']>;
  runtimeMode?: Maybe<Scalars['Boolean']['output']>;
  url?: Maybe<Scalars['String']['output']>;
};

export enum UaServerRunningMode {
  Engineering = 'Engineering',
  Runtime = 'Runtime'
}

export enum UaServerState {
  DownloadComplete = 'DownloadComplete',
  Downloading = 'Downloading',
  Shutdown = 'Shutdown',
  Started = 'Started'
}

export type UaService = {
  __typename?: 'UaService';
  desc?: Maybe<Scalars['String']['output']>;
  id?: Maybe<Scalars['Int']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  type?: Maybe<Scalars['String']['output']>;
};

export type UaServiceForRuntime = {
  __typename?: 'UaServiceForRuntime';
  id?: Maybe<Scalars['Int']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  servers?: Maybe<Array<Maybe<UaServerForRuntime>>>;
};

export type UaServiceType = {
  __typename?: 'UaServiceType';
  configurable?: Maybe<Scalars['Boolean']['output']>;
  desc?: Maybe<Scalars['String']['output']>;
  id?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
};

export type UaState = {
  __typename?: 'UaState';
  configureOption?: Maybe<Array<Maybe<UaStateMachineConfigureOption>>>;
  display?: Maybe<Scalars['String']['output']>;
  groupId?: Maybe<Scalars['String']['output']>;
  initialState?: Maybe<Scalars['Boolean']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  parentgroupId?: Maybe<Scalars['String']['output']>;
  stateNumber?: Maybe<Scalars['Int']['output']>;
};

export type UaStateMachine = {
  __typename?: 'UaStateMachine';
  states?: Maybe<Array<Maybe<UaState>>>;
  transitions?: Maybe<Array<Maybe<UaTransition>>>;
};

export enum UaStateMachineConfigureOption {
  Configure = 'Configure',
  Link = 'Link',
  Remove = 'Remove'
}

export type UaTemplateLib = {
  __typename?: 'UaTemplateLib';
  name?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
};

export type UaTopology = {
  __typename?: 'UaTopology';
  interfaces?: Maybe<Array<Maybe<UaTopologyInterface>>>;
  links?: Maybe<Array<Maybe<UaTopologyLink>>>;
  objects?: Maybe<Array<Maybe<UaTopologyObject>>>;
};

export type UaTopologyInterface = {
  __typename?: 'UaTopologyInterface';
  direction?: Maybe<UaDirection>;
  display?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export type UaTopologyLink = {
  __typename?: 'UaTopologyLink';
  sourceId?: Maybe<Scalars['String']['output']>;
  targetId?: Maybe<Scalars['String']['output']>;
};

export type UaTopologyLinkInput = {
  sourceId?: InputMaybe<Scalars['String']['input']>;
  targetId?: InputMaybe<Scalars['String']['input']>;
};

export type UaTopologyObject = {
  __typename?: 'UaTopologyObject';
  display?: Maybe<Scalars['String']['output']>;
  interfaces?: Maybe<Array<Maybe<UaTopologyInterface>>>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export type UaTransition = {
  __typename?: 'UaTransition';
  fromState?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  removable?: Maybe<Scalars['Boolean']['output']>;
  toState?: Maybe<Scalars['String']['output']>;
  transitionNumber?: Maybe<Scalars['Int']['output']>;
};

export type UaType = {
  __typename?: 'UaType';
  browseName?: Maybe<Scalars['String']['output']>;
  isAbstract?: Maybe<Scalars['Boolean']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export enum UaValueType {
  Boolean = 'Boolean',
  Double = 'Double',
  Enumeration = 'Enumeration',
  Integer = 'Integer',
  NodeId = 'NodeId',
  None = 'None',
  String = 'String',
  Structure = 'Structure',
  Time = 'Time',
  UInteger = 'UInteger'
}

export type UaVariable = {
  __typename?: 'UaVariable';
  accessLevel?: Maybe<UaAccessLevel>;
  browseName?: Maybe<Scalars['String']['output']>;
  dataType?: Maybe<Scalars['String']['output']>;
  enabled?: Maybe<Scalars['Boolean']['output']>;
  historizing?: Maybe<Scalars['Boolean']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  nodeId?: Maybe<Scalars['String']['output']>;
  value?: Maybe<Scalars['JSON']['output']>;
  valueRank?: Maybe<Scalars['Int']['output']>;
  writeMask?: Maybe<Scalars['Int']['output']>;
};

export type UaVariableTypeLib = {
  __typename?: 'UaVariableTypeLib';
  browseName?: Maybe<Scalars['String']['output']>;
  dataType?: Maybe<Scalars['String']['output']>;
  libraryId?: Maybe<Scalars['String']['output']>;
  name?: Maybe<Scalars['String']['output']>;
  typeId?: Maybe<Scalars['String']['output']>;
};

export type UaVariableValue = {
  nodeId?: InputMaybe<Scalars['String']['input']>;
  value?: InputMaybe<Scalars['JSON']['input']>;
};

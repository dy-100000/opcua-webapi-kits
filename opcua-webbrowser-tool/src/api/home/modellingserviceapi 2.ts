import { ApolloClient, InMemoryCache, gql } from '@apollo/client';
import * as ServiceTypes from "./modellingapitypes";

export class ModellingServiceApi
{
    constructor(url: string)
    {
      this.url = url;    
    }

    public async getUaServiceTypes(): Promise<ServiceTypes.UaServiceType[]>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache()
          });

      let request = gql`
        query GetUaServiceTypes {
            getUaServiceTypes {
              id
              name
              configurable
            }
          }
        `;

      let { data, error } = await client.query({
        query: request });

      if (error) throw error;

      return data.getUaServiceTypes;
    }

    public async getUaServices(args : ServiceTypes.QueryGetUaServicesArgs) : Promise<ServiceTypes.UaService[]>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache(),
          });

      let request = gql`
      query GetUaServices($page: Int!, $limit: Int!, $type: String!) {
        getUaServices(page: $page, limit: $limit, type: $type) {
          id
          name
          type
          description
        }
      }
            `;

      let { data, error } = await client.query({
                query: request,
                variables: args
              });

      if (error) throw error;
      
      return data.getUaServices;
    }

    public async addUaService(args : ServiceTypes.MutationAddUaServiceArgs) : Promise<ServiceTypes.UaService>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddUaService($name: String!, $type: String!, $description: String!) {
        addUaService(name: $name, type: $type, description: $description) {
          id
          name
          type
          description
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaService;
    }

    public async setUaService(args : ServiceTypes.MutationSetUaServiceArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetUaService($id: Int!, $name: String, $description: String) {
        setUaService(id: $id, name: $name, description: $description)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setUaService;
    }   

    public async deleteUaService(args : ServiceTypes.MutationDeleteUaServiceArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation DeleteUaService($id: Int!) {
        deleteUaService(id: $id)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.deleteUaService;
    }     

    public async getUaServers(args : ServiceTypes.QueryGetUaServersArgs) : Promise<ServiceTypes.UaServer[]>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache(),
          });

      let request = gql`
      query GetUaServers($serviceId: Int!) {
        getUaServers(serviceId: $serviceId) {
          id
          runningMode
          configure {
            netAddr
            port
          }
        }
      }
            `;

      let { data, error } = await client.query({
                query: request,
                variables: args
              });

      if (error) throw error;
      
      return data.getUaServers;
    }

    public async addUaServer(args : ServiceTypes.MutationAddUaServerArgs) : Promise<ServiceTypes.UaServer>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddUaServer($serviceId: Int!, $runningMode: UaServerRunningMode!) {
        addUaServer(serviceId: $serviceId, runningMode: $runningMode) {
          id
          runningMode
          configure {
            netAddr
            port
          }
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaServer;
    }

    public async setUaServer(args : ServiceTypes.MutationSetUaServerArgs) : Promise<number>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetUaServer($serverId: Int!, $configure: UaServerConfigureInput!) {
        setUaServer(serverId: $serverId, configure: $configure)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setUaService;
    }   

    public async deleteUaServer(args : ServiceTypes.MutationDeleteUaServerArgs) : Promise<number>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation DeleteUaServer($serverId: Int!) {
        deleteUaServer(serverId: $serverId)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.deleteUaServer;
    }   

    public async downloadUaEngineeringData(args : ServiceTypes.MutationDownloadUaEngineeringDataArgs) : Promise<number>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation DownloadUaEngineeringData($serverId: Int!) {
        downloadUaEngineeringData(serverId: $serverId)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.downloadUaEngineeringData;
    }   

    public async getUaServerState(args : ServiceTypes.QueryGetUaServerStateArgs) : Promise<ServiceTypes.UaServerState>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache(),
          });

      let request = gql`
      query GetUaServerState($serverId: Int!) {
        getUaServerState(serverId: $serverId)
      }`;

      let { data, error } = await client.query({
                query: request,
                variables: args
              });

      if (error) throw error;
      
      return data.getUaServerState;
    }

    public async browseNode(args : ServiceTypes.QueryBrowseNodeArgs) : Promise<ServiceTypes.UaNode[]>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache(),
          });

      let request = gql`
        query BrowseNode($sid: Int!, $nodeId: String!, $nodeClassToReturn: [UaNodeClass]!, $isForward: Boolean!, $limit: Int, $page: Int) {
            browseNode(sid: $sid, nodeId: $nodeId, nodeClassToReturn: $nodeClassToReturn, isForward: $isForward, limit: $limit, page: $page) {
              nodeId
              nodeClass
              browseName
              name
              writeMask
              typeId
              orderNum
            }
          }
            `;

      let { data, error } = await client.query({
                query: request,
                variables: args
              });

      if (error) throw error;
      
      return data.browseUaNode;
    }

    public async findNodeByPath(args : ServiceTypes.QueryFindNodeByPathArgs) : Promise<string>
    {
      let client = new ApolloClient({
          uri: this.url,
          cache: new InMemoryCache()
        });

      let request = gql`
      query FindNodeByPath($sid: Int!, $nodeId: String!, $path: [String]!) {
        findNodeByPath(sid: $sid, nodeId: $nodeId, path: $path)
      }
          `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.findUaNodeByPath;    
    }

    public async getAttributesAndProperties(args : ServiceTypes.QueryGetAttributesAndPropertiesArgs) : Promise<ServiceTypes.UaAttributesAndProperties>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetAttributesAndProperties($sid: Int!, $nodeId: String!) {
        getAttributesAndProperties(sid: $sid, nodeId: $nodeId) {
          attribute { nodeId nodeClass writeMask desc typeId typeName }
          properties { nodeId browseName name value dataType valueRank enabled }
          subPropGroups { nodeId name }
          specific { notifier }
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getAttributesAndProperties; 
    }

    public async getMembers(args : ServiceTypes.QueryGetMembersArgs) : Promise<ServiceTypes.UaMembers>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetMembers($sid: Int!, $nodeId: String!) {
        getMembers(sid: $sid, nodeId: $nodeId) {
          variables { nodeId browseName writeMask name value dataType valueRank accessLevel historizing enabled }
          methods { nodeId browseName name writeMask executable enabled
            inputArguments { name dataType valueRank }
            outputArguments { name dataType valueRank }
            argumentMappings { methodTriggerId triggerType inputArgumentTargetIds }
          }
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getMembers; 
    }

    public async getPath(args : ServiceTypes.QueryGetPathArgs) : Promise<string[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetPath($sid: Int!, $nodeIds: [String]!, $deep: Int!, $spliter: String!) {
        getPath(sid: $sid, nodeIds: $nodeIds, deep: $deep, spliter: $spliter)
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getPath;     
    }

    public async getRootNode(args : ServiceTypes.QueryGetRootNodeArgs)
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetRootNode($sid: Int!, $nodeId: String!) {
        getRootNode(sid: $sid, nodeId: $nodeId) {
          nodeId
          nodeClass
          browseName
          name
          writeMask
          typeId
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getRootNode;        
    }

    public async addObject(args : ServiceTypes.MutationAddObjectArgs) : Promise<ServiceTypes.UaNode>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddObject($sid: Int!, $browseName: String!, $name: String!, $typeId: String!, $libraryId: String!, $parentId: String!) {
        addObject(sid: $sid, browseName: $browseName, name: $name, typeId: $typeId, libraryId: $libraryId, parentId: $parentId) {
          nodeId
          nodeClass
          browseName
          name
          writeMask
          typeId
          orderNum
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaObject;
    }

    public async addVariable(args : ServiceTypes.MutationAddVariableArgs) : Promise<ServiceTypes.UaVariable>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddVariable($sid: Int!, $browseName: String!, $name: String!, $typeId: String!, $libraryId: String!, $dataType: String!, $parentId: String!) {
        addVariable(sid: $sid, browseName: $browseName, name: $name, typeId: $typeId, libraryId: $libraryId, dataType: $dataType, parentId: $parentId) {
          nodeId
          browseName
          writeMask
          name
          value
          dataType
          valueRank
          accessLevel
          historizing
          enabled
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaVariable;
    }   

    public async addMethod(args : ServiceTypes.MutationAddMethodArgs) : Promise<ServiceTypes.UaMethod>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddMethod($sid: Int!, $browseName: String!, $name: String!, $parentId: String!) {
        addMethod(sid: $sid, browseName: $browseName, name: $name, parentId: $parentId) {
          nodeId
          browseName
          name
          writeMask
          executable
          enabled
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaMethod;
    }     

    public async deleteNode(args : ServiceTypes.MutationDeleteNodeArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation DeleteNode($sid: Int!, $nodeId: String!) {
        deleteNode(sid: $sid, nodeId: $nodeId)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.deleteUaNode;
    } 

    public async setModellingRule(args : ServiceTypes.MutationSetModellingRuleArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetModellingRule($sid: Int!, $nodeId: String!, $enabled: Boolean!) {
        setModellingRule(sid: $sid, nodeId: $nodeId, enabled: $enabled)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setModellingRule;
    } 

    public async setName(args : ServiceTypes.MutationSetNameArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetName($sid: Int!, $nodeId: String!, $browseName: String!, $name: String!) {
        setName(sid: $sid, nodeId: $nodeId, browseName: $browseName, name: $name)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setName;
    }

    public async setDescription(args : ServiceTypes.MutationSetDescriptionArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetDescription($sid: Int!, $nodeId: String!, $desc: String!) {
        setDescription(sid: $sid, nodeId: $nodeId, desc: $desc)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setDescription;
    } 

    public async setVariableValues(args : ServiceTypes.MutationSetVariableValuesArgs) : Promise<string[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetVariableValues($sid: Int!, $values: [UaVariableValue!]!) {
        setVariableValues(sid: $sid, values: $values)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setUaVariableValues;
    }

    public async setAccessLevel(args : ServiceTypes.MutationSetAccessLevelArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetAccessLevel($sid: Int!, $nodeId: String!, $accessLevel: UaAccessLevel!) {
        setAccessLevel(sid: $sid, nodeId: $nodeId, accessLevel: $accessLevel)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setAccessLevel;
    }

    public async setHistorizing(args : ServiceTypes.MutationSetHistorizingArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetHistorizing($sid: Int!, $nodeId: String!, $historizing: Boolean!) {
        setHistorizing(sid: $sid, nodeId: $nodeId, historizing: $historizing)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setHistorizing;
    } 

    public async setMethodArguments(args : ServiceTypes.MutationSetMethodArgumentsArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetMethodArguments($sid: Int!, $methodId: String!, $inputArguments: [UaMethodArgumentInput]!, $argumentMappings: UaMethodMappingInput!) {
        setMethodArguments(sid: $sid, methodId: $methodId, inputArguments: $inputArguments, argumentMappings: $argumentMappings)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setUaMethodArguments;
    } 

    public async getDataTypes(args : ServiceTypes.QueryGetDataTypesArgs) : Promise<ServiceTypes.UaDataType[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetDataTypes($limit: Int!, $page: Int!) {
        getDataTypes(limit: $limit, page: $page) {
          nodeId
          browseName
          name
          dataType
          specific
          enumStrings
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getUaDataTypes;
    }

    public async getObjectTypeConfigures(args : ServiceTypes.QueryGetObjectTypeConfiguresArgs) : Promise<ServiceTypes.UaObjectTypeConfigure[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetObjectTypeConfigures($nodeIds: [String!]!) {
        getObjectTypeConfigures(nodeIds: $nodeIds) {
          nodeId
          hasObjectTypeLibrary
          hasVariableTypeLibrary
          hasMethodLibrary
          configureMode
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getUaObjectTypeConfigures;
    }

    public async getObjectTypeLibs(args : ServiceTypes.QueryGetObjectTypeLibsArgs) : Promise<ServiceTypes.UaObjectTypeLib[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetObjectTypeLibs($sid: Int!, $nodeId: String!) {
        getObjectTypeLibs(sid: $sid, nodeId: $nodeId) {
          libraryId
          typeId
          browseName
          name
          isAbstract
          isAddIn
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getUaObjectTypeLibs;      
    }

    public async getObjectTypeLibsFromTopology(args : ServiceTypes.QueryGetObjectTypeLibsFromTopologyArgs) : Promise<ServiceTypes.UaObjectTypeLib[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetObjectTypeLibsFromTopology($sid: Int!, $nodeId: String!) {
        getObjectTypeLibsFromTopology(sid: $sid, nodeId: $nodeId) {
          libraryId
          typeId
          browseName
          name
          isAbstract
          isAddIn
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getObjectTypeLibsFromTopology;      
    }


    public async getVariableTypeLibs(args : ServiceTypes.QueryGetVariableTypeLibsArgs) : Promise<ServiceTypes.UaVariableTypeLib[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetVariableTypeLibs($sid: Int!, $nodeId: String!) {
        getVariableTypeLibs(sid: $sid, nodeId: $nodeId) {
          libraryId
          typeId
          browseName
          name
          dataType
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getUaVariableTypeLibs; 
    }

    public async readNodes(args : ServiceTypes.QueryReadNodesArgs) : Promise<ServiceTypes.UaAttributeValues[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query ReadNodes($sid: Int!, $nodeIds: [String]!) {
        readNodes(sid: $sid, nodeIds: $nodeIds) {
          nodeId
          name
          nodeClass
          typeId
          dataType
          valueRank
          value
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.readNodes; 
    } 

    public async getSubType(args : ServiceTypes.QueryGetSubTypeArgs) : Promise<ServiceTypes.UaType[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetSubType($nodeId: String!) {
        getSubType(nodeId: $nodeId) {
          typeId
          browseName
          name
          isAbstract
          writeMask
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getUaSubType;
    }  

    public async addObjectType(args : ServiceTypes.MutationAddObjectTypeArgs) : Promise<ServiceTypes.UaType>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddObjectType($browseName: String!, $name: String!, $parentType: String!, $isAbstract: Boolean!) {
        addObjectType(browseName: $browseName, name: $name, parentType: $parentType, isAbstract: $isAbstract) {
          typeId
          browseName
          name
          isAbstract
          writeMask
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaObjectType;
    }

    public async getTemplateLibs(args : ServiceTypes.QueryGetTemplateLibsArgs) : Promise<ServiceTypes.UaTemplateLib[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetTemplateLibs($serviceTypeId: String!) {
        getTemplateLibs(serviceTypeId: $serviceTypeId) {
          typeId
          name
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getUaTemplateLibs;
    }

    public async getStateMachine(args : ServiceTypes.QueryGetStateMachineArgs) : Promise<ServiceTypes.UaStateMachine>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetStateMachine($sid: Int!, $nodeId: String!) {
        getStateMachine(sid: $sid, nodeId: $nodeId) {
          states {
            nodeId
            name
            configureOption
            stateNumber
            initialState
            groupId
            parentgroupId
            position
          }
          transitions {
            nodeId
            removable
            transitionNumber
            fromState
            toState
          }
        }
      }
        `;

      let { data, error } = await client.query({
          query: request,
          variables: args
        });

      if (error) throw error;

      return data.getUaStateMachine;
    }

    public async getStateAction(args : ServiceTypes.QueryGetStateActionArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetStateAction($sid: Int!, $stateId: String!, $stateMachineId: String!) {
        getStateAction(sid: $sid, stateId: $stateId, stateMachineId: $stateMachineId)
      }
        `;

      let { data, error } = await client.query({
          query: request,
          variables: args
        });

      if (error) throw error;

      return data.getUaStateAction;
    }

    public async getTransitionGuard(args : ServiceTypes.QueryGetTransitionGuardArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetTransitionGuard($sid: Int!, $transitionId: String!) {
        getTransitionGuard(sid: $sid, transitionId: $transitionId)
      }
        `;

      let { data, error } = await client.query({
          query: request,
          variables: args
        });

      if (error) throw error;

      return data.getUaTransitionGuard;
    }

    public async setSubStateMachine(args : ServiceTypes.MutationSetSubStateMachineArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetSubStateMachine($sid: Int!, $stateId: String!) {
        setSubStateMachine(sid: $sid, stateId: $stateId)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setUaSubStateMachine;      
    }

    public async addState(args : ServiceTypes.MutationAddStateArgs) : Promise<ServiceTypes.UaState>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddState($sid: Int!, $browseName: String!, $name: String!, $parentId: String!, $stateNumber: Int!, $position: String!) {
        addState(sid: $sid, browseName: $browseName, name: $name, parentId: $parentId, stateNumber: $stateNumber, position: $position) {
          nodeId
          name
          configureOption
          stateNumber
          initialState
          groupId
          parentgroupId
          position
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaState;   
    }

    public async setInitialState(args : ServiceTypes.MutationSetInitialStateArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetInitialState($sid: Int!, $stateId: String!) {
        setInitialState(sid: $sid, stateId: $stateId)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setInitialState;   
    }

    public async addTransition(args : ServiceTypes.MutationAddTransitionArgs) : Promise<ServiceTypes.UaTransition>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddTransition($sid: Int!, $fromStateId: String!, $toStateId: String!, $transitionNumber: Int!) {
        addTransition(sid: $sid, fromStateId: $fromStateId, toStateId: $toStateId, transitionNumber: $transitionNumber) {
          nodeId
          removable
          transitionNumber
          fromState
          toState
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addUaTransition;   
    }

    public async setActionMethod(args : ServiceTypes.MutationSetActionMethodArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetActionMethod($sid: Int!, $actionId: String!, $methodId: String!) {
        setActionMethod(sid: $sid, actionId: $actionId, methodId: $methodId)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setActionMethod;
    }

    public async getProcessTopology(args : ServiceTypes.QueryGetProcessTopologyArgs) : Promise<ServiceTypes.UaTopology>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetProcessTopology($sid: Int!, $nodeId: String!) {
        getProcessTopology(sid: $sid, nodeId: $nodeId) {
          interfaces {
            nodeId
            name
            typeId
            writeMask
            direction
            display
          }
          objects {
            nodeId
            name
            typeId
            writeMask
            display
            interfaces {
              nodeId
              name
              typeId
              writeMask
              direction
              display
            }
          }
          links {
            sourceId
            targetId
          }
        }
      }
        `;

      let {data} = await client.query({
              query: request,
              variables: args
            });

      return data.getProcessTopology;      
    }

    public async addProcessObject(args : ServiceTypes.MutationAddProcessObjectArgs) : Promise<ServiceTypes.UaTopologyObject>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddProcessObject($sid: Int!, $browseName: String!, $name: String!, $typeId: String!, $libraryId: String!, $parentId: String!, $display: String!) {
        addProcessObject(sid: $sid, browseName: $browseName, name: $name, typeId: $typeId, libraryId: $libraryId, parentId: $parentId, display: $display) {
          nodeId
          name
          typeId
          writeMask
          display
          interfaces {
            nodeId
            name
            typeId
            writeMask
            direction
            display
          }
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addProcessObject;
    }

    public async addInterface(args : ServiceTypes.MutationAddInterfaceArgs) : Promise<ServiceTypes.UaTopologyInterface>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddInterface($sid: Int!, $browseName: String!, $name: String!, $typeId: String!, $libraryId: String!, $parentId: String!, $direction: UaDirection!, $display: String!) {
        addInterface(sid: $sid, browseName: $browseName, name: $name, typeId: $typeId, libraryId: $libraryId, parentId: $parentId, direction: $direction, display: $display) {
          nodeId
          name
          typeId
          writeMask
          direction
          display
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addInterface;
    }

    public async getFunctionBlockDiagram(args : ServiceTypes.QueryGetFunctionBlockDiagramArgs) : Promise<ServiceTypes.UaTopology>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetFunctionBlockDiagram($sid: Int!, $nodeId: String!) {
        getFunctionBlockDiagram(sid: $sid, nodeId: $nodeId) {
          objects {
            nodeId
            name
            typeId
            writeMask
            display
            interfaces {
              nodeId
              name
              typeId
              writeMask
              direction
              display
            }
          }
          interfaces {
            nodeId
            name
            typeId
            writeMask
            direction
            display
          }
          links {
            sourceId
            targetId
          }
        }
      }
        `;

      let {data} = await client.query({
              query: request,
              variables: args
            });

      return data.getFunctionBlockDiagram;      
    }

    public async addFunctionBlock(args : ServiceTypes.MutationAddFunctionBlockArgs) : Promise<ServiceTypes.UaTopologyObject>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddFunctionBlock($sid: Int!, $browseName: String!, $name: String!, $typeId: String!, $libraryId: String!, $parentId: String!, $display: String!) {
        addFunctionBlock(sid: $sid, browseName: $browseName, name: $name, typeId: $typeId, libraryId: $libraryId, parentId: $parentId, display: $display) {
          nodeId
          name
          typeId
          writeMask
          display
          interfaces {
            nodeId
            name
            typeId
            writeMask
            direction
            display
          }
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addFunctionBlock;
    }

    public async addFBDiagramVariable(args : ServiceTypes.MutationAddFbDiagramVariableArgs) : Promise<ServiceTypes.UaTopologyInterface>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddFBDiagramVariable($sid: Int!, $browseName: String!, $name: String!, $dataType: String!, $parentId: String!, $display: String!) {
        addFBDiagramVariable(sid: $sid, browseName: $browseName, name: $name, dataType: $dataType, parentId: $parentId, display: $display) {
          nodeId
          name
          typeId
          writeMask
          direction
          display
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addFBDiagramVariable;
    }

    public async updateLinks(args : ServiceTypes.MutationUpdateLinksArgs) : Promise<ServiceTypes.UaTopologyInterface>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation UpdateLinks($sid: Int!, $linksToAdd: [UaTopologyLinkInput]!, $linksToDelete: [UaTopologyLinkInput]!, $reference: String!) {
        updateLinks(sid: $sid, linksToAdd: $linksToAdd, linksToDelete: $linksToDelete, reference: $reference)
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.updateLinks;
    }    

    private url : string;
}
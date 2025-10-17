import { ApolloClient, InMemoryCache, gql } from '@apollo/client';
import * as ServiceTypes from "./modellingapitypes";

export class ModellingServiceApi
{
    constructor(url: string)
    {
      this.url = url ? url : "http://172.26.18.29:4000"; 
      // this.url = url ? url : "http://127.0.0.1:4000";    
    }


    // public async getIcons(args : ServiceTypes.QueryGetIconsArgs)
    // {
    //   let client = new ApolloClient({
    //     uri: this.url,
    //     cache: new InMemoryCache()
    //   });

    //   let request = gql`
    //   query GetIcons($typeIds: String! ) {
    //     getIcons(typeIds: $typeIds )
    //   }
    //     `;

    //   let { data, error } = await client.query({
    //           query: request,
    //           variables: args
    //         });

    //   if (error) throw error;

    //   return data;      
    // }

    public async getIcons(args : ServiceTypes.QueryGetIconsArgs) : Promise<string>
    {
      let client = new ApolloClient({
          uri: this.url,
          cache: new InMemoryCache()
        });

      let request = gql`
      query GetIcons(  $typeIds: [String]!) {
        getIcons(  typeIds: $typeIds)
      }
          `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;
      return data.getIcons;    
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
          desc
        }
      }
            `;
      //此接口不存在 description 参数
      // description

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
      mutation AddUaService($name: String!, $type: String!,  ) {
        addUaService(name: $name, type: $type,  ) {
          name
          type 
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
      mutation SetUaService($serviceId: Int!, $name: String, $desc: String) {
        setUaService(serviceId: $serviceId, name: $name, desc: $desc)
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
      mutation DeleteUaService($serviceId: Int!) {
        deleteUaService(serviceId: $serviceId)
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
    public async getUaServiceSettings(args : ServiceTypes.UaServiceSettings) : Promise<ServiceTypes.UaServiceSettings>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache(),
          });

      let request = gql`
      query GetUaServiceSettings($serviceId: Int!) {
        getUaServiceSettings(serviceId: $serviceId){
          servers {
            id
            type
            enabled
            configure{
              netAddr
              port
            }
          }
        }
      }`;

      let { data, error } = await client.query({
                query: request,
                variables: args
              });

      if (error) throw error;
      
      return data.getUaServiceSettings;
    }
    public async browseNode(args : ServiceTypes.QueryBrowseNodeArgs) : Promise<ServiceTypes.UaNode[]>
    {
      let client = new ApolloClient({
            uri: this.url,
            cache: new InMemoryCache(),
          }); 
          console.log(args,'ssssssssss');
      let request = gql`
        query BrowseNode($showHidedNode: Boolean!,$sid: Int!, $nodeId: String!, $nodeClassToReturn: [UaNodeClass]!, $isForward: Boolean!, $limit: Int, $page: Int) {
            browseNode(showHidedNode: $showHidedNode,sid: $sid, nodeId: $nodeId, nodeClassToReturn: $nodeClassToReturn, isForward: $isForward, limit: $limit, page: $page) {
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
            console.log(args,'requestrequestrequest');
      let { data, error } = await client.query({
                query: request,
                variables: args
              });

      if (error) throw error;
      
      return data.browseNode;
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

      // let request = gql`
      // query GetAttributesAndProperties($sid: Int!, $nodeId: String!) {
      //   getAttributesAndProperties(sid: $sid, nodeId: $nodeId) {
      //     attribute { nodeId nodeClass writeMask desc typeId typeName }
      //     properties { nodeId browseName name value dataType valueRank enabled }
      //     subPropGroups { nodeId name }
      //     specific { notifier }
      //   }
      // }
      //   `;
      let request = gql`
      query GetAttributesAndProperties($sid: Int!, $nodeId: String!) {
        getAttributesAndProperties(sid: $sid, nodeId: $nodeId) {
          attribute { nodeId nodeClass writeMask desc typeId typeName }
          properties { nodeId browseName name value dataType valueRank enabled }
          subPropGroups { nodeId name }
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
      // argumentMappings { methodTriggerId triggerType inputArgumentTargetIds }
      let request = gql`
      query GetMembers($sid: Int!, $nodeId: String!) {
        getMembers(sid: $sid, nodeId: $nodeId) {
          variables { nodeId browseName writeMask name value dataType valueRank accessLevel historizing enabled }
          methods { nodeId browseName name writeMask executable enabled
            inputArguments { name dataType valueRank }
            outputArguments { name dataType valueRank }
            isConfigurable
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

      return data.addObject;
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
    public async setNodesDisplay(args : ServiceTypes.MutationSetNodesDisplayArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetNodesDisplay($sid: Int!, $nodesDisplay:[UaTopologyNodeDisplayInput]!) {
        setNodesDisplay(sid: $sid, nodesDisplay: $nodesDisplay )
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setName;
    }
    public async setDisplay(args : ServiceTypes.MutationSetDisplayArgs) : Promise<ServiceTypes.UaSizeAndPositionInput>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetDisplay($sid: Int!,$nodeId: String!,  $display:UaSizeAndPositionInput!) {
        setDisplay(sid: $sid,nodeId: $nodeId, display: $display )
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.setDisplay;
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
      mutation SetMethodArguments($sid: Int!, $methodId: String!, $inputArguments: [UaMethodArgumentInput]!, $invocation: UaMethodInvocationInput!) {
        setMethodArguments(sid: $sid, methodId: $methodId, inputArguments: $inputArguments, invocation: $invocation)
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
          isAbstract
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;

      return data.getDataTypes;
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
                  
      // return data.getUaObjectTypeConfigures;
      return data.getObjectTypeConfigures

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
           __typename
        parentTypeId
        libraries {
            typeId
            browseName
            name
            isAbstract
            isAddIn
        }
        hasMoreLibraries
        }
      }
        `;

      let { data, error } = await client.query({
              query: request,
              variables: args
            });

      if (error) throw error;
      return data.getObjectTypeLibs;
      // return data.getUaObjectTypeLibs;      
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

      return data.getVariableTypeLibs; 
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
          desc
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

      return data.getSubType;
    }  

    public async addObjectType(args : ServiceTypes.MutationAddObjectTypeArgs) : Promise<ServiceTypes.UaType>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddObjectType($browseName: String!, $name: String!, $parentType: String! ) {
        addObjectType(browseName: $browseName, name: $name, parentType: $parentType ) {
          nodeId
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

      return data.addObjectType;
    }

    public async getTemplateLibs(args : ServiceTypes.QueryGetTemplateLibsArgs) : Promise<ServiceTypes.UaTemplateLib[]>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      query GetTemplateLibs{
        getTemplateLibs{
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

      return data.getTemplateLibs;
    }

    public async getStateMachine(args : ServiceTypes.QueryGetStateMachineArgs) : Promise<ServiceTypes.UaStateMachine>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      // position 返回值里面的

      let request = gql`
      query GetStateMachine($sid: Int!, $stateMachineId: String!) {
        getStateMachine(sid: $sid, stateMachineId: $stateMachineId) {
          states {
            nodeId
            name
            configureOption 
            stateNumber
            initialState
            groupId
            parentgroupId
            display{
               posX
               posY
            }
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

      return data.getStateMachine;
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

      return data.getStateAction;
    }

    public async setStateAction(args : ServiceTypes.MutationSetStateActionArgs) : Promise<string>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation SetStateAction($sid: Int!, $stateActionId: String!, $objectId: String!,$methodId: String!) {
        setStateAction(sid: $sid, stateActionId: $stateActionId, objectId: $objectId,methodId:$methodId)
      }
        `;

      let { data } = await client.mutate({
        mutation: request,
          variables: args
        });
      return data.setStateAction;
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

      return data.getTransitionGuard;
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

      return data.setSubStateMachine;      
    }

    public async addState(args : ServiceTypes.MutationAddStateArgs) : Promise<ServiceTypes.UaState>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });

      let request = gql`
      mutation AddState($sid: Int!, $browseName: String!, $name: String!, $parentId: String!, $stateNumber: Int!, $display: UaSizeAndPositionInput!) {
        addState(sid: $sid, browseName: $browseName, name: $name, parentId: $parentId, stateNumber: $stateNumber, display: $display) {
          nodeId
          name
          configureOption
          stateNumber
          initialState
          groupId
          parentgroupId
          display{
          posX
          posY
          }
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });

      return data.addState;   
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
      // interfaces {
      //   nodeId
      //   name
      //   typeId
      //   writeMask
      //   direction
      //   display
      // }
      let request = gql`
      query GetProcessTopology($sid: Int!, $nodeId: String!) {
        getProcessTopology(sid: $sid, nodeId: $nodeId) {
           __typename
        objects {
            nodeId
            name
            typeId
            writeMask
            display {
                sizeX
                sizeY
                posX
                posY
            }
            interfaces {
                nodeId
                direction
                display {
                    posX
                    posY
                }
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
x
    public async addProcessObject(args : ServiceTypes.MutationAddProcessObjectArgs) : Promise<ServiceTypes.UaTopologyObject>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });
      // $sid: Int!,
      let request = gql`
      mutation AddProcessObject( $browseName: String!, $name: String!, $typeId: String!, $parentId: String!, $display: UaSizeAndPositionInput!) {
        addProcessObject(  browseName: $browseName, name: $name, typeId: $typeId,  parentId: $parentId, display: $display) {
          nodeId
          name
          typeId
          writeMask
          display{
              posX
              posY 
              sizeX
              sizeY
              }
          interfaces{
            nodeId
           
            direction
            display{
              posX
              posY  
              }
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
    public async addProcessInterface(args : ServiceTypes.MutationDeleteProcessInterfaceArgs) : Promise<ServiceTypes.UaTopologyLink>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });
      
      let request = gql`
      mutation AddProcessInterface($fromObjectId: String!, $toObjectId: String!, $sourceInterfaceDisplay: UaPositionInput!,$targetInterfaceDisplay:UaPositionInput!) {
        addProcessInterface(fromObjectId: $fromObjectId, toObjectId: $toObjectId, sourceInterfaceDisplay: $sourceInterfaceDisplay,targetInterfaceDisplay:$targetInterfaceDisplay) {
            source {
              nodeId
              direction
              display { posX posY }
            }
            target {
              nodeId
              direction
              display { posX posY }
            }
            link { sourceId targetId }

        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });
      return data.addProcessInterface;
    }
    public async deleteProcessInterface(args : ServiceTypes.MutationDeleteProcessInterfaceArgs) : Promise<ServiceTypes.UaTopologyLink>
    {
      let client = new ApolloClient({
        uri: this.url,
        cache: new InMemoryCache()
      });
      
      let request = gql`
      mutation DeleteProcessInterface(  $sourceInterfaceId:String!,$targetInterfaceId:String!) {
        deleteProcessInterface(  sourceInterfaceId: $sourceInterfaceId,targetInterfaceId:$targetInterfaceId){
          sourceId
          targetId
        }
      }
        `;

      let {data} = await client.mutate({
              mutation: request,
              variables: args
            });
      return data.deleteProcessInterface;
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
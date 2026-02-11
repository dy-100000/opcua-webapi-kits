import { Attributes, BrowseDescription, BrowseDirection, NodeClass, ReadValueId, ReferenceTypeIds, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataType, UaDataValue, UaError, UaInstanceNode, UaLocalizedText, UaNode, UaNodeId, UaObject, UaObjectType, UaVariableType, UaPayloadMapper, UaReferenceDescriptor, UaReferenceType, UaVariable, VariableIds, VariableTypeIds, parseUaNodeId, UaMethod, UaLink } from "../../common";
import { UaWebClient } from "../UaWebClient"

type NodeToBrowse = {
    nodeId : UaNodeId;
    nodeClassToReturn : number; 
}

type CpToBrowse = {
    nodeId : UaNodeId;
    continuationPoint: string;
}

type BrowseResult = {
    fromNodeId : UaNodeId;
    nodeId : UaNodeId;
    nodeClass : number;
    browseName : string;
    displayName : UaLocalizedText;
    referenceTypeId : UaNodeId;
    isForward: boolean;
    typeDefinitionId?: UaNodeId;
}

type ReadResult = {
    parentId : UaNodeId;
    node: UaNode;
}

export type ReadChildResult = {
    nodeId: UaNodeId;
    children: Array<UaNode>;
}

export type ReadLinkResult = {
    nodeId: UaNodeId;
    links: Array<UaLink>;
}

export abstract class UaNodeReaderBase
{      
    protected _finished : boolean;
    protected _nodesToBrowse : Array<NodeToBrowse>;
    protected _continuationPointToBrowse: Array<CpToBrowse>;
    protected _browseResults : Array<BrowseResult>;
    protected _readResults : Map<string, ReadResult>;
    protected _nodeClassToReturn : number;

    private static s_variableTypeWithoutMember : Set<string> = null;
    private static s_variableNameToReadValue : Set<string> = null;

    constructor(
        returnVariables ? : boolean | null,
        returnMethod? : boolean | null)
    {
        this._finished = false;
        this._nodesToBrowse = [];
        this._continuationPointToBrowse = [];
        this._browseResults = [];
        this._readResults = new Map;

        this._nodeClassToReturn = 0;       

        if (false != returnVariables) this._nodeClassToReturn += NodeClass.Variable;
        if (false != returnMethod) this._nodeClassToReturn += NodeClass.Method;

        if (null == UaNodeReaderBase.s_variableTypeWithoutMember)
        {
            UaNodeReaderBase.s_variableTypeWithoutMember = new Set;
            UaNodeReaderBase.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.BaseVariableType).toString());
            UaNodeReaderBase.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.PropertyType).toString());
            UaNodeReaderBase.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.BaseDataVariableType).toString());
        }

        if (null == UaNodeReaderBase.s_variableNameToReadValue)
        {
            UaNodeReaderBase.s_variableNameToReadValue = new Set;
            UaNodeReaderBase.s_variableNameToReadValue.add("InputArguments");
            UaNodeReaderBase.s_variableNameToReadValue.add("OutputArguments");
            UaNodeReaderBase.s_variableNameToReadValue.add("EnumStrings");
            UaNodeReaderBase.s_variableNameToReadValue.add("EnumValues");
        }
    }

    protected async _browseNodes(client : UaWebClient)
    {
        let nodesToBrowse = this._nodesToBrowse.splice(0, 50);
        let browseDescriptions: Array<BrowseDescription> = [];

        for (let item of nodesToBrowse)
        {           
            browseDescriptions.push({
                NodeId: item.nodeId.toString(),
                BrowseDirection: BrowseDirection.Forward,
                ReferenceTypeId: ReferenceTypeIds.HierarchicalReferences,
                IncludeSubtypes: true,
                NodeClassMask: item.nodeClassToReturn,
                ResultMask: 63
            });
        }

        let results = await client.browse(browseDescriptions, 50);

        for (let i=0; i<nodesToBrowse.length; ++i)
        {
            let currentNode = nodesToBrowse[i];
            let currentResult = results[i];

            let statusCode = UaPayloadMapper.statusCodeFromWebApi(currentResult.StatusCode);
            if (statusCode.isNotGood()) throw new UaError(statusCode);

            if (!currentResult.References) continue;

            for (let item of currentResult.References)
            {
                let reference = UaPayloadMapper.referenceDescriptionFromWebApi(item);

                this._browseResults.push({ 
                    fromNodeId: currentNode.nodeId,
                    nodeId: reference.nodeId.getNodeId(),
                    nodeClass: reference.nodeClass,
                    browseName: reference.browseName,
                    displayName: reference.displayName,
                    referenceTypeId: reference.referenceTypeId,
                    isForward: reference.isForward,
                    typeDefinitionId: reference.typeDefinition?.getNodeId()
                });

                this._updateNodesToBrowse(reference);
            }

            if (currentResult.ContinuationPoint && currentResult.ContinuationPoint.length != 0)
            {
                this._continuationPointToBrowse.push({
                    nodeId: currentNode.nodeId,
                    continuationPoint: currentResult.ContinuationPoint
                });
            }
        }       
    }
    
    protected async _browseContinuationPoints(client : UaWebClient)
    {
        let cpsToBrowse = this._continuationPointToBrowse.splice(0, 50);
        let continuationPoints: Array<string> = [];

        for (let item of cpsToBrowse)
        {           
            continuationPoints.push(item.continuationPoint);
        }

        let results = await client.browseNext(continuationPoints, false);

        for (let i=0; i<cpsToBrowse.length; ++i)
        {
            let currentNode = cpsToBrowse[i];
            let currentResult = results[i];

            let statusCode = UaPayloadMapper.statusCodeFromWebApi(currentResult.StatusCode);
            if (statusCode.isNotGood()) throw new UaError(statusCode);

            if (!currentResult.References) continue;

            for (let item of currentResult.References)
            {
                let reference = UaPayloadMapper.referenceDescriptionFromWebApi(item);
                this._browseResults.push({ 
                    fromNodeId: currentNode.nodeId,
                    nodeId: reference.nodeId.getNodeId(),
                    nodeClass: reference.nodeClass,
                    browseName: reference.browseName,
                    displayName: reference.displayName,
                    referenceTypeId: reference.referenceTypeId,
                    isForward: reference.isForward,
                    typeDefinitionId: reference.typeDefinition?.getNodeId()
                });

                this._updateNodesToBrowse(reference);
            }

            if (currentResult.ContinuationPoint && currentResult.ContinuationPoint.length != 0)
            {
                this._continuationPointToBrowse.push({
                    nodeId: currentNode.nodeId,
                    continuationPoint: currentResult.ContinuationPoint
                });
            }
        } 
    }

    protected async _readNodes(client : UaWebClient)
    {
        if (this._browseResults.length == 0) return;

        let nodesToRead = this._browseResults.splice(0, 100);

        await this._readTypes(nodesToRead, client);
        await this._readObjects(nodesToRead, client);
        await this._readVariables(nodesToRead, client);
        await this._readMethods(nodesToRead, client);
    }

    protected _buildNodeTree()
    {
        if (this._finished) return;

        for (let item of this._readResults)
        {
            let childNode = item[1].node;
            let parentNode = this._readResults.get(item[1].parentId.toString());           

            if (!parentNode) continue;            

            if (NodeClass.Object == parentNode.node.nodeClass)
            {
                let parent = parentNode.node as UaObject;

                if (NodeClass.Variable == childNode.nodeClass ||
                    NodeClass.Method == childNode.nodeClass) 
                {
                    parent.addMember(childNode as UaInstanceNode);
                }
            } else if (NodeClass.Variable == parentNode.node.nodeClass) {
                let parent = parentNode.node as UaVariable;

                if (NodeClass.Variable == childNode.nodeClass)
                {
                    parent.addMember(childNode as UaVariable);
                }
            } else if (NodeClass.Method == parentNode.node.nodeClass) {
                let parent = parentNode.node as UaVariable;

                if (NodeClass.Variable == childNode.nodeClass) 
                {
                    parent.addMember(childNode as UaVariable);
                }
            } else if (NodeClass.ObjectType == parentNode.node.nodeClass) {  
                let parent = parentNode.node as UaObjectType;

                if (NodeClass.Variable == childNode.nodeClass ||
                    NodeClass.Method == childNode.nodeClass) 
                {  
                    parent.addMember(childNode as UaInstanceNode);
                }
            } else if (NodeClass.VariableType == parentNode.node.nodeClass) {
                let parent = parentNode.node as UaVariableType;

                if (NodeClass.Variable == childNode.nodeClass)
                {
                    parent.addMember(childNode as UaVariable);
                }
            } else if (NodeClass.DataType == parentNode.node.nodeClass) {
                let parent = parentNode.node as UaDataType;

                if (NodeClass.Variable == childNode.nodeClass)
                {
                    parent.setEnumVariable(childNode as UaVariable);
                }
            }
        }

        this._finished = true;
    }

    private async _readTypes(references : Array<BrowseResult>, client : UaWebClient)
    {
        let typesToRead : Array<BrowseResult> = [];

        for (let item of references)
        {
            if (item.nodeClass != NodeClass.ObjectType &&
                item.nodeClass != NodeClass.DataType &&
                item.nodeClass != NodeClass.VariableType &&
                item.nodeClass != NodeClass.ReferenceType) continue;

            typesToRead.push(item);
        }

        if (0 == typesToRead.length) return;
              
        let nodesToRead: Array<ReadValueId> = [];
        
        for (let item of typesToRead)
        {
            nodesToRead.push({ 
                NodeId: item.nodeId.toString(), 
                AttributeId: Attributes.IsAbstract });

            if (item.nodeClass == NodeClass.VariableType)
            {
                nodesToRead.push({ 
                NodeId: item.nodeId.toString(), 
                AttributeId: Attributes.DataType });

                nodesToRead.push({ 
                NodeId: item.nodeId.toString(), 
                AttributeId: Attributes.ValueRank });
            }
        }

        let results = await client.read(nodesToRead);
        
        let dataValues : Array<UaDataValue> = [];                        
        for (let item of results)
        {
            dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));
        }          
        
        for (let i=0; i<typesToRead.length; ++i)
        {
            let dataIndex = (typesToRead[i].nodeClass == NodeClass.VariableType) ? i*3 : i; 

            if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);

            let isAbstract = dataValues[dataIndex].value.toBoolean();
            if (null == isAbstract) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));

            let dataType = UaNodeId.nullNodeId;
            let valueRank = -1;

            if (typesToRead[i].nodeClass == NodeClass.VariableType)
            {
                if (dataValues[dataIndex + 1].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex + 1].statusCode);
                if (dataValues[dataIndex + 2].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex + 2].statusCode);

                dataType = dataValues[dataIndex + 1].value.toNodeId();
                valueRank = dataValues[dataIndex + 2].value.toNumber();

                if (null == dataType || null == valueRank) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
            }

            let typeNode: UaNode;

            if (typesToRead[i].nodeClass == NodeClass.ObjectType)
            {
                typeNode = new UaObjectType(
                    typesToRead[i].nodeId,
                    typesToRead[i].browseName,
                    typesToRead[i].displayName,
                    isAbstract);  
            } else if (typesToRead[i].nodeClass == NodeClass.DataType) {
                typeNode = new UaDataType(
                    typesToRead[i].nodeId,
                    typesToRead[i].browseName,
                    typesToRead[i].displayName,
                    isAbstract);  
            } else if (typesToRead[i].nodeClass == NodeClass.VariableType) {
                typeNode = new UaVariableType(
                    typesToRead[i].nodeId,
                    typesToRead[i].browseName,
                    typesToRead[i].displayName,
                    isAbstract,
                    dataType,
                    valueRank);
            } else if (typesToRead[i].nodeClass == NodeClass.ReferenceType) {
                typeNode = new UaReferenceType(
                    typesToRead[i].nodeId,
                    typesToRead[i].browseName,
                    typesToRead[i].displayName,
                    isAbstract,
                    UaLocalizedText.nullText,
                    false);
            }

            typeNode.refToParent = typesToRead[i].referenceTypeId;

            this._readResults.set(typeNode.nodeId.toString(), 
                {
                    parentId: typesToRead[i].fromNodeId,
                    node: typeNode
                });
        }

    }

    private async _readObjects(references : Array<BrowseResult>, client : UaWebClient)
    {
        let objectsToRead : Array<BrowseResult> = [];

        for (let item of references)
        {
            if (item.nodeClass != NodeClass.Object) continue;
            objectsToRead.push(item);
        }

        if (0 == objectsToRead.length) return;
              
        let nodesToRead: Array<ReadValueId> = [];
        
        for (let item of objectsToRead)
        {
            nodesToRead.push({ 
                NodeId: item.nodeId.toString(), 
                AttributeId: Attributes.EventNotifier })
        }

        let results = await client.read(nodesToRead);
        
        let dataValues : Array<UaDataValue> = [];                        
        for (let item of results)
        {
            dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));
        }          
        
        for (let i=0; i<objectsToRead.length; ++i)
        {
            if (dataValues[i].statusCode.isNotGood()) throw new UaError(dataValues[i].statusCode);

            let eventNotifier = dataValues[i].value.toNumber();
            if (null == eventNotifier || undefined == objectsToRead[i].typeDefinitionId) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
                        
            let objectNode = new UaObject(
                objectsToRead[i].nodeId,
                objectsToRead[i].browseName,
                objectsToRead[i].displayName,
                eventNotifier,
                objectsToRead[i].typeDefinitionId);

            objectNode.refToParent = objectsToRead[i].referenceTypeId;           

            this._readResults.set(objectNode.nodeId.toString(), 
                {
                    parentId: objectsToRead[i].fromNodeId,
                    node: objectNode
                });
        }
    }
   
    private async _readVariables(references : Array<BrowseResult>, client : UaWebClient)
    {
        let variablesToRead : Array<BrowseResult> = [];
        let nodeIds : Array<UaNodeId> = [];

        for (let item of references)
        {
            if (item.nodeClass != NodeClass.Variable) continue;
            variablesToRead.push(item);
            nodeIds.push(item.nodeId);
        }

        if (0 == nodeIds.length) return;
              
        let results = await client.readVariableAttributes(nodeIds); 
        let valuesToRead : Array<UaNodeId> = [];  
        
        for (let i=0; i<variablesToRead.length; ++i)
        {
            let variableNode = new UaVariable(
                    variablesToRead[i].nodeId,
                    variablesToRead[i].browseName,
                    variablesToRead[i].displayName,
                    results[i].dataType,
                    results[i].valueRank,
                    results[i].accessLevel,
                    results[i].userAccessLevel,
                    results[i].historizing,
                    variablesToRead[i].typeDefinitionId);

            this._readResults.set(variableNode.nodeId.toString(), 
                {
                    parentId: variablesToRead[i].fromNodeId,
                    node: variableNode
                });

            if (UaNodeReaderBase.s_variableNameToReadValue.has(variableNode.browseName)) valuesToRead.push(variableNode.nodeId);
        }       

        if (valuesToRead.length != 0)
        {
            let values = await client.readValues(valuesToRead);

            for (let i=0; i<valuesToRead.length; ++i)
            {
                if (values[i].statusCode.isNotGood()) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
            
                let variableNode = this._readResults.get(valuesToRead[i].toString());
                (variableNode.node as UaVariable).value = values[i].value;
            }
        }
    }

    private async _readMethods(references : Array<BrowseResult>, client : UaWebClient)
    {
        for (let item of references)
        {
            if (item.nodeClass != NodeClass.Method) continue;
            
            let methodNode = new UaMethod(
                    item.nodeId,
                    item.browseName,
                    item.displayName);
            
            this._readResults.set(methodNode.nodeId.toString(), 
                {
                    parentId: item.fromNodeId,
                    node: methodNode
                });
        }
    }

    private _updateNodesToBrowse(reference: UaReferenceDescriptor)
    {
        let nodeClassToReturn : number = NodeClass.Unspecified;

        if (reference.nodeClass == NodeClass.ObjectType ||
            reference.nodeClass == NodeClass.Object)
        {
            nodeClassToReturn = this._nodeClassToReturn;
        } else if (reference.nodeClass == NodeClass.Variable) {
            if (reference.typeDefinition && 
                !UaNodeReaderBase.s_variableTypeWithoutMember.has(reference.typeDefinition.getNodeId().toString()))
            {
                nodeClassToReturn = NodeClass.Variable;
            }
        } else if (reference.nodeClass == NodeClass.Method || 
            reference.nodeClass == NodeClass.VariableType ||
            reference.nodeClass == NodeClass.DataType) {
            nodeClassToReturn = NodeClass.Variable;
        }

        if (NodeClass.Unspecified != nodeClassToReturn)
        {
            this._nodesToBrowse.push({
                nodeId: reference.nodeId.getNodeId(),
                nodeClassToReturn: nodeClassToReturn
            });
        }
    }
}

export class UaNodeReader extends UaNodeReaderBase
{
    private _nodeIds : Set<string>;
    private _isRootNodeRead : boolean;

    constructor(
        nodeIds : Array<UaNodeId>,
        returnVariables ? : boolean | null,
        returnMethod? : boolean | null)
    {
        super(returnVariables, returnMethod);

        if (nodeIds.length == 0) this._finished = true;

        this._nodeIds = new Set;

        for (let item of nodeIds)
        {
            this._nodeIds.add(item.toString());
        }        
    }

    async read(client : UaWebClient)
    {
        if (this._finished) return;

        if (!this._isRootNodeRead) {
            await this._readRootNodes(client);
        } else if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints(client);
        } else if (this._nodesToBrowse.length != 0) {
            await this._browseNodes(client);
        } else if (this._browseResults.length != 0) {
            await this._readNodes(client);
        } else {
            this._buildNodeTree();
        }
    }

    isFinish() : boolean
    {
        return this._finished;
    }
    
    getResults() : Array<UaNode>
    {
        let ret : Array<UaNode> = [];

        for (let item of this._readResults)
        {
            if (!this._nodeIds.has(item[1].node.nodeId.toString())) continue;
            ret.push(item[1].node);
        }

        return [...ret.values()];
    } 

    private async _readRootNodes(client : UaWebClient)
    {
        if (this._isRootNodeRead) return;

        let nodeIds : UaNodeId[] = [];
        let nodesToRead: Array<ReadValueId> = [];
        
        for (let item of this._nodeIds)
        {  
            nodeIds.push(parseUaNodeId(item));

            nodesToRead.push({
                NodeId: item, 
                AttributeId: Attributes.NodeClass });

            nodesToRead.push({ 
                NodeId: item, 
                AttributeId: Attributes.BrowseName });
        
            nodesToRead.push({ 
                NodeId: item, 
                AttributeId: Attributes.DisplayName });
        }

        let results = await client.read(nodesToRead);
        
        let dataValues : Array<UaDataValue> = [];                        
        for (let item of results)
        {
            dataValues.push(UaPayloadMapper.dataValueFromWebApi(item));
        }          
        
        let browseResults : Map<string,BrowseResult> = new Map;

        for (let i=0; i<nodeIds.length; ++i)
        {
            let dataIndex = i*3; 
            let currentNodeId = nodeIds[i];

            if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);
            if (dataValues[dataIndex+1].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex+1].statusCode);
            if (dataValues[dataIndex+2].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex+2].statusCode);
            
            let nodeClass = dataValues[dataIndex].value.toNumber();
            let browseName = dataValues[dataIndex+1].value.toString();
            let displayName = dataValues[dataIndex+2].value.toLocalizedText();

            if (null == nodeClass || null == browseName || null == displayName) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));

            if (NodeClass.Unspecified != this._nodeClassToReturn)
            {
                this._nodesToBrowse.push({
                    nodeId: currentNodeId,
                    nodeClassToReturn: this._nodeClassToReturn
                });
            }

            browseResults.set(
                currentNodeId.toString(),
                {
                    fromNodeId: UaNodeId.nullNodeId,
                    nodeId: currentNodeId,
                    nodeClass: nodeClass,
                    browseName: browseName,
                    displayName: displayName,
                    referenceTypeId: UaNodeId.nullNodeId,   
                    isForward: true,
                    typeDefinitionId: UaNodeId.nullNodeId});
        }

        let browseDescriptions: Array<BrowseDescription> = [];

        for (let item of browseResults)
        {           
            if (item[1].nodeClass != NodeClass.Object &&
                item[1].nodeClass != NodeClass.Variable) continue;

            browseDescriptions.push({
                NodeId: item[0],
                BrowseDirection: BrowseDirection.Forward,
                ReferenceTypeId: ReferenceTypeIds.HasTypeDefinition,
                IncludeSubtypes: false,
                NodeClassMask: NodeClass.ObjectType | NodeClass.VariableType,
                ResultMask: 0
            });
        }

        if (browseDescriptions.length != 0)
        {
            let results = await client.browse(browseDescriptions);

            for (let i=0; i<browseDescriptions.length; ++i)
            {
                let browseResult = browseResults.get(browseDescriptions[i].NodeId);
                if (undefined == browseResult) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));

                let statusCode = UaPayloadMapper.statusCodeFromWebApi(results[i].StatusCode);
                if (statusCode.isNotGood()) throw new UaError(statusCode);

                if (!results[i].References || results[i].References.length != 1) continue;

                let reference = UaPayloadMapper.referenceDescriptionFromWebApi(results[i].References[0]);
                browseResult.typeDefinitionId = reference.nodeId.getNodeId();
            }
        }

        this._browseResults = Array.from(browseResults.values());
        this._isRootNodeRead = true;
    } 
}

export class UaNodeChildReader extends UaNodeReaderBase
{
    private _nodeIds : Set<string>;

    constructor(
        nodeIds : Array<UaNodeId>,
        returnVariables ? : boolean | null,
        returnMethod? : boolean | null)
    {
        super(returnVariables,returnMethod);

        if (nodeIds.length == 0) this._finished = true;

        this._nodeIds = new Set;

        for (let item of nodeIds)
        {
            this._nodeIds.add(item.toString());
            this._nodesToBrowse.push({
                nodeId: item,
                nodeClassToReturn: NodeClass.Object | NodeClass.ObjectType | NodeClass.VariableType | NodeClass.DataType
            });
        }
    }

    async read(client : UaWebClient)
    {
        if (this._finished) return;

        if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints(client);
        } else if (this._nodesToBrowse.length != 0) {
            await this._browseNodes(client);
        } else if (this._browseResults.length != 0) {
            await this._readNodes(client);
        } else {
            this._buildNodeTree();
        }
    }

    isFinish() : boolean
    {
        return this._finished;
    }
    
    getResults() : Array<ReadChildResult>
    {
        let ret : Map<string, ReadChildResult> = new Map;

        for (let item of this._readResults)
        {
            if (!this._nodeIds.has(item[1].parentId.toString())) continue;

            let result : ReadChildResult;
            let parentId = item[1].parentId.toString();
            
            if (ret.has(parentId))
            {
                result = ret.get(parentId);
            } else {
                result = { nodeId: item[1].parentId, children: [] };
                ret.set(parentId, result);
            }

            result.children.push(item[1].node);
        }

        return [...ret.values()];
    }
}

export class UaNodeLinkReader
{
    private _nodesToBrowse : Array<UaNodeId>;
    private _continuationPointToBrowse : Array<CpToBrowse>;
    private _browseResults : Array<BrowseResult>;

    constructor(nodeIds : Array<UaNodeId>)
    {
        this._nodesToBrowse = nodeIds;
        this._continuationPointToBrowse = [];
        this._browseResults = [];
    }

    async read(client : UaWebClient)
    {
        if (this.isFinish()) return;

        if (this._nodesToBrowse.length != 0) {
            await this._browseNodes(client);
        } else if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints(client);
        }
    }

    isFinish() : boolean
    {
        return this._nodesToBrowse.length == 0 && this._continuationPointToBrowse.length == 0;
    }

    getResults() : Array<ReadLinkResult>
    {
        let ret : Map<string, ReadLinkResult> = new Map;

        for (let item of this._browseResults)
        {            
            let result : ReadLinkResult = ret.get(item.fromNodeId.toString());

            if (!result)
            {
                result = {
                    nodeId: item.fromNodeId,
                    links: []
                };

                ret.set(item.fromNodeId.toString(), result);
            }

            result.links.push(new UaLink(
                item.nodeId,
                item.nodeClass,
                item.referenceTypeId,
                item.isForward
            ));
        }

        return [...ret.values()];
    } 

    private async _browseNodes(client : UaWebClient)
    {
        if (this._nodesToBrowse.length == 0) return;

        let nodesToBrowse = this._nodesToBrowse.splice(0, 50);
        let browseDescriptions: Array<BrowseDescription> = [];

        for (let item of nodesToBrowse)
        {           
            browseDescriptions.push({
                NodeId: item.toString(),
                BrowseDirection: BrowseDirection.Both,
                ReferenceTypeId: ReferenceTypeIds.NonHierarchicalReferences,
                IncludeSubtypes: true,
                NodeClassMask: NodeClass.Object | NodeClass.Variable | NodeClass.Method,
                ResultMask: 7
            });
        }

        let results = await client.browse(browseDescriptions, 50);

        for (let i=0; i<nodesToBrowse.length; ++i)
        {
            let currentNodeId = nodesToBrowse[i];
            let currentResult = results[i];

            let statusCode = UaPayloadMapper.statusCodeFromWebApi(currentResult.StatusCode);
            if (statusCode.isNotGood()) throw new UaError(statusCode);

            if (!currentResult.References) continue;
            
            for (let item of currentResult.References)
            {
                let reference = UaPayloadMapper.referenceDescriptionFromWebApi(item);
                this._browseResults.push({
                    fromNodeId: currentNodeId,
                    nodeId: reference.nodeId.getNodeId(),
                    nodeClass: reference.nodeClass,
                    browseName: reference.browseName,
                    displayName: reference.displayName,
                    referenceTypeId: reference.referenceTypeId,
                    isForward: reference.isForward,
                    typeDefinitionId: reference.typeDefinition?.getNodeId()
                });
            }

            if (currentResult.ContinuationPoint && currentResult.ContinuationPoint.length != 0)
            {
                this._continuationPointToBrowse.push({
                    nodeId: currentNodeId,
                    continuationPoint: currentResult.ContinuationPoint
                });
            }
        }       
    }

    private async _browseContinuationPoints(client : UaWebClient)
    {
        let cpsToBrowse = this._continuationPointToBrowse.splice(0, 50);
        let continuationPoints: Array<string> = [];

        for (let item of cpsToBrowse)
        {           
            continuationPoints.push(item.continuationPoint);
        }

        let results = await client.browseNext(continuationPoints, false);

        for (let i=0; i<cpsToBrowse.length; ++i)
        {
            let currentNodeId = cpsToBrowse[i].nodeId;
            let currentResult = results[i];

            let statusCode = UaPayloadMapper.statusCodeFromWebApi(currentResult.StatusCode);
            if (statusCode.isNotGood()) throw new UaError(statusCode);

            if (!currentResult.References) continue;
            
            for (let item of currentResult.References)
            {
                let reference = UaPayloadMapper.referenceDescriptionFromWebApi(item);
                this._browseResults.push({
                    fromNodeId: currentNodeId,
                    nodeId: reference.nodeId.getNodeId(),
                    nodeClass: reference.nodeClass,
                    browseName: reference.browseName,
                    displayName: reference.displayName,
                    referenceTypeId: reference.referenceTypeId,
                    isForward: reference.isForward,
                    typeDefinitionId: reference.typeDefinition?.getNodeId()
                });
            }

            if (currentResult.ContinuationPoint && currentResult.ContinuationPoint.length != 0)
            {
                this._continuationPointToBrowse.push({
                    nodeId: currentNodeId,
                    continuationPoint: currentResult.ContinuationPoint
                });
            }
        }
    }
}


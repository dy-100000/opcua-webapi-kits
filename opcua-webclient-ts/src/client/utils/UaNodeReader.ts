import { Attributes, BrowseDescription, BrowseDirection, NodeClass, ReadValueId, ReferenceTypeIds, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataType, UaDataValue, UaError, UaInstanceNode, UaLocalizedText, UaNode, UaNodeId, UaObject, UaObjectType, UaVariableType, UaPayloadMapper, UaReferenceDescriptor, UaReferenceType, UaVariable, VariableIds, VariableTypeIds, parseUaNodeId } from "../../common";
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
    parentId : UaNodeId;
    nodeId : UaNodeId;
    nodeClass : number;
    browseName : string;
    displayName : UaLocalizedText;
    typeDefinitionId?: UaNodeId
}

type ReadResult = {
    parentId : UaNodeId;
    node: UaNode;
}

export type ReadChildResult = {
    nodeId: UaNodeId;
    children: Array<UaNode>;
}

export abstract class UaNodeReaderBase
{      
    protected _finished : boolean;
    protected _nodesToBrowse : Array<NodeToBrowse>;
    protected _continuationPointToBrowse: Array<CpToBrowse>;
    protected _browseResults : Array<BrowseResult>;
    protected _readResults : Map<string, ReadResult>;
    protected _returnVariable : boolean;
    protected _returnMethod : boolean;

    private static s_variableTypeWithoutMember : Set<string> = null;

    constructor(
        returnVariables : boolean,
        returnMethod : boolean)
    {
        this._finished = false;
        this._returnVariable = returnVariables;
        this._returnMethod = returnMethod;

        this._nodesToBrowse = [];
        this._browseResults = [];
        this._readResults = new Map;

        if (null == UaNodeReaderBase.s_variableTypeWithoutMember)
        {
            UaNodeReaderBase.s_variableTypeWithoutMember = new Set;
            UaNodeReaderBase.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.BaseVariableType).toString());
            UaNodeReaderBase.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.PropertyType).toString());
            UaNodeReaderBase.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.BaseDataVariableType).toString());
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
                    parentId: currentNode.nodeId,
                    nodeId: reference.nodeId.getNodeId(),
                    nodeClass: reference.nodeClass,
                    browseName: reference.browseName,
                    displayName: reference.displayName,
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
                    parentId: currentNode.nodeId,
                    nodeId: reference.nodeId.getNodeId(),
                    nodeClass: reference.nodeClass,
                    browseName: reference.browseName,
                    displayName: reference.displayName,
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

        if (this._browseResults.length == 0)
        {
            this._buildNodeTree();
        }
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
                    new UaLocalizedText(),
                    false);
            }

            this._readResults.set(typeNode.nodeId.toString(), 
                {
                    parentId: typesToRead[i].parentId,
                    node: typeNode
                });
        }

    }
   
    private _updateNodesToBrowse(reference: UaReferenceDescriptor)
    {
        let nodeClassToReturn : number = NodeClass.Unspecified;

        if (reference.nodeClass == NodeClass.ObjectType ||
            reference.nodeClass == NodeClass.Object)
        {
            if (!this._returnVariable && !this._returnMethod) return;

            if (this._returnVariable && this._returnMethod)
            {
                nodeClassToReturn = Number(NodeClass.Variable | NodeClass.Method);
            } else if (this._returnVariable) {
                nodeClassToReturn = NodeClass.Variable;
            } else if (this._returnMethod) {
                nodeClassToReturn = NodeClass.Method;
            }
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

    private _buildNodeTree()
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
}

export class UaNodeChildReader extends UaNodeReaderBase
{
    private _nodeIds : Set<string>;

    constructor(
        nodeIds : Array<UaNodeId>,
        returnVariables ? : boolean | null,
        returnMethod? : boolean | null)
    {
        super(
            (returnVariables) ? true : false, 
            (returnMethod) ? true : false);

        this._nodeIds = new Set;

        for (let item of nodeIds)
        {
            this._nodeIds.add(item.toString());
            this._nodesToBrowse.push({
                nodeId: item,
                nodeClassToReturn: Number(NodeClass.Object | NodeClass.ObjectType | NodeClass.VariableType | NodeClass.DataType)
            });
        }
    }

    async read(client : UaWebClient)
    {
        if (this._finished || this._nodeIds.size == 0) return;

        if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints(client);
        } else if (this._nodesToBrowse.length != 0) {
            await this._browseNodes(client);
        } else if (this._browseResults.length != 0) {
            await this._readNodes(client);
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

export class UaNodeReader extends UaNodeReaderBase
{
    private _nodeIds : Set<string>;
    private _isRootNodeRead : boolean;

    constructor(nodeIds : Array<UaNodeId>)
    {
        super(true, true);
        this._nodeIds = new Set;

        for (let item of nodeIds)
        {
            this._nodeIds.add(item.toString());
        }
    }

    async read(client : UaWebClient)
    {
        if (this._finished || this._nodeIds.size == 0) return;

        if (!this._isRootNodeRead) {
            await this._readRootNodes(client);
        } else if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints(client);
        } else if (this._nodesToBrowse.length != 0) {
            await this._browseNodes(client);
        } else if (this._browseResults.length != 0) {
            await this._readNodes(client);
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

            this._nodesToBrowse.push({
                nodeId: currentNodeId,
                nodeClassToReturn: Number(NodeClass.Variable | NodeClass.Method)
            });

            browseResults.set(
                currentNodeId.toString(),
                {
                    parentId: UaNodeId.nullNodeId,
                    nodeId: currentNodeId,
                    nodeClass: nodeClass,
                    browseName: browseName,
                    displayName: displayName,         
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
                NodeClassMask: Number(NodeClass.ObjectType | NodeClass.VariableType),
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
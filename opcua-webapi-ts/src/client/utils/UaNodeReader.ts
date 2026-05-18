import { Attributes, BrowseDirection, NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataType, UaError, UaInstanceNode, UaLocalizedText, UaNode, UaNodeId, UaObject, UaObjectType, UaVariableType, UaReferenceType, UaVariable, VariableTypeIds, parseUaNodeId, UaMethod, UaBrowseDescription, UaReferenceDescription, UaReadValueId, ReferenceTypeIds, UaReference, UaDataValue, UaVariableAttributes } from "../../common";
import { UaWebClient } from "../UaWebClient"

type CpToBrowse = {
    nodeId: UaNodeId;
    continuationPoint: string;
}

export type BrowseReferenceResult = {
    nodeId: UaNodeId;
    references: Array<UaReference>;
}

export type ReadChildNodeResult = {
    nodeId: UaNodeId;
    children: Array<UaNode>;
}

export class UaNodeReferenceBrowser {
    private _nodesToBrowse: Array<UaNodeId>;
    private _continuationPointToBrowse: Array<CpToBrowse>;
    private _referenceType: UaNodeId;
    private _nodeClassToReturn: number;
    private _returnDescription: boolean;
    private _maxNodesPerBrowse: number;
    private _maxReferencesPerNode: number;
    private _readResults: Map<string,Array<UaReference>>;

    constructor(
        nodeIds: Array<UaNodeId>,
        referenceType: UaNodeId,
        nodeClassToReturn: number,
        returnDescription: boolean,
        maxNodesPerBrowse?: number | null,
        maxReferencesPerNode?: number | null) 
    {
        this._nodesToBrowse = [];
        this._referenceType = referenceType;
        this._nodeClassToReturn = nodeClassToReturn;
        this._continuationPointToBrowse = [];
        this._returnDescription = returnDescription;
        this._maxNodesPerBrowse = (null == maxNodesPerBrowse || maxNodesPerBrowse <= 0) ? 50 : maxNodesPerBrowse;
        this._maxReferencesPerNode = (null == maxReferencesPerNode || maxReferencesPerNode < 0) ? 50 : maxReferencesPerNode;
        this._readResults = new Map<string, Array<UaReference>>();

        if (this._nodeClassToReturn != 0)
        {
            let nodeIdSet: Set<string> = new Set;
            for (let item of nodeIds) {
                if (nodeIdSet.has(item.toString())) continue;
                nodeIdSet.add(item.toString());
                this._nodesToBrowse.push(item);
            }
        }
    }

    async browse(client: UaWebClient) {
        if (this.isFinish()) return;
        await this.browseOnce(client);
        await this.browse(client);
    }

    public async browseOnce(client: UaWebClient) {        
        if (this._nodesToBrowse.length != 0) {
            await this._browseNodes(client);
        } else if (this._continuationPointToBrowse.length != 0) {
            await this._browseContinuationPoints(client);
        }
    }

    results(): Array<BrowseReferenceResult> {
        let results: Array<BrowseReferenceResult> = [];
        for (let item of this._readResults) {
            results.push({
                nodeId: parseUaNodeId(item[0]),
                references: item[1]
            });                       
        }

        this._readResults.clear();

        return results;
    }

    isFinish(): boolean {
        return (this._nodesToBrowse.length == 0) && (this._continuationPointToBrowse.length == 0);
    }

    private async _browseNodes(client: UaWebClient) {
        if (this._nodesToBrowse.length == 0) return;

        let nodesToBrowse = this._nodesToBrowse.splice(0, this._maxNodesPerBrowse);
        let browseDescriptions: Array<UaBrowseDescription> = [];

        for (let item of nodesToBrowse) {
            let browseDescription = new UaBrowseDescription(
                item,
                BrowseDirection.Forward,
                this._referenceType,
                true,
                this._nodeClassToReturn,
                63
            );

            browseDescriptions.push(browseDescription);
        }

        let results = await client.browse(browseDescriptions, this._maxReferencesPerNode);

        for (let i = 0; i < nodesToBrowse.length; ++i) {
            let currentNodeId = nodesToBrowse[i];
            let currentResult = results[i];

            if (currentResult.statusCode.isNotGood()) throw new UaError(currentResult.statusCode);

            let currentReferences: Array<UaReference> = this._readResults.get(currentNodeId.toString());
            if (!currentReferences)
            {
                currentReferences = [];
                this._readResults.set(currentNodeId.toString(), currentReferences);
            }

            let newReferences: Array<UaReference> = [];
            for (let item of currentResult.references) {
                let nodeId = item.nodeId.getNodeId();
                let typeDefinition = item.typeDefinition?.getNodeId();
                if (null == nodeId || null === typeDefinition) continue;

                let reference = new UaReference(
                        nodeId,
                        (item.nodeClass) ? item.nodeClass : NodeClass.Unspecified,
                        item.browseName ? item.browseName : "",
                        item.displayName ? item.displayName : UaLocalizedText.nullText,
                        item.referenceTypeId ? item.referenceTypeId : UaNodeId.nullNodeId,
                        (typeDefinition) ? typeDefinition : UaNodeId.nullNodeId);

                currentReferences.push(reference);
                newReferences.push(reference);
            }

            if (currentResult.continuationPoint) {
                this._continuationPointToBrowse.push({
                    nodeId: currentNodeId,
                    continuationPoint: currentResult.continuationPoint
                });
            }

            if (this._returnDescription) await this._readDescriptions(client, newReferences);
        }
    }

    private async _browseContinuationPoints(client: UaWebClient) {
        let cpsToBrowse = this._continuationPointToBrowse.splice(0, this._maxNodesPerBrowse);
        let continuationPoints: Array<string> = [];

        for (let item of cpsToBrowse) {
            continuationPoints.push(item.continuationPoint);
        }

        let results = await client.browseNext(continuationPoints, false);

        for (let i = 0; i < cpsToBrowse.length; ++i) {
            let currentNodeId = cpsToBrowse[i].nodeId;
            let currentResult = results[i];

            if (currentResult.statusCode.isNotGood()) throw new UaError(currentResult.statusCode);

            let newReferences: Array<UaReference> = [];
            for (let item of currentResult.references) {
                let nodeId = item.nodeId.getNodeId();
                let typeDefinition = item.typeDefinition?.getNodeId();
                if (null == nodeId || null === typeDefinition) continue;

                let currentReferences: Array<UaReference> = this._readResults.get(currentNodeId.toString());
                if (!currentReferences) 
                {
                    currentReferences = [];
                    this._readResults.set(currentNodeId.toString(), currentReferences);
                }

                let reference = new UaReference(
                        nodeId,
                        (item.nodeClass) ? item.nodeClass : NodeClass.Unspecified,
                        item.browseName ? item.browseName : "",
                        item.displayName ? item.displayName : UaLocalizedText.nullText,
                        item.referenceTypeId ? item.referenceTypeId : UaNodeId.nullNodeId,
                        (typeDefinition) ? typeDefinition : UaNodeId.nullNodeId);

                currentReferences.push(reference);
                newReferences.push(reference);
            }

            if (currentResult.continuationPoint) {
                this._continuationPointToBrowse.push({
                    nodeId: currentNodeId,
                    continuationPoint: currentResult.continuationPoint
                });
            }

            if (this._returnDescription) await this._readDescriptions(client, newReferences);
        }
    }

    private async _readDescriptions(
        client: UaWebClient,
        references: Array<UaReference>) {
        if (references.length === 0) return;

        let nodesToRead: Array<UaReadValueId> = [];

        for (let item of references) {
            nodesToRead.push(new UaReadValueId(item.nodeId, Attributes.Description));
        }

        let dataValues = await client.read(nodesToRead);
        for (let i = 0; i < references.length; ++i) {
            if (dataValues[i].statusCode.isGood()) {
                let description = dataValues[i].value.toLocalizedText();
                if (description) references[i].description = description;
            }
        }
    }
}

export class UaChildBrowser extends UaNodeReferenceBrowser {
    constructor(nodeIds: Array<UaNodeId>, returnDescription?: boolean) {
        super(
            nodeIds, 
            UaNodeId.from(ReferenceTypeIds.HierarchicalReferences), 
            NodeClass.Object | NodeClass.ObjectType| NodeClass.VariableType | NodeClass.DataType | NodeClass.ReferenceType, 
            returnDescription ?? false,
            20);
    }
}

export class UaLinkBrowser extends UaNodeReferenceBrowser {
    constructor(nodeIds: Array<UaNodeId>, returnDescription?: boolean) {
        super(
            nodeIds, 
            UaNodeId.from(ReferenceTypeIds.NonHierarchicalReferences), 
            NodeClass.Object, 
            returnDescription ?? false,
            20);
    }
}

export class UaNodeReader {
    private _nodeClassToReturn: number;
    private _returnDescription: boolean;
    private _returnAttributes: boolean;
    private _returnValue: boolean;    
    private static s_variableTypeWithoutMember: Set<string> = null;
    private static s_variablesNameToRead: Set<string> = null;

    constructor(
        returnDescription?: boolean | null,
        returnVariable?: boolean | null,
        returnMethod?: boolean | null,
        returnAttributes?: boolean | null,
        returnValue?: boolean | null) 
    {
        this._returnDescription = (returnDescription) ? returnDescription : false;
        this._returnAttributes = (returnAttributes) ? returnAttributes : false;
        this._returnValue = (returnValue) ? returnValue : false;

        this._nodeClassToReturn = 0;
        if (returnVariable) this._nodeClassToReturn += NodeClass.Variable;
        if (returnMethod) this._nodeClassToReturn += NodeClass.Method;

        if (null == UaNodeReader.s_variableTypeWithoutMember) {
            UaNodeReader.s_variableTypeWithoutMember = new Set;
            UaNodeReader.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.BaseVariableType).toString());
            UaNodeReader.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.PropertyType).toString());
            UaNodeReader.s_variableTypeWithoutMember.add(new UaNodeId(VariableTypeIds.BaseDataVariableType).toString());
        }

        if (null == UaNodeReader.s_variablesNameToRead) {
            UaNodeReader.s_variablesNameToRead = new Set;
            UaNodeReader.s_variablesNameToRead.add("InputArguments");
            UaNodeReader.s_variablesNameToRead.add("OutputArguments");
            UaNodeReader.s_variablesNameToRead.add("EnumStrings");
            UaNodeReader.s_variablesNameToRead.add("EnumValues");
        }
    }

    async read(nodeIds: Array<UaNodeId>, client: UaWebClient) : Promise<Array<UaNode>> {
        if (nodeIds.length == 0) return [];
        
        let nodeIdsToRead: Array<UaNodeId> = [];
        let nodeIdsSet: Set<string> = new Set();

        for (let item of nodeIds) {
            if (nodeIdsSet.has(item.toString())) continue;
            nodeIdsToRead.push(item);
            nodeIdsSet.add(item.toString());
        }

        let rootReferences = await this._readRootNodes(nodeIdsToRead, client);
        return await this.readByReferences(rootReferences, client);
    }

    async readByReferences(references: Array<UaReference>, client: UaWebClient) : Promise<Array<UaNode>>
    {
        if (references.length == 0) return [];

        let referencesToRead: Array<UaReference> = [];
        let nodeIdsToBrowse: Array<UaNodeId> = [];
        let nodeIdsSet: Set<string> = new Set();

        for (let item of references) {
            if (nodeIdsSet.has(item.nodeId.toString())) continue;
            referencesToRead.push(item);
            nodeIdsToBrowse.push(item.nodeId);
            nodeIdsSet.add(item.nodeId.toString());
        }

        let nodeReferenceBrowser = new UaNodeReferenceBrowser(
            nodeIdsToBrowse,
            UaNodeId.from(ReferenceTypeIds.HierarchicalReferences),
            this._nodeClassToReturn,
            false);

        await nodeReferenceBrowser.browse(client);
        let childReferences = nodeReferenceBrowser.results();

        let childNodesToBrowse: Array<UaNodeId> = [];
        for (let item of childReferences) {
            for (let reference of item.references) {
                if (NodeClass.Variable == reference.nodeClass &&
                    !UaNodeReader.s_variableTypeWithoutMember.has(reference.typeDefinitionId.toString())) {
                    childNodesToBrowse.push(reference.nodeId);
                }

                if (NodeClass.Method == reference.nodeClass) {
                    childNodesToBrowse.push(reference.nodeId);
                }
            }
        }

        let variableReferences: Array<BrowseReferenceResult> = [];
        if (childNodesToBrowse.length != 0) {
            let childReferenceBrowser = new UaNodeReferenceBrowser(
                childNodesToBrowse,
                UaNodeId.from(ReferenceTypeIds.HierarchicalReferences),
                NodeClass.Variable,
                false);

            await childReferenceBrowser.browse(client);
            variableReferences = childReferenceBrowser.results();
        }

        let allReferences: Array<UaReference> = [];

        for (let item of references) {
            allReferences.push(item);
        }

        for (let item of childReferences) {
            for (let reference of item.references) {
                allReferences.push(reference);
            }
        }

        for (let item of variableReferences) {
            for (let reference of item.references) {
                allReferences.push(reference);
            };
        }

        let nodesMap: Map<string, UaNode> = new Map<string, UaNode>();
        await this._readNodes(allReferences, nodesMap, client);

        for (let item of variableReferences) {
            this._buildNodeTree(item, nodesMap);
        }

        for (let item of childReferences) {
            this._buildNodeTree(item, nodesMap);
        }

        let rootNodes: Array<UaNode> = [];
        for (let item of references) {

            let node = nodesMap.get(item.nodeId.toString());
            if (node) rootNodes.push(node);
        }

        return rootNodes;
    }

    private async _readRootNodes(nodeIds: Array<UaNodeId>, client: UaWebClient) : Promise<Array<UaReference>> {
        let nodesToRead: Array<UaReadValueId> = [];

        for (let item of nodeIds) {
            nodesToRead.push(new UaReadValueId(
                item,
                Attributes.NodeClass));

            nodesToRead.push(new UaReadValueId(
                item,
                Attributes.BrowseName));

            nodesToRead.push(new UaReadValueId(
                item,
                Attributes.DisplayName));
        }

        let dataValues = await client.read(nodesToRead);
        let references: Array<UaReference> = [];
        let typeDefinitionsToRead: Array<UaReference> = [];

        for (let i = 0; i < nodeIds.length; ++i) {
            let dataIndex = i * 3;
            let currentNodeId = nodeIds[i];

            if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);
            let nodeClass = dataValues[dataIndex].value.toNumber();
            dataIndex++;

            if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);
            let browseName = dataValues[dataIndex].value.toString();
            dataIndex++;

            if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);
            let displayName = dataValues[dataIndex].value.toLocalizedText();
            dataIndex++;

            if (null == nodeClass || null == browseName || null == displayName) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));

            if (NodeClass.Unspecified != this._nodeClassToReturn) {
                let newReference = new UaReference(
                    currentNodeId,
                    nodeClass,
                    browseName,
                    displayName);

                references.push(newReference);
                if (NodeClass.Object == nodeClass) typeDefinitionsToRead.push(newReference);
            }
        }

        if (typeDefinitionsToRead.length != 0) {
            let browseDescriptions: Array<UaBrowseDescription> = [];

            for (let item of typeDefinitionsToRead) {
                let browseDescription = new UaBrowseDescription(
                            item.nodeId,
                            BrowseDirection.Forward,
                            UaNodeId.from(ReferenceTypeIds.HasTypeDefinition),
                            false,
                            NodeClass.ObjectType,
                            0);
                browseDescriptions.push(browseDescription);
            }

            let results = await client.browse(browseDescriptions);

            for (let i = 0; i < browseDescriptions.length; ++i) {
                if (results[i].statusCode.isNotGood()) throw new UaError(results[i].statusCode);
                if (results[i].references.length != 1) continue;
                typeDefinitionsToRead[i].typeDefinitionId = results[i].references[0].nodeId.getNodeId();
            }
        }

        return references;
    }

    private async _readNodes(references: Array<UaReference>, nodes: Map<string,UaNode>,client: UaWebClient) {
        if (references.length == 0) return;

        let refrencesToRead: Array<Array<UaReference>> = [];

        while (references.length != 0) {
            refrencesToRead.push(references.splice(0, 100));
        }
  
        for (let item of refrencesToRead) {
            await this._readTypes(item, nodes, client);
            await this._readObjects(item, nodes, client);
            await this._readVariables(item, nodes, client);
            await this._readMethods(item, nodes, client);
        }
    }

    private async _readTypes(references: Array<UaReference>, nodes: Map<string,UaNode>, client: UaWebClient) {
        let typesToRead: Array<UaReference> = [];
        let needReadAttributes = this._returnAttributes || this._returnDescription;

        for (let item of references) {
            if (item.nodeClass != NodeClass.ObjectType &&
                item.nodeClass != NodeClass.DataType &&
                item.nodeClass != NodeClass.VariableType &&
                item.nodeClass != NodeClass.ReferenceType) continue;
            
            typesToRead.push(item);
        }

        if (0 == typesToRead.length) return;

        let dataValues : Array<UaDataValue> = [];

        if (needReadAttributes) {
            let nodesToRead: Array<UaReadValueId> = [];

            for (let item of typesToRead) {
                nodesToRead.push(new UaReadValueId(
                    item.nodeId,
                    Attributes.IsAbstract));

                nodesToRead.push(new UaReadValueId(
                    item.nodeId,
                    Attributes.Description));

                if (item.nodeClass == NodeClass.VariableType) {
                    nodesToRead.push(new UaReadValueId(
                        item.nodeId,
                        Attributes.DataType));

                    nodesToRead.push(new UaReadValueId(
                        item.nodeId,
                        Attributes.ValueRank));
                }            
            }

            dataValues = await client.read(nodesToRead);
        }

        for (let i = 0; i < typesToRead.length; ++i) {   
            let isAbstract : boolean = false;
            let description: UaLocalizedText = undefined;
            let dataType = UaNodeId.nullNodeId;
            let valueRank = -1;

            if (needReadAttributes) {
                let dataIndex = (typesToRead[i].nodeClass == NodeClass.VariableType) ? i * 4 : i * 2;
                if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);

                isAbstract = dataValues[dataIndex].value.toBoolean();
                if (null == isAbstract) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
                dataIndex++;
                
                if (this._returnDescription && dataValues[dataIndex].statusCode.isGood()) {
                    description = dataValues[dataIndex].value.toLocalizedText();
                    if (null == description) description = undefined
                }
                dataIndex++;

                if (typesToRead[i].nodeClass == NodeClass.VariableType) {
                    if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);
                    dataType = dataValues[dataIndex].value.toNodeId();
                    dataIndex++;

                    if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);                
                    valueRank = dataValues[dataIndex].value.toNumber();
                    dataIndex++;

                    if (null == dataType || null == valueRank) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
                }
            }

            let typeNode: UaNode;

            if (typesToRead[i].nodeClass == NodeClass.ObjectType) {
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

            if (this._returnDescription && description) {
                typeNode.description = description;
            }

            typeNode.refToParent = typesToRead[i].referenceTypeId;
            nodes.set(typeNode.nodeId.toString(), typeNode);
        }
    }

    private async _readObjects(references: Array<UaReference>, nodes: Map<string, UaNode>, client: UaWebClient) {
        let objectsToRead: Array<UaReference> = [];

        for (let item of references) {
            if (item.nodeClass != NodeClass.Object) continue;
            objectsToRead.push(item);
        }

        if (0 == objectsToRead.length) return;

        let nodesToRead: Array<UaReadValueId> = [];

        for (let item of objectsToRead) {
            if (this._returnAttributes)
            {
                nodesToRead.push(new UaReadValueId(
                    item.nodeId,
                    Attributes.EventNotifier));
            }

            if (this._returnDescription) {
                nodesToRead.push(new UaReadValueId(
                    item.nodeId,
                    Attributes.Description));
            }
        }

        let dataValues : Array<UaDataValue>= [];
        
        if (nodesToRead.length != 0)
        {
            dataValues = await client.read(nodesToRead);
        }
        
        for (let i = 0; i < objectsToRead.length; ++i) {
            let dataIndex = (this._returnAttributes && this._returnDescription) ? i * 2 : i;

            let eventNotifier: number = 0;
            let description: UaLocalizedText = undefined;

            if (this._returnAttributes && dataValues[dataIndex].statusCode.isGood()) {
                eventNotifier = dataValues[dataIndex].value.toNumber();
                if (null == eventNotifier) eventNotifier = 0;
                dataIndex++;
            }

            if (this._returnDescription && dataValues[dataIndex].statusCode.isGood()) {
                description = dataValues[dataIndex].value.toLocalizedText();
                if (null == description) description = undefined;
                dataIndex++;
            }

            let objectNode = new UaObject(
                objectsToRead[i].nodeId,
                objectsToRead[i].browseName,
                objectsToRead[i].displayName,
                eventNotifier,
                objectsToRead[i].typeDefinitionId);

            objectNode.refToParent = objectsToRead[i].referenceTypeId;

            if (this._returnDescription && description) {
                objectNode.description = description;                
            }

            nodes.set(objectNode.nodeId.toString(), objectNode);
        }
    }

    private async _readVariables(references: Array<UaReference>, nodes: Map<string, UaNode>, client: UaWebClient) {
        let variablesToRead: Array<UaReference> = [];
        let nodeIds: Array<UaNodeId> = [];

        for (let item of references) {
            if (item.nodeClass != NodeClass.Variable) continue;
            variablesToRead.push(item);
            nodeIds.push(item.nodeId);
        }

        if (0 == nodeIds.length) return;
        
        let results : Array<UaVariableAttributes> = [];
        
        if (this._returnAttributes)
        {
            results = await client.readVariableAttributes(nodeIds);
        }
        
        let variablesToReadValue : Array<UaVariable> = [];

        for (let i = 0; i < variablesToRead.length; ++i) {
            let variableNode;
            
            if (this._returnAttributes) {            
                variableNode = new UaVariable(
                    variablesToRead[i].nodeId,
                    variablesToRead[i].browseName,
                    variablesToRead[i].displayName,
                    results[i].dataType,
                    results[i].valueRank,
                    results[i].accessLevel,
                    results[i].userAccessLevel,
                    results[i].historizing,
                    variablesToRead[i].typeDefinitionId);
            } else {
                variableNode = new UaVariable(
                    variablesToRead[i].nodeId,
                    variablesToRead[i].browseName,
                    variablesToRead[i].displayName,
                    UaNodeId.nullNodeId,
                    -1,
                    0,
                    0,
                    false,
                    variablesToRead[i].typeDefinitionId);
            }

            nodes.set(variableNode.nodeId.toString(), variableNode);            
            
            if (this._returnValue || (UaNodeReader.s_variablesNameToRead.has(variableNode.browseName))) {
                variablesToReadValue.push(variableNode);                
            }
        }

        if (variablesToReadValue.length != 0) {
            let valuesToRead: Array<UaNodeId> = [];

            for (let item of variablesToReadValue) {
                valuesToRead.push(item.nodeId);
            }

            let values = await client.readValues(valuesToRead);

            for (let i = 0; i < valuesToRead.length; ++i) {
                let variableNode = nodes.get(valuesToRead[i].toString());
                (variableNode as UaVariable).dataValue = values[i];
            }            
        }
    }

    private async _readMethods(references: Array<UaReference>, nodes: Map<string, UaNode>, client: UaWebClient) {        
        let methodsToRead: Array<UaReference> = [];

        for (let item of references) {
            if (item.nodeClass != NodeClass.Method) continue;
            methodsToRead.push(item);
        }

        if (0 == methodsToRead.length) return;

        let nodesToRead: Array<UaReadValueId> = [];

        if (this._returnDescription) {
            for (let item of methodsToRead) {
                nodesToRead.push(new UaReadValueId(
                    item.nodeId,
                    Attributes.Description));
            }
        }
    
        let dataValues : Array<UaDataValue>= [];        
        if (nodesToRead.length != 0)
        {
            dataValues = await client.read(nodesToRead);            
        }

        for (let i = 0; i < methodsToRead.length; ++i) {
            let methodNode = new UaMethod(
                methodsToRead[i].nodeId,
                methodsToRead[i].browseName,
                methodsToRead[i].displayName);

            if (this._returnDescription) {
                if (dataValues[i].statusCode.isGood()) {
                    let description = dataValues[i].value.toLocalizedText();
                    if (null == description) description = undefined;
                    methodNode.description = description;
                }
            }
                        
            nodes.set(methodNode.nodeId.toString(), methodNode);
        }
    }

    private _buildNodeTree(referenceResult : BrowseReferenceResult, nodes: Map<string, UaNode>) { 
        let parentNode = nodes.get(referenceResult.nodeId.toString());
        if (null == parentNode) return;

        for (let item of referenceResult.references) {
            let childNode = nodes.get(item.nodeId.toString());
            if (!childNode) continue;

            if (NodeClass.Object == parentNode.nodeClass) {
                let parent = parentNode as UaObject;

                if (NodeClass.Variable == childNode.nodeClass ||
                    NodeClass.Method == childNode.nodeClass) {
                    parent.addMember(childNode as UaInstanceNode);
                }
            } else if (NodeClass.Variable == parentNode.nodeClass) {
                let parent = parentNode as UaVariable;

                if (NodeClass.Variable == childNode.nodeClass) {
                    parent.addMember(childNode as UaVariable);
                }
            } else if (NodeClass.Method == parentNode.nodeClass) {
                let parent = parentNode as UaMethod;

                if (NodeClass.Variable == childNode.nodeClass) {
                    parent.addMember(childNode as UaVariable);
                }
            } else if (NodeClass.ObjectType == parentNode.nodeClass) {
                let parent = parentNode as UaObjectType;

                if (NodeClass.Variable == childNode.nodeClass ||
                    NodeClass.Method == childNode.nodeClass) {
                    parent.addMember(childNode as UaInstanceNode);
                }
            } else if (NodeClass.VariableType == parentNode.nodeClass) {
                let parent = parentNode as UaVariableType;

                if (NodeClass.Variable == childNode.nodeClass) {
                    parent.addMember(childNode as UaVariable);
                }
            } else if (NodeClass.DataType == parentNode.nodeClass) {
                let parent = parentNode as UaDataType;

                if (NodeClass.Variable == childNode.nodeClass) {                    
                    parent.setEnumVariable(childNode as UaVariable);
                }
            }
        }
    }
}

export class UaObjectReader extends UaNodeReader {
    constructor(        
        returnDescription?: boolean | null,
        returnValue?: boolean | null) {
        super(returnDescription ?? false, true, true, true, returnValue ?? false);
    }

    async read(nodeIds: Array<UaNodeId>, client: UaWebClient) : Promise<Array<UaNode>> {
        let nodes = await super.read(nodeIds, client);
        
        let objectNodes: Array<UaNode> = [];
        for (let item of nodes) {
            if (item.nodeClass == NodeClass.Object) {
                objectNodes.push(item);
            }
        }

        return objectNodes;
    }
}

export class UaTypeReader extends UaNodeReader {
    constructor() {
        super(true, true, true, true, false);
    }

    async read(nodeIds: Array<UaNodeId>, client: UaWebClient) : Promise<Array<UaNode>> {
        let nodes = await super.read(nodeIds, client);
        let typeNodes: Array<UaNode> = [];

        for (let item of nodes) {
            if (item.nodeClass == NodeClass.ObjectType ||
                item.nodeClass == NodeClass.VariableType ||
                item.nodeClass == NodeClass.DataType ||
                item.nodeClass == NodeClass.ReferenceType) {
                typeNodes.push(item);
            }
        }

        return typeNodes;
    }
}
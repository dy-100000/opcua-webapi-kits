import { Attributes, BrowseDirection, NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataType, UaError, UaInstanceNode, UaLocalizedText, UaNode, UaNodeId, UaObject, UaObjectType, UaVariableType, UaReferenceType, UaVariable, VariableTypeIds, parseUaNodeId, UaMethod, UaBrowseDescription, UaReadValueId, ReferenceTypeIds, UaReference, UaDataValue, OpcUaVariableAttributes, UaDefintionNode } from "../../common";
import { UaWebClient } from "../UaWebClient"
import { UaNodeBrowser,BrowseReferenceResult } from "./UaNodeBrowser";

export class UaNodeReader {
    private _client : UaWebClient;
    private _nodeClassToReturn: number;
    private _returnDescription: boolean;
    private _referenceTypeToReturn: UaNodeId;
    private _returnObjectAttributes: boolean;
    private _returnVariableAttributes: boolean;
    private _returnVariableValue: boolean;
    private _returnPropertyAttributes: boolean;
    private _returnPropertyValue: boolean;
    private static s_variableTypeWithoutMember: Set<string> = null;
    private static s_variablesNameToRead: Set<string> = null;

    constructor(
        client : UaWebClient,
        returnDescription: boolean,
        returnVariable: boolean,
        returnMethod: boolean,
        referenceTypeToReturn: UaNodeId,
        returnObjectAttributes: boolean,
        returnVariableAttributes: boolean,
        returnVariableValue: boolean,
        returnPropertyAttributes: boolean,
        returnPropertyValue: boolean)
    {        
        this._client = client;
        this._returnDescription = returnDescription;        
        this._referenceTypeToReturn = referenceTypeToReturn;
        this._returnObjectAttributes = returnObjectAttributes;
        this._returnVariableAttributes = returnVariableAttributes;
        this._returnVariableValue = returnVariableValue;
        this._returnPropertyAttributes = returnPropertyAttributes;
        this._returnPropertyValue = returnPropertyValue;

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

    async read(nodeIds: Array<UaNodeId>) : Promise<Array<UaNode>> {
        if (nodeIds.length == 0) return [];
        
        let nodeIdsToRead: Array<UaNodeId> = [];
        let nodeIdsSet: Set<string> = new Set();

        for (let item of nodeIds) {
            if (nodeIdsSet.has(item.toString())) continue;
            nodeIdsToRead.push(item);
            nodeIdsSet.add(item.toString());
        }

        let rootReferences = await this._readRootNodes(nodeIdsToRead);
        return await this.readByReferences(rootReferences);
    }

    async readByReferences(references: Array<UaReference>) : Promise<Array<UaNode>>
    {
        if (references.length == 0) return [];

        let nodeIdsToBrowse: Array<UaNodeId> = [];
        let nodeIdsSet: Set<string> = new Set();

        for (let item of references) {
            if (nodeIdsSet.has(item.nodeId.toString())) continue;
            nodeIdsToBrowse.push(item.nodeId);
            nodeIdsSet.add(item.nodeId.toString());
        }

        let nodeReferenceBrowser = new UaNodeBrowser(
            this._client,
            nodeIdsToBrowse,
            this._referenceTypeToReturn,
            this._nodeClassToReturn);

        await nodeReferenceBrowser.browse();
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
            let childReferenceBrowser = new UaNodeBrowser(
                this._client,
                childNodesToBrowse,
                UaNodeId.from(ReferenceTypeIds.Aggregates),
                NodeClass.Variable);

            await childReferenceBrowser.browse();
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
        await this._readNodes(allReferences, nodesMap);

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

    private async _readRootNodes(nodeIds: Array<UaNodeId>) : Promise<Array<UaReference>> {
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

        let dataValues = await this._client.read(nodesToRead);
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

            let results = await this._client.browse(browseDescriptions);

            for (let i = 0; i < browseDescriptions.length; ++i) {
                if (results[i].statusCode.isNotGood()) throw new UaError(results[i].statusCode);
                if (results[i].references.length != 1) continue;
                typeDefinitionsToRead[i].typeDefinitionId = results[i].references[0].nodeId.getNodeId();
            }
        }

        return references;
    }

    private async _readNodes(references: Array<UaReference>, nodes: Map<string,UaNode>) {
        if (references.length == 0) return;

        let refrencesToRead: Array<Array<UaReference>> = [];

        while (references.length != 0) {
            refrencesToRead.push(references.splice(0, 100));
        }
  
        for (let item of refrencesToRead) {
            await this._readTypes(item, nodes);
            await this._readObjects(item, nodes);
            await this._readVariables(item, nodes);
            await this._readMethods(item, nodes);
        }
    }

    private async _readTypes(references: Array<UaReference>, nodes: Map<string,UaNode>) {
        let typesToRead: Array<UaReference> = [];

        for (let item of references) {
            if (item.nodeClass != NodeClass.ObjectType &&
                item.nodeClass != NodeClass.DataType &&
                item.nodeClass != NodeClass.VariableType &&
                item.nodeClass != NodeClass.ReferenceType) continue;
            
            typesToRead.push(item);
        }

        if (0 == typesToRead.length) return;

        let dataValues : Array<UaDataValue> = [];
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

        dataValues = await this._client.read(nodesToRead);
        

        for (let i = 0; i < typesToRead.length; ++i) {   
            let isAbstract : boolean = false;
            let description: UaLocalizedText = undefined;
            let dataType = UaNodeId.nullNodeId;
            let valueRank = -1;

            let dataIndex = (typesToRead[i].nodeClass == NodeClass.VariableType) ? i * 4 : i * 2;
            if (dataValues[dataIndex].statusCode.isNotGood()) throw new UaError(dataValues[dataIndex].statusCode);

            isAbstract = dataValues[dataIndex].value.toBoolean();
            if (null == isAbstract) throw new UaError(makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid));
            dataIndex++;
            
            if (dataValues[dataIndex].statusCode.isGood()) {
                description = dataValues[dataIndex].value.toLocalizedText();
                if (null == description) description = undefined;
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

            if (description) {
                typeNode.description = description;
            }

            nodes.set(typeNode.nodeId.toString(), typeNode);
        }
    }

    private async _readObjects(references: Array<UaReference>, nodes: Map<string, UaNode>) {
        let objectsToRead: Array<UaReference> = [];

        for (let item of references) {
            if (item.nodeClass != NodeClass.Object) continue;
            objectsToRead.push(item);
        }

        if (0 == objectsToRead.length) return;

        let nodesToRead: Array<UaReadValueId> = [];

        for (let item of objectsToRead) {
            if (this._returnObjectAttributes)
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
            dataValues = await this._client.read(nodesToRead);
        }
        
        for (let i = 0; i < objectsToRead.length; ++i) {
            let dataIndex = (this._returnObjectAttributes && this._returnDescription) ? i * 2 : i;

            let eventNotifier: number = 0;
            let description: UaLocalizedText = undefined;

            if (this._returnObjectAttributes && dataValues[dataIndex].statusCode.isGood()) {
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

            if (this._returnDescription && description) {
                objectNode.description = description;                
            }

            nodes.set(objectNode.nodeId.toString(), objectNode);
        }
    }

    private async _readVariables(references: Array<UaReference>, nodes: Map<string, UaNode>) {
        let variablesToRead: Array<UaReference> = [];
        let nodeIdsToReadAttributes: Array<UaNodeId> = [];
        let variablesToReadValue : Array<UaVariable> = [];

        for (let item of references) {
            if (item.nodeClass != NodeClass.Variable) continue;
            variablesToRead.push(item);
        }

        if (0 == variablesToRead.length) return;

        let hasPropertyId = UaNodeId.from(ReferenceTypeIds.HasProperty);
        let attributeIndexMap = new Map<number, number>();
        for (let i=0; i<variablesToRead.length; ++i) {
            let item = variablesToRead[i];

            if (item.referenceTypeId.equal(hasPropertyId))
            {
                if (this._returnPropertyAttributes) 
                {
                    attributeIndexMap.set(i, nodeIdsToReadAttributes.length);
                    nodeIdsToReadAttributes.push(item.nodeId);                    
                }                                       
            } else {
                if (this._returnVariableAttributes) 
                {
                    attributeIndexMap.set(i, nodeIdsToReadAttributes.length);
                    nodeIdsToReadAttributes.push(item.nodeId);
                }                    
            }            
        }
        
        let results : Array<OpcUaVariableAttributes> = [];        
        if (nodeIdsToReadAttributes.length != 0)
        {
            results = await this._client.readVariableAttributes(nodeIdsToReadAttributes);
        }
        
        for (let i=0; i<variablesToRead.length; ++i) {
            let variableNode;
            let attributeIndex = attributeIndexMap.get(i);
            
            if (null != attributeIndex) {            
                variableNode = new UaVariable(
                    variablesToRead[i].nodeId,
                    variablesToRead[i].browseName,
                    variablesToRead[i].displayName,
                    results[attributeIndex].dataType,
                    results[attributeIndex].valueRank,
                    results[attributeIndex].accessLevel,
                    results[attributeIndex].userAccessLevel,
                    results[attributeIndex].historizing,
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
            
            if (variablesToRead[i].referenceTypeId.equal(hasPropertyId))
            {
                variableNode.isProperty = true;
            }

            if (this._returnVariableValue && !variableNode.isProperty)
            {
                variablesToReadValue.push(variableNode);
            } else if (this._returnPropertyValue && variableNode.isProperty) {
                variablesToReadValue.push(variableNode);
            } else if (UaNodeReader.s_variablesNameToRead.has(variableNode.browseName)) {
                variablesToReadValue.push(variableNode);                
            }
        }

        if (variablesToReadValue.length != 0) {
            let valuesToRead: Array<UaNodeId> = [];

            for (let item of variablesToReadValue) {
                valuesToRead.push(item.nodeId);
            }

            let values = await this._client.readValues(valuesToRead);

            for (let i = 0; i < valuesToRead.length; ++i) {
                let variableNode = nodes.get(valuesToRead[i].toString());
                (variableNode as UaVariable).dataValue = values[i];
            }            
        }
    }

    private async _readMethods(references: Array<UaReference>, nodes: Map<string, UaNode>) {        
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
            dataValues = await this._client.read(nodesToRead);            
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
        client : UaWebClient,
        returnVariable: boolean = true,
        returnMethod: boolean = true,
        returnDescription: boolean = false) {
        super(
            client,
            returnDescription,
            returnVariable,
            returnMethod,
            UaNodeId.from(ReferenceTypeIds.Aggregates),
            false,
            true,
            false,
            false,
            true
        );
    }

    async readByReferences(references: Array<UaReference>) : Promise<Array<UaNode>> {
        let objectReferences: Array<UaReference> = [];
        for (let item of references) {
            if (item.nodeClass == NodeClass.Object) {
                objectReferences.push(item);
            }
        }

        if (0 == objectReferences.length) return [];
        return await super.readByReferences(objectReferences);
    }
}

export class UaFullObjectReader extends UaNodeReader {
    constructor(      
        client : UaWebClient,
        returnDescription: boolean = true,
        returnValue: boolean = false) {
        super(
            client,
            returnDescription,
            true,
            true,
            UaNodeId.from(ReferenceTypeIds.Aggregates),
            true,
            true,
            returnValue,
            true,
            returnValue
        );
    }

    async readByReferences(references: Array<UaReference>) : Promise<Array<UaNode>> {
        let objectReferences: Array<UaReference> = [];
        for (let item of references) {
            if (item.nodeClass == NodeClass.Object) {
                objectReferences.push(item);
            }
        }

        if (0 == objectReferences.length) return [];
        return await super.readByReferences(objectReferences);
    }
}

export class UaTypeReader extends UaNodeReader {
    constructor(client: UaWebClient, returnDescription: boolean = false) {
        super(
            client,
            returnDescription,
            true, 
            true,
            UaNodeId.from(ReferenceTypeIds.Aggregates),
            false, 
            true,
            false,
            true,
            false
        );
    }

    async readByReferences(references: Array<UaReference>) : Promise<Array<UaNode>> {
        let typeReferences: Array<UaReference> = [];
        for (let item of references) {
            if (item.nodeClass == NodeClass.ObjectType ||
                item.nodeClass == NodeClass.VariableType ||
                item.nodeClass == NodeClass.DataType ||
                item.nodeClass == NodeClass.ReferenceType) {
                typeReferences.push(item);
            }
        }

        if (0 == typeReferences.length) return [];
        return await super.readByReferences(typeReferences);
    }
}
import { Attributes, NodeClass } from "opcua-webapi";
import { UaError, UaInstanceNode, UaLocalizedText, UaNode, UaNodeId, UaObject, UaObjectType, UaModellingRule, UaDeleteNodesItem, UaAddNodesItem, UaExpandedNodeId, UaVariant, UaWriteValue, ReferenceTypeIds } from "../../common";
import { UaWebClient } from "../UaWebClient"
import { UaObjectTypeDictionary, UaObjectAttributes, UaNodeBrowser } from "../..";

export class UaModelling {
    private _client : UaWebClient;

    constructor(client : UaWebClient) {
        this._client = client;
    }

    public async getObjectTypeToAdd(
        objectTypeId: UaNodeId,
        dictionary: UaObjectTypeDictionary) : Promise<Array<UaObjectType>> {        
        let objectType = await this._readObjectMembers(objectTypeId,dictionary);
        if (!objectType) return [];
        
        let ret : Array<UaObjectType> = [];
        let objectMembers = objectType.objectMembers;

        for (let item of objectMembers) {
            if (item.modellingRule != UaModellingRule.PlaceHolder) continue;
            let childObjectType = dictionary.getObjectType(item.typeDefinitionId);
            if (!childObjectType) continue;
            ret.push(childObjectType);
        }

        return ret;
    }

    public async getWriteMask(nodes: Array<UaNode>) : Promise<void> {
        let nodesToRead: Array<UaNodeId> = [];

        for (let node of nodes) {
            if (node.writeMask != null) continue;
            nodesToRead.push(node.nodeId);
        }

        if (0 == nodesToRead.length) return;

        let results = await this._client.readWriteMasks(nodesToRead); 
        
        let index = 0;
        for (let node of nodes) {
            if (node.writeMask != null) continue;
            node.writeMask = results[index];
            index++;
        }
    }

    public async addObject(
        parentNodeId: UaNodeId,
        objectTypeId: UaNodeId, 
        displayName: UaLocalizedText,
        browseName? : string) : Promise<UaNodeId> {
        let objectAttributes = new UaObjectAttributes(displayName);
        let extensionObject = objectAttributes.toExtensionObject();

        let nodeToAdd = new UaAddNodesItem(
            new UaExpandedNodeId(parentNodeId),
            NodeClass.Object,
            extensionObject,
            new UaExpandedNodeId(objectTypeId),
            null,
            browseName ?? null,
            null);
        
        let results = await this._client.addNodes([nodeToAdd]);
        if (results[0].statusCode.isNotGood()) throw new UaError(results[0].statusCode);
        let newNodeId = results[0].addedNodeId;
        return newNodeId;
    }

    public async deleteNode(nodeId: UaNodeId) : Promise<void> {
        let nodeToDelete = new UaDeleteNodesItem(nodeId);
        let results = await this._client.deleteNodes([nodeToDelete]);
        if (results[0].isNotGood()) throw new UaError(results[0]);   
           
    }

    public async rename(nodeId: UaNodeId, name: UaLocalizedText) : Promise<void> {
        let writeValue = new UaWriteValue(nodeId, UaVariant.localizedText(name), Attributes.DisplayName);
        let results = await this._client.write([writeValue]);
        if (results[0].isNotGood()) throw new UaError(results[0]);
    }

    public async setDescription(nodeId: UaNodeId, description: UaLocalizedText) : Promise<void> {
        let writeValue = new UaWriteValue(nodeId, UaVariant.localizedText(description), Attributes.Description);
        let results = await this._client.write([writeValue]);
        if (results[0].isNotGood()) throw new UaError(results[0]);
    }

    private async _readObjectMembers(objectTypeId: UaNodeId, dictionary: UaObjectTypeDictionary) : Promise<UaObjectType | null> {
        let objectType = dictionary.getObjectType(objectTypeId);
        if (!objectType) return null;
        if (objectType.isObjectMemberRead) return objectType;

        let typeMemberBrowser = new UaNodeBrowser(this._client, [objectType.nodeId], UaNodeId.from(ReferenceTypeIds.Aggregates), NodeClass.Object, false);
        
        await typeMemberBrowser.browse();
        let browseMemberResults = typeMemberBrowser.results();

        let instanceNodes : Map<string, UaInstanceNode> = new Map();
        for (let item of browseMemberResults) {
            for (let reference of item.references) {
                if (reference.nodeClass != NodeClass.Object) continue;
                instanceNodes.set(reference.nodeId.toString(), new UaObject(reference.nodeId, reference.browseName, reference.displayName, 0, reference.typeDefinitionId));
            }
        }

        let memberObjectIds : Array<UaNodeId> = [];
        for (let node of instanceNodes) {
            memberObjectIds.push(node[1].nodeId);
        }

        let modellingRuleBrowser = new UaNodeBrowser(this._client, memberObjectIds, UaNodeId.from(ReferenceTypeIds.HasModellingRule), NodeClass.Object, false);
        await modellingRuleBrowser.browse();
        let browseModellingRuleResults = modellingRuleBrowser.results();

        for (let item of browseModellingRuleResults) {
            let instanceNode = instanceNodes.get(item.nodeId.toString());
            if (!instanceNode) continue;
            if (0 == item.references.length) continue;
            instanceNode.setModellingRule(item.references[0].nodeId);
        }

        for (let node of instanceNodes) {
            if (node[1].modellingRule == UaModellingRule.None) continue;            
            objectType.addMember(node[1]);
        }

        objectType.isObjectMemberRead = true;
        return objectType;
    }
}
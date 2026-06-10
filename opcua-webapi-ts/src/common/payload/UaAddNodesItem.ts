import { AddNodesItem, NodeClass } from "opcua-webapi";
import { parseUaExpandedNodeIdOrNull, parseUaNodeIdOrNull, UaExpandedNodeId, UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";
import { UaPayloadMapper } from "../..";

export class UaAddNodesItem 
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.AddNodesItem);

    private _parentNodeId : UaExpandedNodeId;
    private _nodeClass : NodeClass;
    private _nodeAttributes : UaExtensionObject;
    private _typeDefinition : UaExpandedNodeId | null;
    private _requestedNewNodeId : UaExpandedNodeId | null;
    private _browseName : string | null;
    private _referenceTypeId : UaNodeId | null;
    
    constructor(
        parentNodeId : UaExpandedNodeId,
        nodeClass : NodeClass,
        nodeAttributes : UaExtensionObject,
        typeDefinition? : UaExpandedNodeId | null,
        requestedNewNodeId? : UaExpandedNodeId | null,
        browseName? : string | null,
        referenceTypeId? : UaNodeId | null)
    {        
        this._parentNodeId = parentNodeId;
        this._nodeClass = nodeClass;
        this._nodeAttributes = nodeAttributes;
        this._typeDefinition = (undefined != typeDefinition) ? typeDefinition : null;
        this._requestedNewNodeId = (undefined != requestedNewNodeId) ? requestedNewNodeId : null;
        this._browseName = (undefined != browseName) ? browseName : null;
        this._referenceTypeId = (undefined != referenceTypeId) ? referenceTypeId : null;
    }

    get parentNodeId() : UaExpandedNodeId
    {
        return this._parentNodeId;
    }

    get nodeClass() : NodeClass
    {
        return this._nodeClass;
    }  

    get nodeAttributes() : UaExtensionObject
    {
        return this._nodeAttributes;
    }

    get typeDefinition() : UaExpandedNodeId | null
    {
        return this._typeDefinition;
    }

    get requestedNewNodeId() : UaExpandedNodeId | null
    {
        return this._requestedNewNodeId;
    }

    get browseName() : string | null
    {
        return this._browseName;
    }

    get referenceTypeId() : UaNodeId | null
    {
        return this._referenceTypeId;
    }

    toStruct() : AddNodesItem
    {
        let addNodesItem : AddNodesItem = {
            ParentNodeId: this._parentNodeId.toString(),
            NodeClass: this._nodeClass,
            NodeAttributes: UaPayloadMapper.extensionObjectToWebApi(this._nodeAttributes),
            TypeDefinition: (this._typeDefinition) ? this._typeDefinition.toString() : undefined,
            RequestedNewNodeId: (this._requestedNewNodeId) ? this._requestedNewNodeId.toString() : undefined,
            BrowseName: (this._browseName) ? this._browseName : undefined,
            ReferenceTypeId: (this._referenceTypeId) ? this._referenceTypeId.toString() : undefined
        };
        return addNodesItem;
    }

    static fromStruct(addNodesItem : AddNodesItem) : UaAddNodesItem | null
    {
        if (addNodesItem.ParentNodeId === undefined || addNodesItem.NodeClass === undefined || addNodesItem.NodeAttributes === undefined) return null;

        let parentNodeId = parseUaExpandedNodeIdOrNull(addNodesItem.ParentNodeId);
        let nodeClass = (typeof addNodesItem.NodeClass === "number") ? addNodesItem.NodeClass : null;
        let nodeAttributes = UaPayloadMapper.extensionObjectFromWebApi(addNodesItem.NodeAttributes);
        let typeDefinition = (addNodesItem.TypeDefinition) ? parseUaExpandedNodeIdOrNull(addNodesItem.TypeDefinition) : null;
        let requestedNewNodeId = (addNodesItem.RequestedNewNodeId) ? parseUaExpandedNodeIdOrNull(addNodesItem.RequestedNewNodeId) : null;
        let browseName = (typeof addNodesItem.BrowseName === "string") ? addNodesItem.BrowseName : null;
        let referenceTypeId = (addNodesItem.ReferenceTypeId) ? parseUaNodeIdOrNull(addNodesItem.ReferenceTypeId) : null;

        if (null == parentNodeId || null == nodeClass || null == nodeAttributes) return null;

        return new UaAddNodesItem(parentNodeId, nodeClass as NodeClass, nodeAttributes, typeDefinition, requestedNewNodeId, browseName, referenceTypeId);
    }
}
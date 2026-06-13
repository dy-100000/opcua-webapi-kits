import { AddReferencesItem, NodeClass } from "opcua-webapi";
import { parseUaExpandedNodeIdOrNull, parseUaNodeIdOrNull, UaExpandedNodeId, UaNodeId, UaStatusCode } from "../types";
import { DataTypeIds } from "../nodes";

export class UaAddReferencesItem
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.AddReferencesItem);
    
    private _sourceNodeId: UaNodeId;
    private _targetNodeId: UaExpandedNodeId;
    private _isForward: boolean;    
    private _referenceTypeId: UaNodeId;    
    private _targetNodeClass: NodeClass;

    constructor(
        sourceNodeId: UaNodeId,
        targetNodeId: UaExpandedNodeId,
        isForward: boolean,
        referenceTypeId: UaNodeId,
        targetNodeClass: NodeClass)
    {
        this._sourceNodeId = sourceNodeId;
        this._targetNodeId = targetNodeId;
        this._isForward = isForward;
        this._referenceTypeId = referenceTypeId;
        this._targetNodeClass = targetNodeClass;
    }

    get sourceNodeId() : UaNodeId
    {
        return this._sourceNodeId;
    }

    get targetNodeId() : UaExpandedNodeId
    {
        return this._targetNodeId;
    }

    get isForward() : boolean
    {
        return this._isForward;
    }

    get referenceTypeId() : UaNodeId
    {
        return this._referenceTypeId;
    }

    get targetNodeClass() : NodeClass
    {
        return this._targetNodeClass;
    }

    toStruct() : AddReferencesItem
    {        
        let addReferencesItem : AddReferencesItem = {
            SourceNodeId: this._sourceNodeId.toString(),
            TargetNodeId: this._targetNodeId.toString(),
            IsForward: this._isForward,
            ReferenceTypeId: this._referenceTypeId.toString(),
            TargetNodeClass: this._targetNodeClass
        };
        return addReferencesItem;
    }

    static fromStruct(addReferencesItem : AddReferencesItem) : UaAddReferencesItem | null
    {
        if (addReferencesItem.SourceNodeId === undefined || addReferencesItem.TargetNodeId === undefined) return null;

        let sourceNodeId = parseUaNodeIdOrNull(addReferencesItem.SourceNodeId);
        let targetNodeId = parseUaExpandedNodeIdOrNull(addReferencesItem.TargetNodeId);
        let isForward = (undefined != addReferencesItem.IsForward) ? addReferencesItem.IsForward : true;
        let referenceTypeId = (undefined != addReferencesItem.ReferenceTypeId) ? parseUaNodeIdOrNull(addReferencesItem.ReferenceTypeId) : undefined;
        let targetNodeClass = (undefined != addReferencesItem.TargetNodeClass) ? addReferencesItem.TargetNodeClass : NodeClass.Unspecified;
        if (sourceNodeId === null || targetNodeId === null || referenceTypeId === undefined) return null;
        return new UaAddReferencesItem(sourceNodeId, targetNodeId, isForward, referenceTypeId, targetNodeClass as NodeClass);
    }
}

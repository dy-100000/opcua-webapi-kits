import { DeleteReferencesItem } from "opcua-webapi";
import { parseUaExpandedNodeIdOrNull, parseUaNodeIdOrNull, UaExpandedNodeId, UaNodeId, UaStatusCode } from "../types";
import { DataTypeIds } from "../nodes";

export class UaDeleteReferencesItem
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.DeleteReferencesItem);
    
    private _sourceNodeId: UaNodeId;
    private _targetNodeId: UaExpandedNodeId;
    private _isForward: boolean;    
    private _referenceTypeId: UaNodeId;
    private _deleteBidirectional: boolean;

    constructor(
        sourceNodeId: UaNodeId,
        targetNodeId: UaExpandedNodeId,
        isForward: boolean,
        referenceTypeId: UaNodeId,
        deleteBidirectional: boolean = true)
    {
        this._sourceNodeId = sourceNodeId;
        this._targetNodeId = targetNodeId;
        this._isForward = isForward;
        this._referenceTypeId = referenceTypeId;
        this._deleteBidirectional = deleteBidirectional;
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

    get deleteBidirectional() : boolean
    {
        return this._deleteBidirectional;
    }

    toStruct() : DeleteReferencesItem
    {
        return {
            SourceNodeId: this._sourceNodeId.toString(),
            TargetNodeId: this._targetNodeId.toString(),
            IsForward: this._isForward,
            ReferenceTypeId: this._referenceTypeId.toString(),
            DeleteBidirectional: this._deleteBidirectional
        };
    }

    static fromStruct(item : DeleteReferencesItem) : UaDeleteReferencesItem | null
    {
        if (!item.SourceNodeId || !item.TargetNodeId) return null;

        let sourceNodeId = parseUaNodeIdOrNull(item.SourceNodeId);
        if (!sourceNodeId) return null;

        let targetNodeId = parseUaExpandedNodeIdOrNull(item.TargetNodeId);
        if (!targetNodeId) return null;

        let referenceTypeId = (item.ReferenceTypeId) ? parseUaNodeIdOrNull(item.ReferenceTypeId) : UaNodeId.nullNodeId;
        if (!referenceTypeId) return null;

        let isForward = (item.IsForward !== undefined) ? item.IsForward : true;
        let deleteBidirectional = (item.DeleteBidirectional !== undefined) ? item.DeleteBidirectional : true;

        return new UaDeleteReferencesItem(
            sourceNodeId, 
            targetNodeId, 
            isForward, 
            referenceTypeId, 
            deleteBidirectional);
    }
}

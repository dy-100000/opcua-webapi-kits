import { ReadValueId } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { parseUaNodeIdOrNull, UaNodeId } from "../types";

export class UaReadValueId
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ReadValueId);

    private _nodeId: UaNodeId;
    private _attributeId: number;
    private _indexRange?: string;

    constructor(
        nodeId: UaNodeId,
        attributeId: number,
        indexRange?: string)
    {
        this._nodeId = nodeId;
        this._attributeId = attributeId;
        this._indexRange = indexRange;
    }

    get nodeId() : UaNodeId
    {
        return this._nodeId;
    }

    get attributeId() : number
    {
        return this._attributeId;
    }

    get indexRange() : string | undefined
    {
        return this._indexRange;
    }

    toStruct() : ReadValueId
    {
        let readValueId : ReadValueId = {
            NodeId: this._nodeId.toString(),
            AttributeId: this._attributeId,
            IndexRange: this._indexRange
        };

        return readValueId;
    }

    static fromStruct(readValueId : ReadValueId) : UaReadValueId | null
    {
        let nodeId = parseUaNodeIdOrNull(readValueId.NodeId); 
        let attributeId = readValueId.AttributeId;
        let indexRange = readValueId.IndexRange;
        
        if (null == nodeId || null == attributeId) return null;

        return new UaReadValueId(
            nodeId, 
            attributeId, 
            indexRange);
    }
}
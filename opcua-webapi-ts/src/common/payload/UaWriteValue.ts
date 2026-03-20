import { Attributes, WriteValue } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { parseUaNodeIdOrNull, UaNodeId, UaVariant } from "../types";
import { UaPayloadMapper } from ".";

export class UaWriteValue
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.WriteValue);

    private _nodeId: UaNodeId;
    private _value: UaVariant;
    private _attributeId: number;    
    private _indexRange?: string;

    constructor(
        nodeId: UaNodeId,
        value: UaVariant,
        attributeId?: number,
        indexRange?: string)
    {
        this._nodeId = nodeId;
        this._value = value;
        this._attributeId = (attributeId) ? attributeId : Attributes.Value;
        this._indexRange = indexRange;
    }

    get nodeId() : UaNodeId
    {
        return this._nodeId;
    }

    get value() : UaVariant
    {
        return this._value;
    }

    get attributeId() : number
    {
        return this._attributeId;
    }

    get indexRange() : string | undefined
    {
        return this._indexRange;
    }

    toStruct() : WriteValue
    {
        let writeValue : WriteValue = {
            NodeId: this._nodeId.toString(),
            Value: UaPayloadMapper.variantToWebApi(this._value),
            AttributeId: this._attributeId,
            IndexRange: this._indexRange
        };

        return writeValue;
    }

    static fromStruct(writeValueId : WriteValue) : UaWriteValue | null
    {
        if (!writeValueId.Value || !writeValueId.AttributeId) return null;

        let nodeId = parseUaNodeIdOrNull(writeValueId.NodeId); 
        let value = UaPayloadMapper.variantFromWebApi(writeValueId.Value);
        let attributeId = writeValueId.AttributeId;
        let indexRange = writeValueId.IndexRange;
        
        if (null == nodeId) return null;

        return new UaWriteValue(
            nodeId, 
            value, 
            attributeId, 
            indexRange);
    }
}
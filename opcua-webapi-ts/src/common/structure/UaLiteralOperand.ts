import { LiteralOperand, LiteralOperandFromJSON, LiteralOperandToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId, UaVariant } from "../types";
import { UaPayloadMapper } from "../payload";
import { DataTypeIds } from "../nodes";

export class UaLiteralOperand
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.LiteralOperand);
    
    private _value: UaVariant;
    
    constructor(value: UaVariant)
    {        
        this._value = value;    
    }

    get value() : UaVariant
    {
        return this._value;
    }

    toStruct() : LiteralOperand
    {
        let operand : LiteralOperand = { 
            Value: UaPayloadMapper.variantToWebApi(this._value)
        };

        return operand;
    }

    static fromStruct(operand : LiteralOperand) : UaLiteralOperand | null
    {
        if (null == operand.Value) return null;     
        
        try
        {
            let value = UaPayloadMapper.variantFromWebApi(operand.Value);
            return new UaLiteralOperand(value);
        } catch(e) {
            return null;
        }
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaLiteralOperand.dataTypeId, LiteralOperandToJSONTyped(this.toStruct()));
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaLiteralOperand | null
    {
        if (!UaLiteralOperand.dataTypeId.equal(extensionObject.typeId)) return null;
        let operand : LiteralOperand = LiteralOperandFromJSON(extensionObject.body);
        return UaLiteralOperand.fromStruct(operand);
    }
}
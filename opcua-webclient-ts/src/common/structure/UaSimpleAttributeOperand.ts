import { SimpleAttributeOperand, SimpleAttributeOperandFromJSON, SimpleAttributeOperandToJSONTyped } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";

export class UaSimpleAttributeOperand
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.SimpleAttributeOperand);
    
    private _browsePath: Array<string>;
    private _typeDefinitionId: UaNodeId;
    
    constructor(
        browsePath: Array<string>,
        typeDefinitionId? : UaNodeId | null)
    {        
        this._browsePath = browsePath;
        this._typeDefinitionId = (typeDefinitionId) ? typeDefinitionId : UaNodeId.nullNodeId;        
    }

    get browsePath() : Array<string>
    {
        return this._browsePath;
    }

    get typeDefinitionId() : UaNodeId
    {
        return this._typeDefinitionId;
    }

    toStruct() : SimpleAttributeOperand
    {
        let operand : SimpleAttributeOperand = { 
            BrowsePath: this._browsePath,
            TypeDefinitionId: (!this._typeDefinitionId.isEmpty()) ? this._typeDefinitionId.toString() : undefined
        };

        return operand;
    }

    static fromStruct(operand : SimpleAttributeOperand) : UaSimpleAttributeOperand | null
    {
        if (null == operand.BrowsePath) return null;
        let browsePath = operand.BrowsePath;
        let dataTypeId = parseUaNodeIdOrNull(operand.TypeDefinitionId);

        return new UaSimpleAttributeOperand(browsePath, dataTypeId);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaSimpleAttributeOperand.dataTypeId, SimpleAttributeOperandToJSONTyped(this.toStruct()));
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaSimpleAttributeOperand | null
    {
        if (!UaSimpleAttributeOperand.dataTypeId.equal(extensionObject.typeId)) return null;
        let operand : SimpleAttributeOperand = SimpleAttributeOperandFromJSON(extensionObject.body);
        return UaSimpleAttributeOperand.fromStruct(operand);
    }
}
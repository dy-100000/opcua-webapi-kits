import { ElementOperand, ElementOperandFromJSON, ElementOperandToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";

export class UaElementOperand
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ElementOperand);
    
    private _index: number;
    
    constructor(index: number)
    {        
        this._index = index;    
    }

    get index() : number
    {
        return this._index;
    }
    
    toStruct() : ElementOperand
    {
        let operand : ElementOperand = { 
            _Index: this._index
        };

        return operand;
    }

    static fromStruct(operand : ElementOperand) : UaElementOperand | null
    {
        if (null == operand._Index) return null;
        return new UaElementOperand(operand._Index); 
    }

    toExtensionObject() : UaExtensionObject
    {       
        return new UaExtensionObject(UaElementOperand.dataTypeId, ElementOperandToJSONTyped(this.toStruct()));
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaElementOperand | null
    {
        if (!UaElementOperand.dataTypeId.equal(extensionObject.typeId)) return null;
        let operand : ElementOperand = ElementOperandFromJSON(extensionObject.body);
        return UaElementOperand.fromStruct(operand);            
    }
}
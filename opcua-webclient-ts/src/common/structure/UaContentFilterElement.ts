import { ContentFilterElement, ExtensionObject } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";
import { UaPayloadMapper } from "../mapper";

export class UaContentFilterElement
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ContentFilterElement);
    
    private _operands: Array<UaExtensionObject>;
    private _operator: number;
    
    constructor(
        operands: Array<UaExtensionObject>,
        operator: number)
    {        
        this._operands = operands;
        this._operator = operator;   
    }

    get operands() : Array<UaExtensionObject>
    {
        return this._operands;
    }

    get operator() : number
    {
        return this.operator;
    }

    toStruct() : ContentFilterElement
    {
        let filterOperands : Array<ExtensionObject> = [];

        for (let item of this._operands)
        {
            filterOperands.push(UaPayloadMapper.extensionObjectToWebApi(item));
        }

        let element : ContentFilterElement = {
            FilterOperands: filterOperands,
            FilterOperator: this._operator
        };

        return element;
    }

    static fromStruct(element : ContentFilterElement) : UaContentFilterElement | null
    {
        if (undefined == element.FilterOperands || undefined == element.FilterOperator) return null;
        
        let operands: Array<UaExtensionObject> = [];

        for (let item of element.FilterOperands)
        {
            let operand = UaPayloadMapper.extensionObjectFromWebApi(item);
            if (null == operand) return null;
            operands.push(operand);
        }

        return new UaContentFilterElement(operands, element.FilterOperator);
    }
}
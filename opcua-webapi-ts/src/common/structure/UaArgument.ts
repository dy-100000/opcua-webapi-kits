import { Argument, ArgumentFromJSON, ArgumentToJSONTyped } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";
import { UaPayloadMapper } from "../payload";
import { DataTypeIds } from "../nodes";

export class UaArgument
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.Argument);

    private _name : string;
    private _dataType : UaNodeId;
    private _valueRank : number;
    private _description : UaLocalizedText | null;
    
    constructor(
        name : string,
        dataType: UaNodeId,
        valueRank?: number | null,
        description?: UaLocalizedText | null)
    {        
        this._name = name;
        this._dataType = dataType;
        this._valueRank = (undefined != valueRank) ? valueRank : -1;
        this._description = (description) ? description : null;
    }

    get name() : string
    {
        return this._name;
    }

    get dataType() : UaNodeId
    {
        return this._dataType;
    }

    get valueRank() : number
    {
        return this._valueRank;
    }

    get description() : UaLocalizedText | null
    {
        return this._description;
    }

    toStruct() : Argument
    {
        let argument : Argument = { 
            Name: this._name,
            DataType: this._dataType.toString(),
            ValueRank: this._valueRank,
            Description: (this._description) ? UaPayloadMapper.localizedTextToWebApi(this._description) : undefined
        };

        return argument;
    }

    static fromStruct(argument : Argument) : UaArgument | null
    {
        let name = (typeof argument.Name === "string") ? argument.Name : null;
        let dataTypeId = parseUaNodeIdOrNull(argument.DataType);        
        let valueRank = (typeof argument.ValueRank === "number") ? argument.ValueRank : -1;
        let description = (argument.Description) ? UaPayloadMapper.localizedTextFromWebApi(argument.Description) : null;

        if (null == dataTypeId || null == name) return null;

        return new UaArgument(name, dataTypeId, valueRank, description);
    }

    toExtensionObject() : UaExtensionObject
    {   
        return new UaExtensionObject(UaArgument.dataTypeId, ArgumentToJSONTyped(this.toStruct()));
    } 

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaArgument | null
    {
        if (!UaArgument.dataTypeId.equal(extensionObject.typeId)) return null;
        let argument : Argument = ArgumentFromJSON(extensionObject.body);
        return UaArgument.fromStruct(argument);
    }

    toJson() : any
    {
        let ret = {
            name: this._name,
            dataType: this._dataType.toString(),
            valueRank: this._valueRank,
            description: (this._description) ? this._description.text : undefined
        }

        return ret;
    }
}
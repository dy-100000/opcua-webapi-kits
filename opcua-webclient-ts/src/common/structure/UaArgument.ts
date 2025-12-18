import { Argument, ArgumentFromJSON } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";
import { UaPayloadMapper } from "../mapper";
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
        this._valueRank = (valueRank) ? valueRank : -1;
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

    toExtensionObject() : UaExtensionObject
    {
        let argument : Argument = { 
            Name: this._name,
            DataType: this._dataType.toString(),
            ValueRank: this._valueRank,
            Description: (this._description) ? UaPayloadMapper.localizedTextToWebApi(this._description) : undefined
        };

        return new UaExtensionObject(UaArgument.dataTypeId, argument);
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaArgument | null
    {
        if (!UaArgument.dataTypeId.equal(extensionObject.typeId)) return null;
        let argument : Argument = ArgumentFromJSON(extensionObject.body);

        let name = (typeof argument.Name === "string") ? argument.Name : null;
        let dataTypeId = parseUaNodeIdOrNull(argument.DataType);        
        let valueRank = (typeof argument.ValueRank === "number") ? argument.ValueRank : -1;
        let description = (argument.Description) ? UaPayloadMapper.localizedTextFromWebApi(argument.Description) : null;

        if (null == dataTypeId || null == name) return null;

        return new UaArgument(name, dataTypeId, valueRank, description);
    }
}
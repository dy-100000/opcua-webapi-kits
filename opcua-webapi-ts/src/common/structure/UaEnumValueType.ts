import { EnumValueType, EnumValueTypeFromJSON, EnumValueTypeToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";
import { UaPayloadMapper } from "../payload";
import { DataTypeIds } from "../nodes";

export class UaEnumValueType
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.EnumValueType);

    private _value : number;
    private _displayName : UaLocalizedText;
    private _description : UaLocalizedText | null;
    
    constructor(
        value : number,
        displayName: UaLocalizedText,
        description: UaLocalizedText | null)
    {        
        this._value = value;
        this._displayName = displayName;
        this._description = description;
    }

    get value() : number
    {
        return this._value;
    }

    get displayName() : UaLocalizedText
    {
        return this._displayName;
    }

    get description() : UaLocalizedText | null
    {
        return this._description;
    }

    toStruct() : EnumValueType
    {
        let enumValue : EnumValueType = { 
            Value: this._value,
            DisplayName: UaPayloadMapper.localizedTextToWebApi(this._displayName),
            Description: (null !== this._description) ? UaPayloadMapper.localizedTextToWebApi(this._description) : undefined
        };
    
        return enumValue;
    }
    
    static fromStruct(enumValueType : EnumValueType) : UaEnumValueType | null
    {        
        let value = enumValueType.Value;
        let displayName = UaPayloadMapper.localizedTextFromWebApi(enumValueType.DisplayName);        
        let description = UaPayloadMapper.localizedTextFromWebApi(enumValueType.Description);

        if (typeof value !== "number" || null == displayName) return null;
        return new UaEnumValueType(value, displayName, description);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaEnumValueType.dataTypeId, EnumValueTypeToJSONTyped(this.toStruct()));
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaEnumValueType | null
    {
        if (!UaEnumValueType.dataTypeId.equal(extensionObject.typeId)) return null;
        let enumValueType : EnumValueType = EnumValueTypeFromJSON(extensionObject.body);
        return UaEnumValueType.fromStruct(enumValueType);        
    }
}
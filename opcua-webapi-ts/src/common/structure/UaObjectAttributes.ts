import { ObjectAttributes, NodeClass, LocalizedText, ObjectAttributesToJSONTyped, ObjectAttributesFromJSON } from "opcua-webapi";
import { UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";
import { UaPayloadMapper } from "../..";

export class UaObjectAttributes
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ObjectAttributes);    
    
    private _displayName: UaLocalizedText;
    private _eventNotifier: number | null;
    private _description: UaLocalizedText | null;
    private _writeMask: number | null;
    private _userWriteMask: number | null;

    constructor(        
        displayName: UaLocalizedText,
        eventNotifier?: number | null,
        description? : UaLocalizedText | null,
        writeMask? : number | null,
        userWriteMask? : number | null)
    {        
        this._displayName = displayName;
        this._eventNotifier = (undefined != eventNotifier) ? eventNotifier : null;
        this._description = (undefined != description) ? description : null;
        this._writeMask = (undefined != writeMask) ? writeMask : null;
        this._userWriteMask = (undefined != userWriteMask) ? userWriteMask : null;
    }

    get displayName() : UaLocalizedText
    {
        return this._displayName;
    }

    get eventNotifier() : number | null
    {
        return this._eventNotifier;
    }

    get description() : UaLocalizedText | null
    {
        return this._description;
    }

    get writeMask() : number | null
    {
        return this._writeMask;
    }

    get userWriteMask() : number | null
    {
        return this._userWriteMask;
    }

    toStruct() : ObjectAttributes
    {
        let struct : ObjectAttributes = {
            DisplayName: UaPayloadMapper.localizedTextToWebApi(this._displayName),
            EventNotifier: (null != this._eventNotifier) ? this._eventNotifier : undefined,
            Description: (this._description) ? UaPayloadMapper.localizedTextToWebApi(this._description) : undefined,
            WriteMask: (null != this._writeMask) ? this._writeMask : undefined,
            UserWriteMask: (null != this._userWriteMask) ? this._userWriteMask : undefined
        };
        return struct;
    }

    static fromStruct(struct : ObjectAttributes) : UaObjectAttributes | null
    {
        if (struct.DisplayName === undefined) return null;
        let displayName = UaPayloadMapper.localizedTextFromWebApi(struct.DisplayName);
        let eventNotifier = (undefined != struct.EventNotifier) ? struct.EventNotifier : null;
        let description = (undefined != struct.Description) ? UaPayloadMapper.localizedTextFromWebApi(struct.Description) : null;
        let writeMask = (undefined != struct.WriteMask) ? struct.WriteMask : null;
        let userWriteMask = (undefined != struct.UserWriteMask) ? struct.UserWriteMask : null;
        return new UaObjectAttributes(displayName, eventNotifier, description, writeMask, userWriteMask);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaObjectAttributes.dataTypeId,ObjectAttributesToJSONTyped(this.toStruct()));
    }

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaObjectAttributes | null
    {
        if (!UaObjectAttributes.dataTypeId.equal(extensionObject.typeId)) return null;      
        let struct : ObjectAttributes = ObjectAttributesFromJSON(extensionObject.body);
        return UaObjectAttributes.fromStruct(struct);
    }
}
import { EUInformation, EUInformationFromJSON } from "opcua-webapi";
import { UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";
import { UaPayloadMapper } from "../mapper";
import { DataTypeIds } from "../nodes";

export class UaEUInformation
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.EUInformation);

    private _unitId : number;
    private _displayName : UaLocalizedText;
    private _description : UaLocalizedText | null;
    private _namespaceUri : string | null;
    
    constructor(
        unitId : number,
        displayName: UaLocalizedText,
        description?: UaLocalizedText | null,
        namespaceUri?: string | null)
    {        
        this._unitId = unitId;
        this._displayName = displayName;
        this._description = (description) ? description : null;
        this._namespaceUri = (namespaceUri) ? namespaceUri : null;
    }

    get unitId() : number
    {
        return this._unitId;
    }

    get displayName() : UaLocalizedText | null
    {
        return this._displayName;
    }

    get description() : UaLocalizedText | null
    {
        return this._description;
    }

    get namespaceUri() : string | null
    {
        return this._namespaceUri;
    }

    toExtensionObject() : UaExtensionObject
    {
        let euInformation : EUInformation = { 
            UnitId: this._unitId,
            DisplayName: UaPayloadMapper.localizedTextToWebApi(this._displayName),
            Description: (this._description) ? UaPayloadMapper.localizedTextToWebApi(this._description) : undefined,
            NamespaceUri: (this._namespaceUri) ? this._namespaceUri : undefined
        };

        return new UaExtensionObject(UaEUInformation.dataTypeId, euInformation);
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaEUInformation | null
    {
        if (!UaEUInformation.dataTypeId.equal(extensionObject.typeId)) return null;      
        let euInformation : EUInformation = EUInformationFromJSON(extensionObject.body);

        let unitId = (typeof euInformation.UnitId === "number") ? euInformation.UnitId : null;
        let displayName = UaPayloadMapper.localizedTextFromWebApi(euInformation.DisplayName);        
        let description = (euInformation.Description) ? UaPayloadMapper.localizedTextFromWebApi(euInformation.Description) : null;  
        let namespaceUri = (typeof euInformation.NamespaceUri === "string") ? euInformation.NamespaceUri : null;

        if (null == unitId || null == displayName) return null;

        return new UaEUInformation(unitId, displayName, description, namespaceUri);
    }
}
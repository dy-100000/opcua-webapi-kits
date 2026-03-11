import { ReadAtTimeDetails, ReadAtTimeDetailsFromJSON, ReadAtTimeDetailsToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";

export class UaReadAtTimeDetails
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ReadAtTimeDetails);

    private _reqTimes: Array<Date>;
    private _useSimpleBounds: boolean;

    constructor(
            reqTimes : Array<Date>,
            useSimpleBounds?: boolean | null)
    {        
        this._reqTimes = reqTimes;
        this._useSimpleBounds = (useSimpleBounds) ? true : false;
    }
    
    get reqTimes()
    {
        return this._reqTimes;
    }

    get useSimpleBounds()
    {
        return this._useSimpleBounds;
    }

    toStruct() : ReadAtTimeDetails
    {
        let details : ReadAtTimeDetails = { 
            ReqTimes: this.reqTimes,
            UseSimpleBounds: this.useSimpleBounds
        };

        return details;
    }

    static fromStruct(details : ReadAtTimeDetails) : UaReadAtTimeDetails | null
    {
        if (!details.ReqTimes) return null;
        return new UaReadAtTimeDetails(details.ReqTimes, details.UseSimpleBounds);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaReadAtTimeDetails.dataTypeId, ReadAtTimeDetailsToJSONTyped(this.toStruct()));
    }

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaReadAtTimeDetails | null
    {
        if (!UaReadAtTimeDetails.dataTypeId.equal(extensionObject.typeId)) return null;      
        let details : ReadAtTimeDetails = ReadAtTimeDetailsFromJSON(extensionObject.body);
        return UaReadAtTimeDetails.fromStruct(details);
    }
}
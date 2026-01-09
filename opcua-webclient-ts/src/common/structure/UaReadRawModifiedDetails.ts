import { ReadRawModifiedDetails, ReadRawModifiedDetailsFromJSON, ReadRawModifiedDetailsToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";

export class UaReadRawModifiedDetails
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ReadRawModifiedDetails);

    private _startTime: Date;
    private _endTime: Date;
    private _numValuesPerNode: number;
    private _returnBounds: boolean;
    private _isReadModified: boolean;

    constructor(
            startTime : Date,
            endTime: Date,
            numValuesPerNode?: number,
            returnBounds?: boolean | null, 
            isReadModified?: boolean | null)
    {        
        this._startTime = startTime;
        this._endTime = endTime;
        this._numValuesPerNode = (numValuesPerNode > 0) ? numValuesPerNode : 0;
        this._returnBounds = (returnBounds) ? returnBounds : null;
        this._isReadModified = (isReadModified) ? isReadModified : null;
    }
    
    get startTime()
    {
        return this._startTime;
    }

    get endTime()
    {
        return this._endTime;
    }

    get numValuesPerNode()
    {
        return this._numValuesPerNode;
    }

    get returnBounds()
    {
        return this._returnBounds;
    }

    get isReadModified()
    {
        return this._isReadModified;
    }

    toStruct() : ReadRawModifiedDetails
    {
        let details : ReadRawModifiedDetails = { 
            StartTime: this.startTime,
            EndTime: this.endTime,
            NumValuesPerNode: this.numValuesPerNode,
            ReturnBounds: this.returnBounds,
            IsReadModified: this.isReadModified
        };

        return details;
    }

    static fromStruct(details : ReadRawModifiedDetails) : UaReadRawModifiedDetails | null
    {
        let startTime = details.StartTime;
        let endTime = details.EndTime;        

        if (null == startTime || null == endTime) return null;
        return new UaReadRawModifiedDetails(startTime, endTime, details.NumValuesPerNode, details.ReturnBounds,details.IsReadModified);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaReadRawModifiedDetails.dataTypeId, ReadRawModifiedDetailsToJSONTyped(this.toStruct()));
    }

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaReadRawModifiedDetails | null
    {
        if (!UaReadRawModifiedDetails.dataTypeId.equal(extensionObject.typeId)) return null;      
        let details : ReadRawModifiedDetails = ReadRawModifiedDetailsFromJSON(extensionObject.body);
        return UaReadRawModifiedDetails.fromStruct(details);
    }
}
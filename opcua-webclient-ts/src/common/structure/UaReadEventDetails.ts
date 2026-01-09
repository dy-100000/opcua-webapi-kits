import { ReadEventDetails, ReadEventDetailsFromJSON, ReadEventDetailsToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";
import { UaEventFilter } from ".";

export class UaReadEventDetails
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ReadEventDetails);

    private _startTime: Date;
    private _endTime: Date;    
    private _filter: UaEventFilter;
    private _numValuesPerNode: number;

    constructor(
        startTime: Date,
        endTime: Date, 
        filter: UaEventFilter,
        numValuesPerNode?: number)
    {        
        this._startTime = startTime;
        this._endTime = endTime;
        this._filter = filter;
        this._numValuesPerNode = (numValuesPerNode && numValuesPerNode > 0) ? numValuesPerNode : 0
    }
    
    get startTime()
    {
        return this._startTime;
    }

    get endTime()
    {
        return this._endTime;
    }

    get filter()
    {
        return this._filter;
    }

    get numValuesPerNode()
    {
        return this._numValuesPerNode;
    } 

    toStruct() : ReadEventDetails
    {
        let details : ReadEventDetails = {
            StartTime: this.startTime,
            EndTime: this.endTime,
            Filter: this.filter.toStruct(),
            NumValuesPerNode: this.numValuesPerNode
        };

        return details;
    }

    static fromStruct(details : ReadEventDetails) : UaReadEventDetails | null
    {
        if (undefined == details.StartTime || !(details.StartTime instanceof Date) ||
            undefined == details.EndTime || !(details.EndTime instanceof Date) ||
            undefined == details.Filter) return null;

        let filter = UaEventFilter.fromStruct(details.Filter);
        if (null == filter) return null;
        
        return new UaReadEventDetails(details.StartTime, details.EndTime, filter, details.NumValuesPerNode);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaReadEventDetails.dataTypeId, ReadEventDetailsToJSONTyped(this.toStruct()));
    }

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaReadEventDetails | null
    {
        if (!UaReadEventDetails.dataTypeId.equal(extensionObject.typeId)) return null;      
        let details : ReadEventDetails = ReadEventDetailsFromJSON(extensionObject.body);
        return UaReadEventDetails.fromStruct(details);
    }
}
import { ReadProcessedDetails, ReadProcessedDetailsFromJSON, ReadProcessedDetailsToJSONTyped } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { parseUaNodeIdOrNull, UaExtensionObject, UaNodeId } from "../types";

export class UaReadProcessedDetails
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ReadProcessedDetails);

    private _startTime: Date;
    private _endTime: Date;
    private _processingInterval: number;
    private _aggregateType: Array<UaNodeId> | null;

    constructor(
            startTime : Date,
            endTime: Date,
            processingInterval: number,
            aggregateType?: Array<UaNodeId> | null)
    {        
        this._startTime = startTime;
        this._endTime = endTime;
        this._processingInterval = processingInterval;
        this._aggregateType = (aggregateType) ? aggregateType : null;
    }

    get startTime() : Date
    {
        return this._startTime;
    }

    get endTime() : Date
    {
        return this._endTime;
    }

    get processingInterval() : number
    {
        return this._processingInterval;
    }

    get aggregateType() : Array<UaNodeId> | null
    {
        return this._aggregateType;
    }

    toStruct() : ReadProcessedDetails
    {        
        let aggregateType : Array<string> = [];

        if (this._aggregateType) {
            for (let item of this._aggregateType) {
                aggregateType.push(item.toString());
            }
        }

        let details : ReadProcessedDetails = { 
            StartTime: this.startTime,
            EndTime: this.endTime,
            ProcessingInterval: this.processingInterval,
            AggregateType: aggregateType
        };

        return details;
    }


    static fromStruct(details : ReadProcessedDetails) : UaReadProcessedDetails | null
    {
        let startTime = details.StartTime;
        let endTime = details.EndTime;        
        let processingInterval = details.ProcessingInterval;

        let aggregateType : Array<UaNodeId> | null = null;
        if (details.AggregateType && details.AggregateType.length > 0) {
            aggregateType = []; 
            for (let item of details.AggregateType) {
                let aggregateTypeId = parseUaNodeIdOrNull(item);
                if (null == aggregateTypeId) continue;
                aggregateType.push(aggregateTypeId);
            }
        }

        if (null == startTime || null == endTime || null == processingInterval) return null;
        return new UaReadProcessedDetails(startTime, endTime, processingInterval, aggregateType);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaReadProcessedDetails.dataTypeId, ReadProcessedDetailsToJSONTyped(this.toStruct()));
    } 
    
    static fromExtensionObject(extensionObject : UaExtensionObject) : UaReadProcessedDetails | null
    {
        if (!UaReadProcessedDetails.dataTypeId.equal(extensionObject.typeId)) return null;      
        let details : ReadProcessedDetails = ReadProcessedDetailsFromJSON(extensionObject.body);
        return UaReadProcessedDetails.fromStruct(details);
    }   
}
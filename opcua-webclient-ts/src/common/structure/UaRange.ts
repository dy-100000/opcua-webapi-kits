import { Range, RangeFromJSON, RangeToJSONTyped } from "opcua-webapi";
import { UaExtensionObject, UaNodeId } from "../types";
import { DataTypeIds } from "../nodes";

export class UaRange
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.Range);

    private _low : number | null;
    private _high : number | null;
    
    constructor(low: number | null, high: number | null)
    {        
        this._low = (low) ? low : Number.MIN_SAFE_INTEGER;
        this._high = (high) ? high : Number.MAX_SAFE_INTEGER;

        if (this._high < this._low)
        {
            let tmp = this._high;
            this._low = this._high;
            this._high = tmp;
        }
    }

    get low() : number | null
    {
        return this._low;
    }

    get high() : number | null
    {
        return this._high;
    }

    toStruct() : Range
    {
        let range : Range = { 
            Low: this._low,
            High: this._high
        };

        return range;
    }

    static fromStruct(range : Range) : UaRange | null
    {
        return new UaRange(range.Low, range.High);
    }

    toExtensionObject() : UaExtensionObject
    {
        return new UaExtensionObject(UaRange.dataTypeId, RangeToJSONTyped(this.toStruct()));
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaRange | null
    {
        if (!UaRange.dataTypeId.equal(extensionObject.typeId)) return null;
        let range = RangeFromJSON(extensionObject.body);       
        return UaRange.fromStruct(range);
    }
}
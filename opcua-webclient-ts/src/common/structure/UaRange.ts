import { Range } from "opcua-webapi";
import { UaExtensionObject, UaNodeId, UaNodeIdType } from "../types";
import { DataTypeIds } from "../nodes";

export class UaRange
{
    static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.Range);

    private _low : number | null;
    private _high : number | null;
    
    constructor(low?: number | null, high?: number | null)
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

    toExtensionObject() : UaExtensionObject
    {
        let range : Range = { 
            Low: this._low,
            High: this._high
        };

        return new UaExtensionObject(UaRange.dataTypeId, range);
    }    

    static fromExtensionObject(extensionObject : UaExtensionObject) : UaRange | null
    {
        if (!UaRange.dataTypeId.equal(extensionObject.dataTypeId)) return null;
        
        let range : Range = extensionObject.payload;       

        let low = (typeof range.Low === "number") ? range.Low : null;
        let high = (typeof range.High === "number") ? range.High : null;

        if (null == range.Low && null == range.High) return null;
        
        return new UaRange(low, high);
    }
}
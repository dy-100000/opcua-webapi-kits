import { UaDataValue } from "./UaDataValue";

export class UaHistoryDataResult
{
    private _historyData : Array<UaDataValue>;
    private _continuationPoint : string | null;

    constructor(historyData: Array<UaDataValue>, continuationPoint? : string | null)
    {
        this._historyData = historyData;
        this._continuationPoint = (continuationPoint) ? continuationPoint : null;
    }

    get historyData() : Array<UaDataValue>
    {
        return this._historyData;
    }

    get continuationPoint() : string | null
    {
        return this._continuationPoint;
    }
}
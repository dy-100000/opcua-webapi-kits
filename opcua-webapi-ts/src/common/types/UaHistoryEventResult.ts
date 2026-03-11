import { UaHistoryEventFieldList } from "./UaHistoryEventFieldList";

export class UaHistoryEventResult
{
    private _historyEvents : Array<UaHistoryEventFieldList>
    private _continuationPoint : string | null;

    constructor(historyEvents : Array<UaHistoryEventFieldList>, continuationPoint? : string | null)
    {
        this._historyEvents = historyEvents;
        this._continuationPoint = (continuationPoint) ? continuationPoint : null;
    }

    get historyEvents() : Array<UaHistoryEventFieldList>
    {
        return this._historyEvents;
    }

    get continuationPoint() : string | null
    {
        return this._continuationPoint;
    }
}
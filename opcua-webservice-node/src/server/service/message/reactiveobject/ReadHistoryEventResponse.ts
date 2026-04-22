import { UaHistoryEventFieldList } from "opcua-webapi-ts";

export class ReadHistoryEventResponse {
    private readonly _events: Array<UaHistoryEventFieldList>;
    private readonly _containsMoreData: boolean;

    constructor(events: Array<UaHistoryEventFieldList>, containsMoreData: boolean) {
        this._events = events;
        this._containsMoreData = containsMoreData;
    }

    get events(): Array<UaHistoryEventFieldList> {
        return this._events;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }
}
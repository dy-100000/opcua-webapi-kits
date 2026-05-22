import { UaHistoryEventFieldList } from "opcua-webapi-ts";

export class ReadEventsResponse {
    private readonly _eventsData: Array<UaHistoryEventFieldList>;
    private _containsMoreData: boolean;

    constructor() {
        this._eventsData = [];
        this._containsMoreData = false;
    }

    addEventData(eventData: UaHistoryEventFieldList): void {
        this._eventsData.push(eventData);
    }

    get eventsData(): Array<UaHistoryEventFieldList> {
        return this._eventsData;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }
}
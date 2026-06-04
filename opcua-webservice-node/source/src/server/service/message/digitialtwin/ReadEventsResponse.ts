import { EventData } from "../../..";

export class ReadEventsResponse {
    private readonly _eventsData: Array<EventData>;
    private _containsMoreData: boolean;

    constructor() {
        this._eventsData = [];
        this._containsMoreData = false;
    }

    addEventData(eventData: EventData): void {
        this._eventsData.push(eventData);
    }

    get eventsData(): Array<EventData> {
        return this._eventsData;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }
}
import { UaVariant } from "opcua-webapi-ts";

export class EventData {
    private readonly _eventId: string;
    private _time: Date | null;
    private _message: string;
    private readonly _eventData: Map<string, UaVariant>;

    constructor(eventId: string) {
        this._eventId = eventId;
        this._time = null;
        this._message = "";
        this._eventData = new Map<string, UaVariant>();
    }

    get eventId(): string {
        return this._eventId;
    }

    get time(): Date | null {
        return this._time;
    }

    set time(time: Date) {
        this._time = time;
    }

    get message(): string {
        return this._message;
    }

    set message(message: string) {
        this._message = message;
    }

    setFieldData(field: string, data: UaVariant): void {
        this._eventData.set(field, data);
    }

    get eventData(): Map<string, UaVariant> {
        return this._eventData;
    }
}

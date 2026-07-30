import { UaLocalizedText } from "opcua-webapi-ts";

export class ReadObjectAttributeResponse {
    private readonly _browseName: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _description: UaLocalizedText;
    private readonly _eventNotifier: number;
    private readonly _writeMask: number;

    constructor(
        browseName: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        eventNotifier: number = 0,
        writeMask: number = 0) {
        this._browseName = browseName;
        this._displayName = displayName;
        this._description = description;
        this._eventNotifier = eventNotifier;
        this._writeMask = writeMask;
    }

    get browseName(): string {
        return this._browseName;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get writeMask(): number {
        return this._writeMask;
    }

    get description(): UaLocalizedText {
        return this._description;
    }

    get eventNotifier(): number {
        return this._eventNotifier;
    }
}
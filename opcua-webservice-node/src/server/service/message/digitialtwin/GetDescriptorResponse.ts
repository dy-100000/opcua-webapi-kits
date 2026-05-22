import { UaLocalizedText } from "opcua-webapi-ts";

export class GetDescriptorResponse {
    private readonly _displayName: UaLocalizedText;
    private readonly _description: UaLocalizedText;

    constructor(displayName: string);
    constructor(displayName: UaLocalizedText, description: UaLocalizedText);
    constructor(displayName: string | UaLocalizedText, description: UaLocalizedText = UaLocalizedText.nullText) {
        this._displayName = typeof displayName === "string"
            ? new UaLocalizedText(displayName)
            : displayName;
        this._description = description;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get description(): UaLocalizedText {
        return this._description;
    }
}
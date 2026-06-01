import { UaLocalizedText } from "opcua-webapi-ts";

export class UaApplicationDescriptor {
    private readonly _uri: string;
    private readonly _name: UaLocalizedText;
    private readonly _url: string;

    constructor(uri: string, name: UaLocalizedText, serverUrl: string) {
        this._uri = uri;
        this._name = name;
        this._url = serverUrl + "/" + uri;
    }

    get uri(): string {
        return this._uri;
    }

    get name(): UaLocalizedText {
        return this._name;
    }

    get url(): string {
        return this._url;
    }
}
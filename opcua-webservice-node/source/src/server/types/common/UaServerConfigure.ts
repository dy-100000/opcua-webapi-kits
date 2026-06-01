import { ApplicationType } from "opcua-webapi";
import { UaLocalizedText } from "opcua-webapi-ts";

export class UaServerConfigure {
    private _applicationUri : string;
    private _applicationName : UaLocalizedText;
    private _productUri : string;
    private _applicationType : ApplicationType;

    private _supportServerUriPath : boolean;

    private _browseRequestMaxSize : number;
    private _readRequestMaxSize : number;
    private _writeRequestMaxSize : number;
    private _callRequestMaxSize : number;
    private _historyReadRequestMaxSize : number;
    private _translateRequestMaxSize : number;

    constructor()
    {
        this._applicationUri = "Unknown";
        this._applicationName = new UaLocalizedText("Unknown");
        this._productUri = "Unknown";
        this._applicationType = ApplicationType.Server;

        this._supportServerUriPath = false;

        this._browseRequestMaxSize = 0;
        this._readRequestMaxSize = 0;
        this._writeRequestMaxSize = 0;
        this._callRequestMaxSize = 0;
        this._historyReadRequestMaxSize = 0;
        this._translateRequestMaxSize = 0;
    }

    get applicationType() : ApplicationType 
    {
        return this._applicationType;
    }

    set applicationType(applicationType : ApplicationType) {
        this._applicationType = applicationType;
    }

    get applicationUri() : string {
        return this._applicationUri;
    }

    set applicationUri(applicationUri: string)
    {
        this._applicationUri = applicationUri;
    }

    get applicationName() : UaLocalizedText 
    {
        return this._applicationName;
    }

    set applicationName(applicationName : UaLocalizedText)
    {
        this._applicationName = applicationName;
    }

    get productUri() : string
    {
        return this._productUri;
    }

    set productUri(productUri : string)
    {
        this._productUri = productUri;
    }

    get supportServerUriPath() : boolean
    {
        return this._supportServerUriPath;
    }

    set supportServerUriPath(isSupported : boolean) 
    {
        this._supportServerUriPath = isSupported;
    }

    get browseRequestMaxSize() : number 
    {
        return this._browseRequestMaxSize;
    }

    set browseRequestMaxSize(browseRequestMaxSize : number) 
    {
        this._browseRequestMaxSize = Math.max(browseRequestMaxSize, 0);
    }

    get readRequestMaxSize() : number
    {
        return this._readRequestMaxSize;
    }

    set readRequestMaxSize(readRequestMaxSize : number) {
        this._readRequestMaxSize = Math.max(readRequestMaxSize, 0);
    }

    get writeRequestMaxSize() : number{
        return this._writeRequestMaxSize;
    }

    set writeRequestMaxSize(writeRequestMaxSize : number) {
        this._writeRequestMaxSize = Math.max(writeRequestMaxSize, 0);
    }

    get callRequestMaxSize() {
        return this._callRequestMaxSize;
    }

    set callRequestMaxSize(callRequestMaxSize : number) {
        this._callRequestMaxSize = Math.max(callRequestMaxSize, 0);
    }

    get historyReadRequestMaxSize() {
        return this._historyReadRequestMaxSize;
    }

    set historyReadRequestMaxSize(historyReadRequestMaxSize: number) {
        this._historyReadRequestMaxSize = historyReadRequestMaxSize;
    }

    get translateRequestMaxSize() {
        return this._translateRequestMaxSize;
    }

    set translateRequestMaxSize(translateRequestMaxSize : number) {
        this._translateRequestMaxSize = Math.max(translateRequestMaxSize, 0);
    }
}
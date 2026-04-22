import { UaBrowseDescription } from "opcua-webapi-ts";
import { UaBrowseAdditionalInfo, UaObjectId } from "../../../types";

export class BrowseObjectRequest {
    private readonly _objectId: UaObjectId;
    private readonly _additionalInfo: UaBrowseAdditionalInfo;
    private readonly _browseDescription: UaBrowseDescription;

    constructor(
        objectId: UaObjectId,
        additionalInfo: UaBrowseAdditionalInfo,
        browseDescription: UaBrowseDescription,
    ) {
        this._objectId = objectId;
        this._additionalInfo = additionalInfo;
        this._browseDescription = browseDescription;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get additionalInfo(): UaBrowseAdditionalInfo {
        return this._additionalInfo;
    }

    get browseDescription(): UaBrowseDescription {
        return this._browseDescription;
    }
}
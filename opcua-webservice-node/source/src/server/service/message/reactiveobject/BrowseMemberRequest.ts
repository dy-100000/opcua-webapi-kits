import { UaBrowseDescription } from "opcua-webapi-ts";
import { UaObjectId } from "../../../types";

export class BrowseMemberRequest {
    private readonly _objectId: UaObjectId;
    private readonly _childId: string;
    private readonly _browseDescription: UaBrowseDescription;

    constructor(
        objectId: UaObjectId,
        childId: string,
        browseDescription: UaBrowseDescription,
    ) {
        this._objectId = objectId;
        this._childId = childId;
        this._browseDescription = browseDescription;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get childId(): string {
        return this._childId;
    }

    get browseDescription(): UaBrowseDescription {
        return this._browseDescription;
    }
}
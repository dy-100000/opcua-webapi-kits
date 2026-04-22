import { UaBrowseDescription } from "opcua-webapi-ts";
import { UaObjectId } from "../../../types";

export class BrowseMemberRequest {
    private readonly _objectId: UaObjectId;
    private readonly _childId: string;
    private readonly _isMethod: boolean;
    private readonly _browseDescription: UaBrowseDescription;

    constructor(
        objectId: UaObjectId,
        childId: string,
        isMethod: boolean,
        browseDescription: UaBrowseDescription,
    ) {
        this._objectId = objectId;
        this._childId = childId;
        this._isMethod = isMethod;
        this._browseDescription = browseDescription;
    }

    get objectId(): UaObjectId {
        return this._objectId;
    }

    get childId(): string {
        return this._childId;
    }

    get isMethod(): boolean {
        return this._isMethod;
    }

    get browseDescription(): UaBrowseDescription {
        return this._browseDescription;
    }
}
import { UaLocalizedText } from "opcua-webapi-ts"
import { ObjectServiceContext } from "../../../types/digitaltwin";

export class AddRequest {
    private readonly _parentId: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        displayName: UaLocalizedText | null) {        
        this._context = context;
        this._parentId = context.objectId.id;
        this._displayName = displayName ?? UaLocalizedText.nullText;
    }

    get parentId(): string {
        return this._parentId;
    }

    get displayName() : UaLocalizedText {
        return this._displayName;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
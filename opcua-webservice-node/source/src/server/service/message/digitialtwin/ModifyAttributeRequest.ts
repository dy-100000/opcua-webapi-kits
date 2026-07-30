import { UaLocalizedText } from "opcua-webapi-ts";
import { ObjectServiceContext } from "../../../types/digitaltwin";

export class ModifyAttributeRequest {
    private readonly _id: string;
    private readonly _context: ObjectServiceContext;
    private readonly _text: UaLocalizedText;

    constructor(context: ObjectServiceContext, text: UaLocalizedText) {        
        this._id = context.objectId.id;
        this._context = context;
        this._text = text;
    }

    get id(): string {
        return this._id;
    }


    get text(): UaLocalizedText {
        return this._text;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
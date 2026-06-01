import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class ReadPropertyValuesRequest {
    private readonly _id: string;
    private readonly _propertyNames: Set<string>;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyNames: Set<string>,
    ) {
        this._id = context.objectId.id;
        this._propertyNames = propertyNames;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyNames(): Set<string> {
        return this._propertyNames;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
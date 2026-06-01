import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class GetPropertySubElementsRequest {
    private readonly _id: string;
    private readonly _propertyId: string;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyId: string) {
        this._id = context.objectId.id;
        this._propertyId = propertyId;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyId(): string {
        return this._propertyId;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
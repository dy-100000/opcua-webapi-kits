import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class GetPropertyDescriptorRequest {
    private readonly _id: string;
    private readonly _propertyId: string;
    private readonly _subElementName: string | null;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyId: string,
        subElementName: string | null = null) {
        this._id = context.objectId.id;
        this._propertyId = propertyId;
        this._subElementName = subElementName;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyId(): string {
        return this._propertyId;
    }

    get subElementName(): string | null {
        return this._subElementName;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
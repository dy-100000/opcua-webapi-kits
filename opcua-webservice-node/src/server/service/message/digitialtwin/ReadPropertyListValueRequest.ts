import { UaChildId } from "../../../types/common/UaChildId";
import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class ReadPropertyListValueRequest {
    private readonly _id: string;
    private readonly _propertyIds: Set<string>;
    private readonly _subPropertyIds: Set<UaChildId>;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyIds: Set<string>,
        subPropertyIds: Set<UaChildId>,
    ) {
        this._id = context.objectId.id;
        this._propertyIds = propertyIds;
        this._subPropertyIds = subPropertyIds;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyIds(): Set<string> {
        return this._propertyIds;
    }

    get subPropertyIds(): Set<UaChildId> {
        return this._subPropertyIds;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
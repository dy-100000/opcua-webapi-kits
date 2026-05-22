import { UaVariant } from "opcua-webapi-ts";
import { UaChildId } from "../../../types/common/UaChildId";
import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class WritePropertyListValuesRequest {
    private readonly _id: string;
    private readonly _propertyIdsAndValues: Map<string, UaVariant>;
    private readonly _subPropertyIdsAndValues: Map<UaChildId, UaVariant>;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyIdsAndValues: Map<string, UaVariant>,
        subPropertyIdsAndValues: Map<UaChildId, UaVariant>) {
        this._id = context.objectId.id;
        this._propertyIdsAndValues = propertyIdsAndValues;
        this._subPropertyIdsAndValues = subPropertyIdsAndValues;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyIdsAndValues(): Map<string, UaVariant> {
        return this._propertyIdsAndValues;
    }

    get subPropertyIdsAndValues(): Map<UaChildId, UaVariant> {
        return this._subPropertyIdsAndValues;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
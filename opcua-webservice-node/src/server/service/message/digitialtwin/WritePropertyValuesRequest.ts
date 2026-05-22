import { UaVariant } from "opcua-webapi-ts";
import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class WritePropertyValuesRequest {
    private readonly _id: string;
    private readonly _propertyNamesAndValues: Map<string, UaVariant>;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyNamesAndValues: Map<string, UaVariant>) {
        this._id = context.objectId.id;
        this._propertyNamesAndValues = propertyNamesAndValues;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyNamesAndValues(): Map<string, UaVariant> {
        return this._propertyNamesAndValues;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
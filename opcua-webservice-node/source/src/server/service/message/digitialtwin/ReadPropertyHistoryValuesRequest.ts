import { DataTypeIds, UaExtensionObject, UaNodeId, UaReadAtTimeDetails, UaReadRawModifiedDetails,UaReadProcessedDetails } from "opcua-webapi-ts";
import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class ReadPropertyHistoryValuesRequest {
    private readonly _id: string;
    private readonly _propertyToRead: string;
    private readonly _details: UaExtensionObject;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        propertyToRead: string,
        details: UaExtensionObject) {
        this._id = context.objectId.id;
        this._propertyToRead = propertyToRead;
        this._details = details;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get propertyToRead(): string {
        return this._propertyToRead;
    }

    get details(): UaExtensionObject {
        return this._details;
    }

    get readRawDetails(): UaReadRawModifiedDetails | null {
        if (!this._details.typeId.equal(UaNodeId.from(DataTypeIds.ReadRawModifiedDetails))) {
            return null;
        }

        return UaReadRawModifiedDetails.fromExtensionObject(this._details);
    }

    get readAtTimeDetails(): UaReadAtTimeDetails | null {
        if (!this._details.typeId.equal(UaNodeId.from(DataTypeIds.ReadAtTimeDetails))) {
            return null;
        }

        return UaReadAtTimeDetails.fromExtensionObject(this._details);
    }

    get readProcessedDetails(): UaReadProcessedDetails | null {
        if (!this._details.typeId.equal(UaNodeId.from(DataTypeIds.ReadProcessedDetails))) {
            return null;
        }

        return UaReadProcessedDetails.fromExtensionObject(this._details);
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
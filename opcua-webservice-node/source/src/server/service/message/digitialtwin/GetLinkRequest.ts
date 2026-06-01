import { ObjectServiceContext } from "../../../types/digitaltwin/ObjectServiceContext";

export class GetLinkRequest {
    private readonly _id: string;
    private readonly _limit: number;
    private readonly _offset: number;
    private readonly _context: ObjectServiceContext;

    constructor(
        context: ObjectServiceContext,
        limit: number,
        offset: number) {
        this._id = context.objectId.id;
        this._limit = limit;
        this._offset = offset;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get limit(): number {
        return this._limit;
    }

    get offset(): number {
        return this._offset;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
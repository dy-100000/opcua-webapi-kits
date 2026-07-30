import { ObjectServiceContext } from "../../../types/digitaltwin";

export class DeleteRequest {
    private readonly _id: string;
    private readonly _context: ObjectServiceContext;

    constructor(context: ObjectServiceContext) {        
        this._id = context.objectId.id;
        this._context = context;
    }

    get id(): string {
        return this._id;
    }

    get context(): ObjectServiceContext {
        return this._context;
    }
}
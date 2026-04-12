export type SerializedObjectIdentifier = {    
    i: string;
    t?: string;
    id?: string;
};

export class UaObjectIdentifier {    
    private _id: string;
    private _typeId: string | null;
    private _instanceDeclId: string | null;

    constructor(typeId: string, id: string, instanceDeclId: string | null) {        
        this._id = id;
        this._typeId = (instanceDeclId) ? null : typeId;
        this._instanceDeclId = instanceDeclId;
    }

    get id(): string {
        return this._id;
    }

    get typeId(): string | null {
        return this._typeId;
    }

    get instanceDeclId(): string | null {
        return this._instanceDeclId;
    }
   
    toString(): string {
        return JSON.stringify(this.toJson());   
    }

    toJson(): SerializedObjectIdentifier {
        return {            
            i: this._id,
            t: this._typeId ?? undefined,
            id: this._instanceDeclId ?? undefined
        };
    }

    static fromJson(json: unknown): UaObjectIdentifier | null {

        if (!json || typeof json !== "object") return null;

        const serialized = json as Partial<SerializedObjectIdentifier>;
        if (typeof serialized.i !== "string") return null;

        if (typeof serialized.t === "string" && typeof serialized.id === "string") {
            return new UaObjectIdentifier(serialized.t, serialized.i, serialized.id ?? null);
        } else if (typeof serialized.t === "string") {
            return new UaObjectIdentifier(serialized.t, serialized.i, null);
        } else if (typeof serialized.id === "string") {
            return new UaObjectIdentifier("", serialized.i, serialized.id);
        } else {
            return null;
        }
    }
}
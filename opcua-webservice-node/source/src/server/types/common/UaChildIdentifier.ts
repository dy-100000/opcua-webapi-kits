export type SerializedChildIdentifier = {
    p: string;
    p2?: string;
};

export class UaChildIdentifier {
    private _path: string;
    private _pathL2: string | null;

    constructor(path: string, pathL2: string | null) {
        this._path = path;
        this._pathL2 = pathL2;
    }

    get path(): string {
        return this._path;
    }

    get pathL2(): string | null {
        return this._pathL2;
    }

    toString(): string {
        let ret = this._path;
        
        if (null !== this._pathL2)
        {
            ret += `@${this._pathL2}`;
        }

        return ret;
    }

    toJson(): SerializedChildIdentifier {
        return {
            p: this._path,
            p2: this._pathL2 ?? undefined
        };
    }

    static fromJson(json: unknown): UaChildIdentifier | null {
        if (!json || typeof json !== "object") {
            return null;
        }

        const serialized = json as Partial<SerializedChildIdentifier>;
        if (typeof serialized.p !== "string") {
            return null;
        }

        if (serialized.p2 !== undefined && typeof serialized.p2 !== "string") {
            return null;
        }

        return new UaChildIdentifier(
            serialized.p,
            serialized.p2 ?? null
        );
    }
}
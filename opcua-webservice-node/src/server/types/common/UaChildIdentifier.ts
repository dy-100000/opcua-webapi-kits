export type SerializedChildIdentifier = {
    p: string;
    p2?: string;
    mn?: boolean;
};

export class UaChildIdentifier {
    private _path: string;
    private _pathL2: string | null;
    private _methodNode: boolean;

    constructor(path: string, pathL2: string | null, methodNode: boolean) {
        this._path = path;
        this._pathL2 = pathL2;
        this._methodNode = methodNode;
    }

    get path(): string {
        return this._path;
    }

    get pathL2(): string | null {
        return this._pathL2;
    }

    get methodNode(): boolean {
        return this._methodNode;
    }    

    toString(): string {
        let ret = this._path;
        
        if (null !== this._pathL2)
        {
            ret += `@${this._pathL2}`;
         }

        if (this._methodNode)
        {
            ret += "*";
        }

        return ret;
    }

    toJson(): SerializedChildIdentifier {
        return {
            p: this._path,
            p2: this._pathL2 ?? undefined,
            mn: this._methodNode ?? undefined,
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

        if (serialized.mn !== undefined && typeof serialized.mn !== "boolean") {
            return null;
        }

        return new UaChildIdentifier(
            serialized.p,
            serialized.p2 ?? null,
            serialized.mn ?? false
        );
    }
}
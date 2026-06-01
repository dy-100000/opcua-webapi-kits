export class UaChildId {
    private readonly _id: string;
    private readonly _subElementName: string | null;

    constructor(id: string, subElementName?: string | null) {
        this._id = id;
        this._subElementName = (subElementName === undefined || subElementName === null || subElementName.length === 0)
            ? null
            : subElementName;
    }

    get id(): string {
        return this._id;
    }

    get subElementName(): string | null {
        return this._subElementName;
    }

    toString(): string {
        let ret = this._id;

        if (this._subElementName !== null) {
            ret += `#${this._subElementName}`;
        }

        return ret;
    }
}
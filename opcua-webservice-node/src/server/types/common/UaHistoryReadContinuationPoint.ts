type SerializedHistoryReadContinuationPoint = {
    offset: number;
};

export class UaHistoryReadContinuationPoint {
    private _offset: number;

    constructor(offset?: number) {
        this._offset = offset ?? 0;
    }

    get offset(): number {
        return this._offset;
    }

    set offset(offset: number) {
        this._offset = offset;
    }

    toByteString(): string {
        if (this._offset <= 0) {
            return "";
        }

        try {
            const serialized: SerializedHistoryReadContinuationPoint = {
                offset: this._offset,
            };

            return Buffer.from(JSON.stringify(serialized), "utf-8").toString("base64");
        } catch {
            return "";
        }
    }

    static fromByteString(continuationPoint: string): UaHistoryReadContinuationPoint | null {
        if (!continuationPoint) {
            return null;
        }

        try {
            const serialized = JSON.parse(
                Buffer.from(continuationPoint, "base64").toString("utf-8"),
            ) as Partial<SerializedHistoryReadContinuationPoint>;

            if (typeof serialized.offset !== "number") {
                return null;
            }

            return new UaHistoryReadContinuationPoint(serialized.offset);
        } catch {
            return null;
        }
    }
}
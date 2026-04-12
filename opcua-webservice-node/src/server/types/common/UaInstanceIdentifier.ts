import { UaChildIdentifier, SerializedChildIdentifier } from "./UaChildIdentifier";
import { UaObjectIdentifier, SerializedObjectIdentifier } from "./UaObjectIdentifier";

export type SerializedInstanceIdentifier = {
    oi: SerializedObjectIdentifier;
    ci?: SerializedChildIdentifier;
};

export class UaInstanceIdentifier {
    private _objectId: UaObjectIdentifier;
    private _childId: UaChildIdentifier | null;

    constructor(objectId: UaObjectIdentifier, childId: UaChildIdentifier | null) {
        this._objectId = objectId;
        this._childId = childId;
    }

    get objectId(): UaObjectIdentifier {
        return this._objectId;
    }

    get childId(): UaChildIdentifier | null {
        return this._childId;
    }

    static fromByteString(identifier: string): UaInstanceIdentifier | null {
        if (!identifier) {
            return null;
        }

        try {
            const serialized = JSON.parse(
                Buffer.from(identifier, "base64").toString("utf-8"),
            ) as Partial<SerializedInstanceIdentifier>;

            return UaInstanceIdentifier.fromJson(serialized);
        } catch {
            return null;
        }
    }

    toByteString(): string | null {
        return JSON.stringify(this.toJson());
    }

    toString(): string {
        let ret = "ObjectId: ";
        ret += this._objectId.toString();

        if (this._childId !== null) {
            ret += ` ChildId: ${this._childId.toString()}`;
        }

        return ret;
    }

    toJson(): SerializedInstanceIdentifier {
        return {
            oi: this._objectId.toJson(),
            ci: this._childId?.toJson(),
        };
    }

    static fromJson(json: unknown): UaInstanceIdentifier | null {
        if (!json || typeof json !== "object") {
            return null;
        }

        const serialized = json as Partial<SerializedInstanceIdentifier>;
        const objectId = UaObjectIdentifier.fromJson(serialized.oi);
        if (objectId === null) {
            return null;
        }

        if (serialized.ci === undefined) {
            return new UaInstanceIdentifier(objectId, null);
        }

        const childId = UaChildIdentifier.fromJson(serialized.ci);
        if (childId === null) {
            return null;
        }

        return new UaInstanceIdentifier(objectId, childId);
    }
}
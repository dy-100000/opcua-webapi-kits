import type { UaObject } from "../../addressspace/nodes/UaObject";

export class UaObjectId {
    private readonly _id: string;
    private readonly _instance: UaObject | null;

    constructor(id: string, instance: UaObject | null) {
        this._id = id;
        this._instance = instance;
    }

    get id(): string {
        return this._id;
    }

    get instance(): UaObject | null {
        return this._instance;
    }

    toString(): string {
        let ret = this._id;

        if (this._instance !== null) {
            ret += `${this._instance.browseName}`;
        }

        return ret;
    }
}
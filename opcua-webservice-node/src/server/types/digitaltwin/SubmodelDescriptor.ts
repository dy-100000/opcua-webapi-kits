import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObject } from "../../addressspace/nodes/UaObject";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";

export class SubmodelDescriptor {
    private readonly _id: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeId: UaNodeId;
    private readonly _instance: UaObject | null;

    constructor(id: string, instance: UaObject);
    constructor(id: string, displayName: UaLocalizedText, submodelType: UaObjectType);
    constructor(id: string, displayNameOrInstance: UaLocalizedText | UaObject, submodelType?: UaObjectType) {
        this._id = id;

        if (displayNameOrInstance instanceof UaObject) {
            this._displayName = UaLocalizedText.nullText;
            this._typeId = UaNodeId.nullNodeId;
            this._instance = displayNameOrInstance;
            return;
        }

        if (submodelType === undefined) {
            throw new Error("submodelType is required when constructing a submodel descriptor from a type definition");
        }

        this._displayName = displayNameOrInstance;
        this._typeId = submodelType.nodeId;
        this._instance = null;
    }

    get id(): string {
        return this._id;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get typeId(): UaNodeId {
        return this._typeId;
    }

    get instance(): UaObject | null {
        return this._instance;
    }
}
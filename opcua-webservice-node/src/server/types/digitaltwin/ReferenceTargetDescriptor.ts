import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaObject } from "../../addressspace/nodes/UaObject";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";

export class ReferenceTargetDescriptor {
    private readonly _id: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeId: UaNodeId;
    private readonly _instanceDeclaration: UaObject | null;
    
    constructor(
        id: string,
        displayName: UaLocalizedText,
        typeDefinition: UaObjectType,
        instanceDeclaration: UaObject | null = null) {
        this._id = id;
        this._displayName = displayName;
        this._typeId = typeDefinition.nodeId;
        this._instanceDeclaration = instanceDeclaration;
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

    get instanceDeclaration(): UaObject | null {
        return this._instanceDeclaration;
    }
}
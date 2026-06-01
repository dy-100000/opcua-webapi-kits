import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaInstanceNode } from "../../addressspace/nodes/UaInstanceNode";
import { UaObjectType } from "../../addressspace/nodes/UaObjectType";

export class ElementDescriptor {
    private readonly _id: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeId: UaNodeId;
    private readonly _instance: UaInstanceNode | null;

    constructor(id: string, instance: UaInstanceNode);
    constructor(id: string, displayName: UaLocalizedText, elementType: UaObjectType);
    constructor(id: string, displayNameOrInstance: UaLocalizedText | UaInstanceNode, elementType?: UaObjectType) {
        let displayName = UaLocalizedText.nullText;
        let typeId = UaNodeId.nullNodeId;
        let instance: UaInstanceNode | null = null;

        if (displayNameOrInstance instanceof UaLocalizedText && elementType != null) {
            displayName = displayNameOrInstance;
            typeId = elementType.nodeId;
        }

        if (displayNameOrInstance instanceof UaInstanceNode) {
            this._instance = displayNameOrInstance;
        }

        this._id = id;
        this._displayName = displayName;
        this._typeId = typeId;
        this._instance = instance;
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

    get instance(): UaInstanceNode | null {
        return this._instance;
    }
}
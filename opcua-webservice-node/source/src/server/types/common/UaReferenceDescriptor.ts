import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaInstanceNode } from "../../addressspace/nodes/UaInstanceNode";
import { UaObject } from "../../addressspace/nodes/UaObject";
import { UaVariable } from "../../addressspace/nodes/UaVariable";

export class UaReferenceDescriptor {
    private readonly _id: string;
    private readonly _nodeClass: NodeClass;
    private readonly _browseName: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeDefinitionId: UaNodeId;
    private readonly _isForward: boolean;
    private readonly _referenceTypeId: UaNodeId;
    private readonly _instanceDeclarationId: UaNodeId;

    constructor(
        id: string,
        nodeClass: NodeClass,
        browseName: string,
        displayName: UaLocalizedText,
        typeDefinitionId: UaNodeId,
        referenceTypeId: UaNodeId,
        isForward: boolean,
        instanceDeclarationId: UaNodeId = UaNodeId.nullNodeId,
    ) {
        this._id = id;
        this._nodeClass = nodeClass;
        this._browseName = browseName;
        this._displayName = displayName;
        this._typeDefinitionId = typeDefinitionId;
        this._referenceTypeId = referenceTypeId;
        this._isForward = isForward;
        this._instanceDeclarationId = instanceDeclarationId;
    }

    get id(): string {
        return this._id;
    }

    get nodeClass(): NodeClass {
        return this._nodeClass;
    }

    get browseName(): string {
        return this._browseName;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get typeDefinitionId(): UaNodeId {
        return this._typeDefinitionId;
    }

    get referenceTypeId(): UaNodeId {
        return this._referenceTypeId;
    }

    get isForward(): boolean {
        return this._isForward;
    }

    get instanceDeclarationId(): UaNodeId {
        return this._instanceDeclarationId;
    }

    static fromInstanceDeclaration(
        id: string,
        instanceDeclaration: UaInstanceNode,
        referenceTypeId: UaNodeId,
        isForward: boolean,
    ): UaReferenceDescriptor {
        let typeDefinitionId = UaNodeId.nullNodeId;

        if (instanceDeclaration.nodeClass === NodeClass.Object) {
            typeDefinitionId = (instanceDeclaration as UaObject).typeDefinition.nodeId;
        } else if (instanceDeclaration.nodeClass === NodeClass.Variable) {
            typeDefinitionId = (instanceDeclaration as UaVariable).typeDefinition.nodeId;
        }

        const descriptor = new UaReferenceDescriptor(
            id,
            instanceDeclaration.nodeClass,
            instanceDeclaration.browseName,
            instanceDeclaration.displayName,
            typeDefinitionId,
            referenceTypeId,
            isForward,
            instanceDeclaration.nodeId,
        );

        return descriptor;
    }
}
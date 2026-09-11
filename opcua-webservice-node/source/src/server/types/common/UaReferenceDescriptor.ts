import { NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaInstanceNode } from "../../addressspace/nodes/UaInstanceNode";
import { UaObject } from "../../addressspace/nodes/UaObject";
import { UaVariable } from "../../addressspace/nodes/UaVariable";

export class UaReferenceDescriptor {
    static readonly HasComponent = UaNodeId.from(ReferenceTypeIds.HasComponent);
    static readonly HasProperty = UaNodeId.from(ReferenceTypeIds.HasProperty);

    private readonly _id: string;
    private readonly _nodeClass: NodeClass;
    private readonly _browseName: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _typeDefinitionId: UaNodeId;
    private readonly _referenceTypeId: UaNodeId;
    private readonly _instanceDeclarationId: UaNodeId;

    constructor(
        id: string,
        nodeClass: NodeClass,
        browseName: string,
        displayName: UaLocalizedText,
        typeDefinitionId: UaNodeId,
        referenceTypeId: UaNodeId,
        instanceDeclarationId: UaNodeId = UaNodeId.nullNodeId) {
        this._id = id;
        this._nodeClass = nodeClass;
        this._browseName = browseName;
        this._displayName = displayName;
        this._typeDefinitionId = typeDefinitionId;
        this._referenceTypeId = referenceTypeId;
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

    get instanceDeclarationId(): UaNodeId {
        return this._instanceDeclarationId;
    }

    static fromInstanceDeclaration(
        id: string,
        instanceDeclaration: UaInstanceNode
    ): UaReferenceDescriptor {
        let typeDefinitionId = UaNodeId.nullNodeId;

        if (instanceDeclaration.nodeClass === NodeClass.Object) {
            typeDefinitionId = (instanceDeclaration as UaObject).typeDefinition.nodeId;
        } else if (instanceDeclaration.nodeClass === NodeClass.Variable) {
            typeDefinitionId = (instanceDeclaration as UaVariable).typeDefinition.nodeId;
        }

        let referenceTypeId = UaReferenceDescriptor.HasComponent;
        if (instanceDeclaration.nodeClass === NodeClass.Variable) {
            if ((instanceDeclaration as UaVariable).isProperty) {
                referenceTypeId = UaReferenceDescriptor.HasProperty;
            }
        }
        
        const descriptor = new UaReferenceDescriptor(
            id,
            instanceDeclaration.nodeClass,
            instanceDeclaration.browseName,
            instanceDeclaration.displayName,
            typeDefinitionId,
            referenceTypeId,
            instanceDeclaration.nodeId,
        );

        return descriptor;
    }
}
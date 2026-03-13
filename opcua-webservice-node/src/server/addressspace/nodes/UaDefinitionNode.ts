import { UaNodeId, UaLocalizedText, UaVariant, UaError, makeUaStatusCode } from "opcua-webapi-ts"; // Adjust paths as necessary
import { UaNode } from './UaNode';
import { UaReference } from './UaReference';
import { Attributes, StatusCodes } from "opcua-webapi";
import { UaReferenceTypes } from "./builtin";

export abstract class UaDefinitionNode extends UaNode {
    private readonly _isAbstract: boolean;
    private _parentType: UaDefinitionNode | null;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean
    ) {
        super(nodeId, browseName, displayName);
        this._isAbstract = isAbstract;
        this._parentType = null;
    }

    public get isAbstract(): boolean {
        return this._isAbstract;
    }
    
    public get parentType(): UaDefinitionNode | null {
        return this._parentType;
    }

    public isSubtypeOf(typeId: UaNodeId): boolean {
        if (this.nodeId.equal(typeId)) return true;
        if (this._parentType === null) return false;
        return this._parentType.isSubtypeOf(typeId);
    }

    protected setParentType(parentType: UaDefinitionNode): void {
        if (this.nodeClass !== parentType.nodeClass) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeClassInvalid));
        }
        if (this._parentType !== null) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadAlreadyExists));
        }

        this._parentType = parentType;
        this.addReference(new UaReference(parentType, UaReferenceTypes.HasSubtype, false));
        parentType.addReference(new UaReference(this, UaReferenceTypes.HasSubtype, true));
    }

    public getAttribute(attributeId: number): UaVariant {
        if (Attributes.IsAbstract === attributeId) {
            return UaVariant.boolean(this._isAbstract);
        }

        return super.getAttribute(attributeId);
    }
}
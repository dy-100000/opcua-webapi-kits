import { NodeClass } from "opcua-webapi";
import { UaNodeId, UaLocalizedText, UaVariant } from "opcua-webapi-ts";
import { UaDefinitionNode } from "./UaDefinitionNode";

export class UaReferenceType extends UaDefinitionNode {
    private readonly _inverseName: UaLocalizedText;
    private readonly _symmetric: boolean;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        inverseName: UaLocalizedText,
        symmetric: boolean) {
        super(nodeId, browseName, displayName, isAbstract);
        this._inverseName = inverseName;
        this._symmetric = symmetric;
    }

    public get nodeClass(): NodeClass {
        return NodeClass.ReferenceType;
    }

    public get inverseName(): UaLocalizedText {
        return this._symmetric ? this.displayName : this._inverseName;
    }

    public get isSymmetric(): boolean {
        return this._symmetric;
    }
}
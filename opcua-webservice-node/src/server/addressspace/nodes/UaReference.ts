
import { UaNode } from './UaNode';
import type { UaReferenceType } from './UaReferenceType';

export class UaReference {
    private readonly _linkedNode: UaNode;
    private readonly _reference: UaReferenceType;
    private readonly _isForward: boolean;

    constructor(
        linkedNode: UaNode,
        reference: UaReferenceType,
        isForward: boolean) {
        this._linkedNode = linkedNode;
        this._reference = reference;
        this._isForward = isForward;
    }

    get linkedNode(): UaNode {
        return this._linkedNode;
    }

    get reference(): UaReferenceType {
        return this._reference;
    }

    get isForward(): boolean {
        return this._isForward;
    }
}

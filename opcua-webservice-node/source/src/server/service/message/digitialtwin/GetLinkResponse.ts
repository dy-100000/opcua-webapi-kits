import { ReferenceTargetDescriptor } from "../../../types/digitaltwin/ReferenceTargetDescriptor";

export class GetLinkResponse {
    private readonly _targets: Array<ReferenceTargetDescriptor>;
    private _containsMoreData: boolean;

    constructor() {
        this._targets = [];
        this._containsMoreData = false;
    }

    add(descriptor: ReferenceTargetDescriptor): void {
        this._targets.push(descriptor);
    }

    get targets(): Array<ReferenceTargetDescriptor> {
        return this._targets;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }
}
import { SubmodelDescriptor } from "../../../types/digitaltwin/SubmodelDescriptor";

export class GetSubmodelsResponse {
    private readonly _submodels: Array<SubmodelDescriptor>;

    constructor() {
        this._submodels = [];
    }

    add(descriptor: SubmodelDescriptor): void {
        this._submodels.push(descriptor);
    }

    get submodels(): Array<SubmodelDescriptor> {
        return this._submodels;
    }
}
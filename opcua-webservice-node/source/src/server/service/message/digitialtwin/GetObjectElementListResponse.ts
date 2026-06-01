import { ObjectElementDescriptor } from "../../../types/digitaltwin/ObjectElementDescriptor";

export class GetObjectElementListResponse {
    private readonly _elements: Array<ObjectElementDescriptor>;
    private _containsMoreData: boolean;

    constructor() {
        this._elements = [];
        this._containsMoreData = false;
    }

    get elements(): Array<ObjectElementDescriptor> {
        return this._elements;
    }

    add(descriptor: ObjectElementDescriptor): void {
        this._elements.push(descriptor);
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }
}
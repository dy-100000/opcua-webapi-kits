import { PropertyElementDescriptor } from "../../../types/digitaltwin/PropertyElementDescriptor";

export class GetPropertyElementListResponse {
    private readonly _elements: Array<PropertyElementDescriptor>;
    private _containsMoreData: boolean;

    constructor() {
        this._elements = [];
        this._containsMoreData = false;
    }

    add(descriptor: PropertyElementDescriptor): void {
        this._elements.push(descriptor);
    }

    get elements(): Array<PropertyElementDescriptor> {
        return this._elements;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }
}
import { DigitalTwinDescriptor } from "../../../types/digitaltwin/DigitalTwinDescriptor";

export class GetDigitalTwinListResponse {
    private readonly _digitalTwins: Array<DigitalTwinDescriptor>;
    private _containsMoreData: boolean;

    constructor() {
        this._digitalTwins = [];
        this._containsMoreData = false;
    }

    add(descriptor: DigitalTwinDescriptor): void {
        this._digitalTwins.push(descriptor);
    }

    get digitalTwins(): Array<DigitalTwinDescriptor> {
        return this._digitalTwins;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }

    set containsMoreData(containsMoreData: boolean) {
        this._containsMoreData = containsMoreData;
    }
}
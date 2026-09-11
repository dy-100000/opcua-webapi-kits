import { UaReferenceDescriptor } from "../../../types";

export class BrowseObjectResponse {
    private readonly _children: Array<UaReferenceDescriptor>;
    private readonly _remainingTasks: number;
    private readonly _offset: number;

    constructor(
        children: Array<UaReferenceDescriptor>,
        remainingTasks: number = 0,
        offset: number = 0) {
        this._children = children;
        this._remainingTasks = remainingTasks;
        this._offset = Math.max(0, offset);
    }

    get children(): Array<UaReferenceDescriptor> {
        return this._children;
    }

    get remainingTasks(): number {
        return this._remainingTasks;
    }

    get offset(): number {
        return this._offset;
    }
}
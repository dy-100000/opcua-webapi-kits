import { UaBrowseAdditionalInfo, UaReferenceDescriptor } from "../../../types";

export class BrowseObjectResponse {
    private readonly _children: Array<UaReferenceDescriptor>;
    private readonly _containsMoreData: boolean;
    private readonly _taskMask: number;

    constructor(
        children: Array<UaReferenceDescriptor>,
        containsMoreData: boolean,
        taskMask: number = UaBrowseAdditionalInfo.ALL_TASK,
    ) {
        this._children = children;
        this._containsMoreData = containsMoreData;
        this._taskMask = taskMask;
    }

    get children(): Array<UaReferenceDescriptor> {
        return this._children;
    }

    get containsMoreData(): boolean {
        return this._containsMoreData;
    }

    get taskMask(): number {
        return this._taskMask;
    }
}
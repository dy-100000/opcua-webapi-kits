import { UaReferenceDescriptor } from "../../../types";

export class BrowseMemberResponse {
    private readonly _children: Array<UaReferenceDescriptor>;

    constructor(children: Array<UaReferenceDescriptor>) {
        this._children = children;
    }

    get children(): Array<UaReferenceDescriptor> {
        return this._children;
    }
}
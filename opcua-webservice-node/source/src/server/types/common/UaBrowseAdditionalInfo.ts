import { BrowseDirection, NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaBrowseDescription, UaNodeId } from "opcua-webapi-ts";

export class UaBrowseAdditionalInfo {
    static readonly GET_PARENT_TASK = 1;
    static readonly GET_DEFINITION_TASK = 2;
    static readonly GET_LINK_TASK = 4;

    static readonly GET_CHILD_OBJECT_TASK = 8;
    static readonly GET_CHILD_VARIABLE_TASK = 16;
    static readonly GET_CHILD_METHOD_TASK = 32;
    static readonly GET_CHILD_TASK =
        UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK +
        UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK +
        UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK;

    static readonly ALL_TASK =
        UaBrowseAdditionalInfo.GET_PARENT_TASK +
        UaBrowseAdditionalInfo.GET_CHILD_TASK +
        UaBrowseAdditionalInfo.GET_DEFINITION_TASK +
        UaBrowseAdditionalInfo.GET_LINK_TASK;

    private readonly _maxReferencesPerNode: number;
    private readonly _referenceOffset: number;
    private readonly _taskCheckListMasks: number;

    constructor(
        maxReferencesPerNode: number,
        referenceOffset: number,
        taskCheckListMasks: number,
    ) {
        this._maxReferencesPerNode = Math.max(maxReferencesPerNode, 0);
        this._referenceOffset = Math.max(referenceOffset, 0);
        this._taskCheckListMasks = taskCheckListMasks;
    }

    get maxReferencesPerNode(): number {
        return this._maxReferencesPerNode;
    }

    get referenceOffset(): number {
        return this._referenceOffset;
    }

    get taskCheckListMasks(): number {
        return this._taskCheckListMasks;
    }

    updateOffset(offsetToAdd: number): UaBrowseAdditionalInfo {
        return new UaBrowseAdditionalInfo(
            this._maxReferencesPerNode,
            this._referenceOffset + offsetToAdd,
            this._taskCheckListMasks,
        );
    }

    updateTasks(description: UaBrowseDescription): UaBrowseAdditionalInfo {
        if (this._taskCheckListMasks !== 0) {
            return this;
        }

        let browseInfoMask = 0;
        const referenceTypeId = description.referenceTypeId;
        const browseDirection = description.browseDirection;
        const includeSubtypes = description.includeSubtypes;
        const nodeClassMask = description.nodeClassMask ?? 0;

        if (includeSubtypes) {
            if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.References)) ||
                referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HierarchicalReferences)) ||
                referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.Aggregates))) {
                if ((browseDirection === BrowseDirection.Inverse || browseDirection === BrowseDirection.Both) &&
                    this.hasNodeClass(nodeClassMask, NodeClass.Object)
                ) {
                    browseInfoMask |= UaBrowseAdditionalInfo.GET_PARENT_TASK;
                }

                if (browseDirection === BrowseDirection.Forward || browseDirection === BrowseDirection.Both) {
                    if (this.hasNodeClass(nodeClassMask, NodeClass.Object)) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK;
                    }

                    if (this.hasNodeClass(nodeClassMask, NodeClass.Variable)) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK;
                    }

                    if (this.hasNodeClass(nodeClassMask, NodeClass.Method)) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK;
                    }
                }
            }

            if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.References)) ||
                referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.NonHierarchicalReferences))) {
                if (browseDirection === BrowseDirection.Forward || browseDirection === BrowseDirection.Both) {
                    if (this.hasNodeClass(nodeClassMask, NodeClass.ObjectType)) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_DEFINITION_TASK;
                    }

                    if (this.hasNodeClass(nodeClassMask, NodeClass.Object)) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_LINK_TASK;
                    }
                }
            }
        } else if (browseDirection === BrowseDirection.Forward || browseDirection === BrowseDirection.Both) {
            if (this.hasNodeClass(nodeClassMask, NodeClass.Object) &&
                referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent))) {
                browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK;
            }

            if (this.hasNodeClass(nodeClassMask, NodeClass.Variable) &&
                (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent)) ||
                    referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasProperty)))) {
                browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK;
            }

            if (this.hasNodeClass(nodeClassMask, NodeClass.Method) &&
                referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent))) {
                browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK;
            }

            if (this.hasNodeClass(nodeClassMask, NodeClass.ObjectType)) {
                if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasTypeDefinition))) {
                    browseInfoMask |= UaBrowseAdditionalInfo.GET_DEFINITION_TASK;
                }

                if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.GeneratesEvent))) {
                    browseInfoMask |= UaBrowseAdditionalInfo.GET_LINK_TASK;
                }
            }
        }

        return new UaBrowseAdditionalInfo(
            this._maxReferencesPerNode,
            this._referenceOffset,
            browseInfoMask,
        );
    }

    isTaskRequired(taskMask: number): boolean {
        return (this._taskCheckListMasks & taskMask) !== 0;
    }

    taskComplete(taskMask: number): UaBrowseAdditionalInfo {
        if (!this.isTaskRequired(taskMask)) {
            return this;
        }

        return new UaBrowseAdditionalInfo(
            this._maxReferencesPerNode,
            0,
            this._taskCheckListMasks & ~taskMask
        );
    }

    isAllTaskComplete(): boolean {
        return this._taskCheckListMasks === 0;
    }

    private hasNodeClass(nodeClassMask: number, nodeClass: NodeClass): boolean {
        return (nodeClassMask & nodeClass) !== 0;
    }
}
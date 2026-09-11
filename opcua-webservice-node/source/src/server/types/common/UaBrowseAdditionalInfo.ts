import { BrowseDirection, NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaBrowseDescription, UaNodeId } from "opcua-webapi-ts";

export class UaBrowseAdditionalInfo {
    static readonly GET_DEFINITION_TASK = 1;
    static readonly GET_RELATED_OBJECT_TASK = 2;
    static readonly GET_CHILD_VARIABLE_TASK = 4;
    static readonly GET_CHILD_METHOD_TASK = 8;

    private _maxReferencesPerNode: number;
    private _referenceOffset: number;
    private _taskCheckListMasks: number;

    constructor(
        maxReferencesPerNode: number,
        referenceOffset: number,
        taskCheckListMasks: number) {
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

    set referenceOffset(offset: number) {
        this._referenceOffset = Math.max(offset, 0);
    }

    get taskCheckListMasks(): number {
        return this._taskCheckListMasks;
    }

    set taskCheckListMasks(taskMask: number) {
        this._taskCheckListMasks = taskMask;
    }

    static build(description: UaBrowseDescription, maxReferencesPerNode: number): UaBrowseAdditionalInfo {
        let browseInfoMask = 0;
        
        const referenceTypeId = description.referenceTypeId;
        const browseDirection = description.browseDirection;
        const includeSubtypes = description.includeSubtypes;
        const nodeClassMask = description.nodeClassMask;

        if (browseDirection === BrowseDirection.Forward || browseDirection === BrowseDirection.Both) {
            if ((nodeClassMask & NodeClass.Object) !== 0)
            {
                browseInfoMask |= UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK;
            } 
            
            if ((nodeClassMask & NodeClass.Variable) !== 0) {                                
                if (includeSubtypes) {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.Aggregates)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasProperty)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.References)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HierarchicalReferences))) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK;
                    }
                } else {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasProperty))) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK;
                    }
                }
            } 
            
            if ((nodeClassMask & NodeClass.Method) !== 0) {
                if (includeSubtypes) {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.Aggregates)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.References)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HierarchicalReferences))) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK;
                        
                    }
                } else {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasComponent))) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK;
                    }
                }
            } 
            
            if ((nodeClassMask & NodeClass.ObjectType) !== 0) {
                if (includeSubtypes) {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.References)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.NonHierarchicalReferences)) ||
                        referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasTypeDefinition))) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_DEFINITION_TASK;
                    }
                } else {
                    if (referenceTypeId.equal(UaNodeId.from(ReferenceTypeIds.HasTypeDefinition))) {
                        browseInfoMask |= UaBrowseAdditionalInfo.GET_DEFINITION_TASK;
                    }
                }
            }
        }       

        return new UaBrowseAdditionalInfo(
            maxReferencesPerNode,
            0,
            browseInfoMask,
        );
    }

    isTaskRequired(taskMask: number): boolean {
        return (this._taskCheckListMasks & taskMask) !== 0;
    }

    taskComplete(taskMask: number) {
        if (!this.isTaskRequired(taskMask)) {
            return this;
        }

        this._taskCheckListMasks = this._taskCheckListMasks & ~taskMask;
    }

    isAllTaskComplete(): boolean {
        return this._taskCheckListMasks === 0;
    }
}
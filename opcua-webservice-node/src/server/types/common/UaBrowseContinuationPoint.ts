import { BrowseDirection } from "opcua-webapi";
import { parseUaNodeIdOrNull, UaBrowseDescription, UaNodeId } from "opcua-webapi-ts";
import { UaBrowseAdditionalInfo } from "./UaBrowseAdditionalInfo";

export type SerializedBrowseContinuationPoint = {
    id: string;
    bDir: number;
    refId: string;
    includeSt: boolean;
    ncMask: number;
    rltMask: number;
    maxRef: number;
    offset: number;
    taskMsk: number;
};

export class UaBrowseContinuationPoint {
    private _id: string;
    private _browseDirection: number;
    private _referenceTypeId: string;
    private _includeSubtypes: boolean;
    private _nodeClassMask: number;
    private _resultMask: number;
    private _maxReferencesPerNode: number;
    private _referenceOffset: number;
    private _taskCheckListMasks: number;

    constructor(description: UaBrowseDescription, additionalInfo: UaBrowseAdditionalInfo) {
        this._id = description.nodeId.toString();
        this._browseDirection = description.browseDirection;
        this._referenceTypeId = description.referenceTypeId.toString();
        this._includeSubtypes = description.includeSubtypes;
        this._nodeClassMask = description.nodeClassMask;
        this._resultMask = description.resultMask;
        this._maxReferencesPerNode = additionalInfo.maxReferencesPerNode;
        this._referenceOffset = additionalInfo.referenceOffset;
        this._taskCheckListMasks = additionalInfo.taskCheckListMasks;
    }

    get browseDescription(): UaBrowseDescription | null {
        const nodeId = parseUaNodeIdOrNull(this._id);
        const referenceTypeId = parseUaNodeIdOrNull(this._referenceTypeId);

        if (nodeId === null || referenceTypeId === null) return null;

        return new UaBrowseDescription(
            nodeId,
            this._browseDirection,
            referenceTypeId,
            this._includeSubtypes,
            this._nodeClassMask,
            this._resultMask,
        );
    }

    get additionalInfo(): UaBrowseAdditionalInfo {
        return new UaBrowseAdditionalInfo(
            this._maxReferencesPerNode,
            this._referenceOffset,
            this._taskCheckListMasks,
        );
    }

    toByteString(): string {
        const serialized: SerializedBrowseContinuationPoint = {
            id: this._id,
            bDir: this._browseDirection,
            refId: this._referenceTypeId,
            includeSt: this._includeSubtypes,
            ncMask: this._nodeClassMask,
            rltMask: this._resultMask,
            maxRef: this._maxReferencesPerNode,
            offset: this._referenceOffset,
            taskMsk: this._taskCheckListMasks,
        };

        return Buffer.from(JSON.stringify(serialized), "utf-8").toString("base64");
    }

    static fromByteString(continuationPoint: string): UaBrowseContinuationPoint | null {
        if (!continuationPoint) {
            return null;
        }

        try {
            const serialized = JSON.parse(
                Buffer.from(continuationPoint, "base64").toString("utf-8"),
            ) as Partial<SerializedBrowseContinuationPoint>;

            if (!serialized || typeof serialized !== "object") return null;
            if (typeof serialized.bDir !== "number") return null;
            if (typeof serialized.includeSt !== "boolean") return null;
            if (typeof serialized.ncMask !== "number") return null;
            if (typeof serialized.rltMask !== "number") return null;
            if (typeof serialized.maxRef !== "number") return null;
            if (typeof serialized.offset !== "number") return null;
            if (typeof serialized.taskMsk !== "number") return null;

            const nodeId = parseUaNodeIdOrNull(serialized.id);
            const referenceTypeId = parseUaNodeIdOrNull(serialized.refId);

            if (nodeId === null || referenceTypeId === null) {
                return null;
            }

            const browseDescription = new UaBrowseDescription(
                nodeId,
                serialized.bDir,
                referenceTypeId,
                serialized.includeSt,
                serialized.ncMask,
                serialized.rltMask,
            );

            const additionalInfo = new UaBrowseAdditionalInfo(
                serialized.maxRef,
                serialized.offset,
                serialized.taskMsk,
            );

            return new UaBrowseContinuationPoint(browseDescription, additionalInfo);
        } catch {
            return null;
        }
    }
}
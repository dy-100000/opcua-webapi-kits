import { Attributes, BrowseDirection, NodeClass, StatusCodes, TimestampsToReturn } from "opcua-webapi";
import {
    isBrowseMaskRequired,
    makeUaStatusCode,
    UaBrowseDescription,
    UaBrowseMask,
    UaBrowseResult,
    UaDataValue,
    UaError,
    UaExpandedNodeId,
    UaLocalizedText,
    UaNodeId,
    UaReferenceDescription,
    UaVariant,
} from "opcua-webapi-ts";
import { UaBrowseAdditionalInfo, UaBrowseContinuationPoint } from "../../types";
import { UaNode, UaObject, UaReference, UaVariable } from "../nodes";
import { NodeManagerBase } from "./NodeManagerBase";
import { NodeManagerList } from "./NodeManagerList";

export abstract class NodeManager extends NodeManagerBase {
    private readonly _nsIndex: number;
    private readonly _namespaceUri: string;
    private readonly _nodes: Map<string, UaNode>;

    constructor(namespaceUri: string) {
        super();
        this._nsIndex = NodeManagerList.nodeManagerList.getNewNsIndex();
        this._namespaceUri = namespaceUri;
        this._nodes = new Map<string, UaNode>();        
    }   

    nsIndex(): number {
        return this._nsIndex;
    }

    namespaceUri(): string {
        return this._namespaceUri;
    }

    getNode(nodeId: UaNodeId): UaNode | null {
        if (this.nsIndex() === nodeId.nsIndex) {
            return this._nodes.get(nodeId.toString()) ?? null;
        }

        const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nodeId.nsIndex);
        if (nodeManager === null) {
            return null;
        }

        return nodeManager.getNode(nodeId);
    }

    addNode(node: UaNode): void {
        if (this._nodes.has(node.nodeId.toString())) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdExists));
        }

        if (node.nodeId.nsIndex !== this.nsIndex()) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdInvalid));
        }

        this._nodes.set(node.nodeId.toString(), node);
    }

    browseNode(
        nodeId: UaNodeId,
        browseDirection: number,
        referenceTypeId: UaNodeId,
        includeSubtypes: boolean,
        nodeClassMask: number): Array<UaReference> | null {
        const nodeToBrowse = this.getNode(nodeId);
        if (nodeToBrowse === null) return null;
  
        const references = nodeToBrowse.getReferences(browseDirection as BrowseDirection);
        const refsToReturn: Array<UaReference> = [];

        for (const item of references) {
            if (((item.linkedNode.nodeClass as number) & nodeClassMask) === 0) {
                continue;
            }

            if (includeSubtypes) {
                if (!item.reference.isSubtypeOf(referenceTypeId)) {
                    continue;
                }
            } else if (!item.reference.nodeId.equal(referenceTypeId)) {
                continue;
            }

            refsToReturn.push(item);
        }

        return refsToReturn;
    }

    browse(
        browseDescription: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
    ): UaBrowseResult {
        const referencesOfNode = this.browseNode(
            browseDescription.nodeId,
            browseDescription.browseDirection,
            browseDescription.referenceTypeId,
            browseDescription.includeSubtypes,
            browseDescription.nodeClassMask,
        );

        if (referencesOfNode === null) {
            return new UaBrowseResult([], null, makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
        }

        let isContinuationPointRequired = false;
        const fromIndex = additionalInfo.referenceOffset;
        let toIndex = referencesOfNode.length;

        if (referencesOfNode.length <= fromIndex) {
            return new UaBrowseResult([], null, makeUaStatusCode(StatusCodes.Good));
        }

        if (additionalInfo.maxReferencesPerNode > 0) {
            toIndex = fromIndex + additionalInfo.maxReferencesPerNode;
            if (toIndex >= referencesOfNode.length) {
                toIndex = referencesOfNode.length;
            } else {
                isContinuationPointRequired = true;
            }
        }

        const referencesToReturn = referencesOfNode.slice(fromIndex, toIndex);
        const references: Array<UaReferenceDescription> = [];

        for (const item of referencesToReturn) {
            const linkedNode = item.linkedNode;
            const resultMask = browseDescription.resultMask;

            let refTypeId: UaNodeId | undefined;
            let browseName: string | undefined;
            let displayName : UaLocalizedText | undefined;
            let nodeClass: number | undefined;
            let typeDefinition: UaExpandedNodeId | undefined;            

            if (isBrowseMaskRequired(resultMask , UaBrowseMask.ReferenceTypeId)) {
                refTypeId = item.reference.nodeId;
            }

            if (isBrowseMaskRequired(resultMask , UaBrowseMask.BrowseName)) {
                browseName = linkedNode.browseName;
            }

            if (isBrowseMaskRequired(resultMask , UaBrowseMask.DisplayName)) {
                displayName = linkedNode.displayName;
            }

            if (isBrowseMaskRequired(resultMask , UaBrowseMask.NodeClass)) {
                nodeClass = linkedNode.nodeClass;
            }

            if (isBrowseMaskRequired(resultMask , UaBrowseMask.TypeDefinition)) {
                if (linkedNode.nodeClass === NodeClass.Object) {
                    typeDefinition = UaExpandedNodeId.from((linkedNode as UaObject).typeDefinition.nodeId);
                } else if (linkedNode.nodeClass === NodeClass.Variable) {
                    typeDefinition = UaExpandedNodeId.from((linkedNode as UaVariable).typeDefinition.nodeId);
                }
            }

            references.push(
                new UaReferenceDescription(
                    UaExpandedNodeId.from(linkedNode.nodeId),
                    nodeClass,
                    browseName,
                    displayName,
                    refTypeId,
                    item.isForward,
                    typeDefinition,
                ),
            );
        }

        let continuationPoint: string | null = null;
        if (isContinuationPointRequired) {
            const cp = new UaBrowseContinuationPoint(
                browseDescription,
                new UaBrowseAdditionalInfo(additionalInfo.maxReferencesPerNode, toIndex, 0),
            );
            continuationPoint = cp.toByteString();
        }

        return new UaBrowseResult(references, continuationPoint, makeUaStatusCode(StatusCodes.Good));
    }

    read(
        nodeId: UaNodeId,
        attributeId: number,
        now: Date,
        timestampsToReturn: TimestampsToReturn,
    ): UaDataValue {
        const nodeToRead = this.getNode(nodeId);

        let value = UaVariant.null();
        let statusCode = makeUaStatusCode(StatusCodes.Good);
        let sourceTimestamp: Date | undefined;
        let serverTimestamp: Date | undefined;

        if (nodeToRead !== null) {
            value = nodeToRead.getAttribute(attributeId);
            if (value.isNull() && Attributes.Value !== attributeId) {
                statusCode = makeUaStatusCode(StatusCodes.BadAttributeIdInvalid);
            }
        } else {
            statusCode = makeUaStatusCode(StatusCodes.BadNodeIdUnknown);
        }

        if (statusCode.isGood() && Attributes.Value === attributeId) {
            if (timestampsToReturn === TimestampsToReturn.Both ||
                timestampsToReturn === TimestampsToReturn.Source) {
                sourceTimestamp = now;
            }

            if (timestampsToReturn === TimestampsToReturn.Both ||
                timestampsToReturn === TimestampsToReturn.Server) {
                serverTimestamp = now;
            }
        }

        return new UaDataValue(value, statusCode, sourceTimestamp, serverTimestamp);
    }
}

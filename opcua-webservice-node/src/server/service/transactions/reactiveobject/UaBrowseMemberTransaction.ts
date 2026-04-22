import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaBrowseDescription, UaExpandedNodeId, UaReferenceDescription, UaNodeId } from "opcua-webapi-ts";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaReactiveObjectType } from "../../../addressspace/reactiveobject/UaReactiveObjectType";
import {
    UaBrowseAdditionalInfo,
    UaChildIdentifier,
    UaInstanceIdentifier,
    UaObjectId,
    UaObjectIdentifier,
} from "../../../types";
import { BrowseMemberRequest, BrowseMemberResponse } from "../../message";
import { UaBrowseTransaction } from "../base/UaBrowseTransaction";
import { ServiceContext } from "../../..";

export class UaBrowseMemberTransaction extends UaBrowseTransaction {
    private readonly objectType: UaReactiveObjectType;
    private readonly objectId: UaObjectId;
    private readonly memberId: UaChildIdentifier;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        serviceContext: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
        objectType: UaReactiveObjectType,
        objectId: UaObjectId,
        memberId: UaChildIdentifier,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this.objectType = objectType;
        this.objectId = objectId;
        this.memberId = memberId;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            if (!this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) || this.memberId.pathL2 !== null) {
                return;
            }

            const isMethod = this.memberId.methodNode;
            const request = new BrowseMemberRequest(
                this.objectId,
                this.memberId.path,
                isMethod,
                this.getItem(),
            );

            const response = await this.objectType.onBrowseMemberChildren(request);
            this.browseMemberChildResult(isMethod, response);
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private browseMemberChildResult(isMethodParent: boolean, response: BrowseMemberResponse): void {
        for (const item of response.children) {
            if (item.id.length === 0 || !item.isForward || item.nodeClass !== NodeClass.Variable) {
                continue;
            }

            const objectIdentifier = new UaObjectIdentifier(
                this.objectType.nodeId.toString(),
                this.objectId.id,
                this.objectId.instance?.nodeId.toString() ?? null,
            );

            const memberIdentifier = new UaChildIdentifier(
                this.memberId.path,
                item.id,
                isMethodParent,
            );

            const newIdentifier = new UaInstanceIdentifier(objectIdentifier, memberIdentifier);
            const byteString = newIdentifier.toByteString();
            if (byteString === null) {
                this._statusCode = makeUaStatusCode(StatusCodes.BadNodeIdInvalid);
                continue;
            }

            this._references.push(
                new UaReferenceDescription(
                    UaExpandedNodeId.from(new UaNodeId(byteString, this.nodeManager.nsIndex())),
                    NodeClass.Variable,
                    item.browseName,
                    item.displayName,
                    item.referenceTypeId,
                    item.isForward,
                    UaExpandedNodeId.from(item.typeDefinitionId),
                ),
            );
        }
    }
}

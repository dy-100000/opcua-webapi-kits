import { NodeClass } from "opcua-webapi";
import { UaBrowseDescription, UaExpandedNodeId, UaReferenceDescription, UaNodeId, UaNodeIdType } from "opcua-webapi-ts";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaReactiveObjectType } from "../../../addressspace/reactiveobject/UaReactiveObjectType";
import {
    UaBrowseAdditionalInfo,
    UaBrowseContinuationPoint,
    UaChildIdentifier,
    UaInstanceIdentifier,
    UaObjectId,
    UaObjectIdentifier,
} from "../../../types";
import { BrowseObjectRequest, BrowseObjectResponse } from "../../message";
import { UaBrowseTransaction } from "../base/UaBrowseTransaction";
import { ServiceContext, UaReferenceType } from "../../..";

export class UaBrowseObjectTransaction extends UaBrowseTransaction {
    private readonly objectType: UaReactiveObjectType;
    private readonly objectId: UaObjectId;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        serviceContext: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
        objectType: UaReactiveObjectType,
        objectId: UaObjectId,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this.objectType = objectType;
        this.objectId = objectId;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const request = new BrowseObjectRequest(
                this.objectId,
                this._additionalInfo,
                this.getItem(),
            );

            if (this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_DEFINITION_TASK)) {
                this.addTypeDefinitionReference(
                    this.objectType.nodeId,
                    this.objectType.browseName,
                    this.objectType.displayName);
                this._additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_DEFINITION_TASK);
            }

            let referenceType = this.nodeManager.getNode(this.objectType.supportedReferenceType());
            let includeSubtypes = this.getItem().includeSubtypes;

            if (referenceType === null || referenceType.nodeClass != NodeClass.ReferenceType)
            {
                this._additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK);
            } else {
                if (includeSubtypes) {
                    if (!(referenceType as UaReferenceType).isSubtypeOf(this.getItem().referenceTypeId))
                    {
                        this._additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK);
                    }
                } else {
                    if (!referenceType.nodeId.equal(this.getItem().referenceTypeId))
                    {
                        this._additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK);
                    }
                }                
            }

            if (this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_RELATED_OBJECT_TASK) ||
                this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) ||
                this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK))
            {
                const response = await this.objectType.onBrowseObject(request);
                this.browseObjectResult(response);
            }
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private browseObjectResult(response: BrowseObjectResponse)
    {
        for (const item of response.children)
        {
            if (item.nodeClass == NodeClass.ObjectType)
            {
                this._references.push(new UaReferenceDescription(
                        UaExpandedNodeId.from(item.typeDefinitionId),
                        item.nodeClass,
                        item.browseName,
                        item.displayName,
                        item.referenceTypeId,
                        true,
                        UaExpandedNodeId.from(UaNodeId.nullNodeId)));

                continue;
            }

            if (item.id.length === 0) continue;

            if (item.nodeClass != NodeClass.Object &&
                    item.nodeClass != NodeClass.Variable &&
                    item.nodeClass != NodeClass.Method) continue;

            let newIdentifier : UaInstanceIdentifier;

            if (item.nodeClass == NodeClass.Object)
            {
                let objectIdentifier = new UaObjectIdentifier(
                        item.typeDefinitionId.toString(),
                        item.id,
                        (!item.instanceDeclarationId.isEmpty()) ? item.instanceDeclarationId.toString() : null);

                newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        null);
            } else {
                let objectIdentifier = new UaObjectIdentifier(
                        this.objectType.nodeId.toString(),
                        this.objectId.id,
                        (this.objectId.instance) ? this.objectId.instance.nodeId.toString() : null);

                let memberIdentifier = new UaChildIdentifier(
                        item.id,
                        null);

                newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        memberIdentifier);
            }

            let newNodeId = new UaNodeId(
                newIdentifier.toByteString(),
                this.nodeManager.nsIndex(),
                UaNodeIdType.BYTESTRING);
                
            this._references.push(new UaReferenceDescription(
                    UaExpandedNodeId.from(newNodeId),
                    item.nodeClass,
                    item.browseName,
                    item.displayName,
                    item.referenceTypeId,
                    true,
                    UaExpandedNodeId.from(item.typeDefinitionId)));
        }

        if (0 != response.remainingTasks && 0 != this._references.length)
        {
            this._additionalInfo.taskCheckListMasks = response.remainingTasks;
            this._additionalInfo.referenceOffset = response.offset;
        } else {
            this._additionalInfo.taskCheckListMasks = 0;
            this._additionalInfo.referenceOffset = 0;
        }

        if (!this._additionalInfo.isAllTaskComplete())
        {
            this._continuationPoint = new UaBrowseContinuationPoint(this.getItem(), this._additionalInfo).toByteString();
        }

        return null;
    }
}

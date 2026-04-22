import { NodeClass } from "opcua-webapi";
import { UaBrowseDescription, UaExpandedNodeId, UaReferenceDescription, UaNodeId } from "opcua-webapi-ts";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaReactiveObjectType } from "../../../addressspace/reactiveobject/UaReactiveObjectType";
import {
    UaBrowseAdditionalInfo,
    UaChildIdentifier,
    UaInstanceIdentifier,
    UaObjectId,
    UaObjectIdentifier,
} from "../../../types";
import { BrowseObjectRequest, BrowseObjectResponse } from "../../message";
import { UaBrowseTransaction } from "../base/UaBrowseTransaction";
import { ServiceContext } from "../../..";

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
                    this.objectType.displayName,
                );
            }

            if (this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_TASK)) {
                const response = await this.objectType.onBrowseObjectChildren(request);
                this.browseObjectResult(response);
            }

            if (this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_PARENT_TASK)) {
                const response = await this.objectType.onBrowseObjectParent(request);
                this.browseObjectResult(response);
            }

            if (this._additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_LINK_TASK)) {
                const response = await this.objectType.onBrowseObjectLinks(request);
                this.browseObjectResult(response);
            }
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private browseObjectResult(response: BrowseObjectResponse): void {
        for (const item of response.children) {
            if (item.id.length === 0) {
                continue;
            }

            const objectIdentifier = new UaObjectIdentifier(
                this.objectType.nodeId.toString(),
                this.objectId.id,
                this.objectId.instance?.nodeId.toString() ?? null,
            );

            const childIdentifier = this.toChildIdentifier(item);
            const nodeId = childIdentifier === null
                ? new UaNodeId(item.id, this.nodeManager.nsIndex())
                : new UaNodeId(new UaInstanceIdentifier(objectIdentifier, childIdentifier).toByteString() ?? "", this.nodeManager.nsIndex());

            if (nodeId.value === "") {
                continue;
            }

            this._references.push(
                new UaReferenceDescription(
                    UaExpandedNodeId.from(nodeId),
                    item.nodeClass,
                    item.browseName,
                    item.displayName,
                    item.referenceTypeId,
                    item.isForward,
                    UaExpandedNodeId.from(item.typeDefinitionId),
                ),
            );
        }
    }

    private toChildIdentifier(item: BrowseObjectResponse["children"][number]): UaChildIdentifier | null {
        if (item.nodeClass === NodeClass.Object) {
            return new UaChildIdentifier(item.id, null, false);
        }
        if (item.nodeClass === NodeClass.Variable) {
            return new UaChildIdentifier(item.id, null, false);
        }
        if (item.nodeClass === NodeClass.Method) {
            return new UaChildIdentifier(item.id, null, true);
        }

        return null;
    }
}

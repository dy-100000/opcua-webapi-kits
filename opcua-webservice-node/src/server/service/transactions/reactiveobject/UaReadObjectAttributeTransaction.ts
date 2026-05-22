import { Attributes, NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaError, UaVariant, UaVariantType } from "opcua-webapi-ts";
import { ReadContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaObjectId, UaObjectIdentifier } from "../../../types";
import { ReadObjectAttributeRequest, ReadObjectAttributeResponse } from "../../message";
import { UaReadTransaction } from "../base/UaReadTransaction";

export class UaReadObjectAttributeTransaction extends UaReadTransaction {
    private readonly objectId: UaObjectIdentifier;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        context: ReadContext,
        handleIds: Array<number>,
        objectId: UaObjectIdentifier,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleIds);
        this.objectId = objectId;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectId);
            if (objectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectId);
            const request = new ReadObjectAttributeRequest(
                new UaObjectId(this.objectId.id, instanceDeclaration),
            );

            const response = await objectType.onReadObjectAttributes(request);
            this.setResults(response);
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private setResults(response: ReadObjectAttributeResponse): void {
        const nodesToRead = this.getRequestedItems();
        this._results = [];

        for (const item of nodesToRead) {
            let value = UaVariant.null();
            let statusCode = makeUaStatusCode(StatusCodes.Good);

            if (item.attributeId === Attributes.DisplayName) {
                value = UaVariant.localizedText(response.displayName);
            } else if (item.attributeId === Attributes.Description) {
                if (response.description.text.length === 0) {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                } else {
                    value = UaVariant.localizedText(response.description);
                }
            } else if (item.attributeId === Attributes.EventNotifier) {
                value = UaVariant.integer(response.eventNotifier, UaVariantType.Byte);
            } else if (item.attributeId === Attributes.NodeId) {
                value = UaVariant.nodeId(item.nodeId);
            } else if (item.attributeId === Attributes.BrowseName) {
                value = UaVariant.qualifiedName(response.browseName);
            } else if (item.attributeId === Attributes.NodeClass) {
                value = UaVariant.integer(NodeClass.Object, UaVariantType.UInt32);
            } else if (item.attributeId === Attributes.WriteMask || item.attributeId === Attributes.UserWriteMask) {
                value = UaVariant.integer(0, UaVariantType.UInt32);
            } else {
                statusCode = makeUaStatusCode(StatusCodes.BadAttributeIdInvalid);
            }

            this._results.push(new UaDataValue(value, statusCode));
        }
    }
}
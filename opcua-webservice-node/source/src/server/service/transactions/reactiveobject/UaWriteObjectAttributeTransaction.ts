import { Attributes, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaArrayType, UaError, UaLocalizedText, UaVariant, UaVariantType } from "opcua-webapi-ts";
import { UaObjectId, WriteContext, WriteObjectAttributeRequest, WriteObjectAttributeResponse } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaObjectIdentifier } from "../../../types";
import { UaWriteTransaction } from "../base/UaWriteTransaction";

export class UaWriteObjectAttributeTransaction extends UaWriteTransaction {
    private readonly objectId: UaObjectIdentifier;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        context: WriteContext,
        handleIds: Array<number>,
        objectId: UaObjectIdentifier,
        nodeManager: NodeManagerReactiveObject) {
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

            let displayName : UaLocalizedText | null = null;
            let description : UaLocalizedText | null = null;

            const requests = this.getRequestedItems();
            for (const request of requests) {
                if (request.attributeId === Attributes.DisplayName && 
                    request.value.type === UaVariantType.LocalizedText &&
                    request.value.arrayType === UaArrayType.Scalar) {
                    displayName = request.value.value as UaLocalizedText;
                } else if (request.attributeId === Attributes.Description && 
                    request.value.type === UaVariantType.LocalizedText &&
                    request.value.arrayType === UaArrayType.Scalar) {
                    description = request.value.value as UaLocalizedText;
                }
            }

            if (displayName === null && description === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNotWritable));
            }
            
            const request = new WriteObjectAttributeRequest(
                new UaObjectId(this.objectId.id, null),
                displayName,
                description
            );

            const response = await objectType.onWriteObjectAttributes(request);
            this.setResults(response);
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private setResults(response: WriteObjectAttributeResponse): void {
        const nodesToWrite = this.getRequestedItems();

        for (const item of nodesToWrite) {
            let statusCode;

            if (item.attributeId === Attributes.DisplayName) {
                statusCode = response.writeDisplayNameStatusCode;
            } else if (item.attributeId === Attributes.Description) {
                statusCode = response.writeDescriptionStatusCode;
            } else {
                statusCode = makeUaStatusCode(StatusCodes.BadNotWritable);
            }

            this._results.push(statusCode);
        }
    }
}
import { Attributes, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaError, UaReadValueId, UaVariant, UaVariantType } from "opcua-webapi-ts";
import { ReadContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaChildIdentifier, UaChildId, UaObjectId, UaObjectIdentifier } from "../../../types";
import { ReadMemberAttributeRequest, ReadMemberAttributeResponse } from "../../message";
import { UaReadTransaction } from "../base/UaReadTransaction";

export class UaReadMemberAttributeTransaction extends UaReadTransaction {
    private readonly objectId: UaObjectIdentifier;
    private readonly memberId: UaChildIdentifier;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        context: ReadContext,
        objectId: UaObjectIdentifier,
        memberId: UaChildIdentifier,
        handleIds: Array<number>,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleIds);
        this.objectId = objectId;
        this.memberId = memberId;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectId);
            if (objectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectId);
            const request = new ReadMemberAttributeRequest(
                new UaObjectId(this.objectId.id, instanceDeclaration),
                new UaChildId(this.memberId.path, this.memberId.pathL2),
                this.memberId.methodNode,
            );

            const response = await objectType.onReadMemberAttributes(request);
            this.setResults(response);
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private setResults(response: ReadMemberAttributeResponse): void {
        const nodesToRead = this.getRequestedItems();
        this._results = [];

        for (const item of nodesToRead) {
            let value = UaVariant.null();
            let statusCode = makeUaStatusCode(StatusCodes.Good);

            if (item.attributeId === Attributes.NodeClass) {
                value = UaVariant.integer(response.nodeClass, UaVariantType.UInt32);
            } else if (item.attributeId === Attributes.DisplayName) {
                value = UaVariant.localizedText(response.displayName);
            } else if (item.attributeId === Attributes.Description) {
                if (response.description.text.length !== 0) {
                    value = UaVariant.localizedText(response.description);
                } else {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                }
            } else if (item.attributeId === Attributes.DataType) {
                if (response.dataTypeId !== null) {
                    value = UaVariant.nodeId(response.dataTypeId);
                } else {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                }
            } else if (item.attributeId === Attributes.ValueRank) {
                if (response.valueRank !== null) {
                    value = UaVariant.integer(response.valueRank, UaVariantType.Int32);
                } else {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                }
            } else if (item.attributeId === Attributes.AccessLevel || item.attributeId === Attributes.UserAccessLevel) {
                if (response.accessLevel !== null) {
                    value = UaVariant.integer(response.accessLevel, UaVariantType.Byte);
                } else {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                }
            } else if (item.attributeId === Attributes.Historizing) {
                if (response.historizing !== null) {
                    value = UaVariant.boolean(response.historizing);
                } else {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                }
            } else if (item.attributeId === Attributes.Value) {
                if (response.value !== null) {
                    value = response.value;
                } else {
                    statusCode = makeUaStatusCode(StatusCodes.BadNodeAttributesInvalid);
                }
            } else if (item.attributeId === Attributes.NodeId) {
                value = UaVariant.nodeId(item.nodeId);
            } else if (item.attributeId === Attributes.BrowseName) {
                value = UaVariant.qualifiedName(response.browseName);
            } else if (item.attributeId === Attributes.Executable || item.attributeId === Attributes.UserExecutable) {
                value = UaVariant.boolean(true);
            } else if (item.attributeId === Attributes.WriteMask || item.attributeId === Attributes.UserWriteMask) {
                value = UaVariant.integer(0, UaVariantType.UInt32);
            } else {
                statusCode = makeUaStatusCode(StatusCodes.BadAttributeIdInvalid);
            }

            this._results.push(new UaDataValue(value, statusCode));
        }
    }
}
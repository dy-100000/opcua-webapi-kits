import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError, UaHistoryData } from "opcua-webapi-ts";
import { HistoryReadContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaChildId, UaHistoryReadContinuationPoint, UaInstanceIdentifier, UaObjectId } from "../../../types";
import { ReadHistoryDataRequest, ReadHistoryDataResponse } from "../../message";
import { UaHistoryReadTransaction } from "../base/UaHistoryReadTransaction";

export class UaReadDataHistoryTransaction extends UaHistoryReadTransaction {
    private readonly nodeManager: NodeManagerReactiveObject;
    private readonly objectIdentifier: UaInstanceIdentifier;
    private readonly offset: number;

    constructor(
        historyReadContext: HistoryReadContext,
        index: number,
        objectIdentifier: UaInstanceIdentifier,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(historyReadContext, index);
        this.objectIdentifier = objectIdentifier;
        this.nodeManager = nodeManager;

        const continuationPoint = this.getItem().continuationPoint;
        if (!continuationPoint) {
            this.offset = 0;
            return;
        }

        const parsedContinuationPoint = UaHistoryReadContinuationPoint.fromByteString(continuationPoint);
        this.offset = parsedContinuationPoint?.offset ?? -1;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectIdentifier.objectId);
            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectIdentifier.objectId);
            const childIdentifier = this.objectIdentifier.childId;

            if (objectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            if (this.offset < 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadContinuationPointInvalid));
            }

            if (childIdentifier === null || childIdentifier.pathL2 !== null || childIdentifier.methodNode) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdInvalid));
            }

            const request = new ReadHistoryDataRequest(
                new UaObjectId(this.objectIdentifier.objectId.id, instanceDeclaration),
                new UaChildId(childIdentifier.path),
                this.details,
                this.offset,
            );

            const response = await objectType.onReadHistoryData(request);
            this.setResults(response);
        } catch (error) {
            this._historyData = undefined;
            this._continuationPoint = undefined;
            this._statusCode = makeUaStatusCode(StatusCodes.BadUnexpectedError);

            if (error instanceof UaError) {
                this._statusCode = error.statusCode;
            }
        }
    }

    private setResults(response: ReadHistoryDataResponse): void {
        this._historyData = new UaHistoryData(response.values).toExtensionObject();
        this._continuationPoint = undefined;

        if (response.containsMoreData && response.values.length > 0) {
            this._continuationPoint = new UaHistoryReadContinuationPoint(
                this.offset + response.values.length).toByteString();
        }
    }
}
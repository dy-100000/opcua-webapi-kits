import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError, UaHistoryEvent } from "opcua-webapi-ts";
import { HistoryReadContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaHistoryReadContinuationPoint, UaInstanceIdentifier, UaObjectId } from "../../../types";
import { ReadHistoryEventRequest, ReadHistoryEventResponse } from "../../message";
import { UaHistoryReadTransaction } from "../base/UaHistoryReadTransaction";

export class UaReadEventHistoryTransaction extends UaHistoryReadTransaction {
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

            if (objectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            if (this.offset < 0) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadContinuationPointInvalid));
            }

            const request = new ReadHistoryEventRequest(
                new UaObjectId(this.objectIdentifier.objectId.id, instanceDeclaration),
                this.details,
                this.offset,
            );

            const response = await objectType.onReadHistoryEvent(request);
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

    private setResults(response: ReadHistoryEventResponse): void {
        this._historyData = new UaHistoryEvent(response.events).toExtensionObject();
        this._continuationPoint = undefined;

        if (response.containsMoreData && response.events.length > 0) {
            this._continuationPoint = new UaHistoryReadContinuationPoint(
                this.offset + response.events.length).toByteString();
        }
    }
}
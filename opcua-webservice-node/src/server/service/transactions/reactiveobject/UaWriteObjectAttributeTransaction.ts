import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode } from "opcua-webapi-ts";
import { WriteContext } from "../../..";
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
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, handleIds);
        this.objectId = objectId;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        this._results.length = 0;

        for (const _handleId of this.handleIds) {
            this._results.push(makeUaStatusCode(StatusCodes.BadNotWritable));
        }
    }
}
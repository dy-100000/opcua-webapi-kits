import { StatusCodes, TimestampsToReturn } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaError, UaVariant } from "opcua-webapi-ts";
import { NodeManager } from "../../../addressspace/nodemanager";
import { ReadContext } from "../../..";
import { UaReadTransaction } from "./UaReadTransaction";

export class UaReadNodeTransaction extends UaReadTransaction {
    private readonly _nodeManager: NodeManager;

    constructor(
        context: ReadContext,
        handleIds: Array<number>,
        nodeManager: NodeManager,
    ) {
        super(context, handleIds);
        this._nodeManager = nodeManager;
    }

    async execute(): Promise<void> {
        try {
            const now = new Date();
            const nodesToRead = this.getRequestedItems();

            for (const item of nodesToRead) {
                const result = this._nodeManager.read(
                    item.nodeId,
                    item.attributeId,
                    now,
                    this.timestampsToReturn as TimestampsToReturn,
                );
                this._results.push(result);
            }
        } catch (error) {
            this.buildErrorResults(error);
        }
    }
}
import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaBrowseDescription, UaNodeId } from "opcua-webapi-ts";
import { UaNode } from "../nodes";
import { CallContext, HistoryReadContext, ReadContext, ServiceContext, UaBrowseAdditionalInfo, WriteContext } from "../../types";
import { UaBrowseTransaction, UaHistoryReadTransaction, UaMethodCallTransaction, UaReadTransaction, UaWriteTransaction } from "../../service/transactions";

export abstract class NodeManagerBase {
        abstract nsIndex(): number;

        abstract namespaceUri(): string;

        abstract getNode(nodeId: UaNodeId): UaNode | null;

        async onStartUp(): Promise<void> {
                return;
        }

        async onShutDown(): Promise<void> {
                return;
        }

        getBrowseTransaction(
                context: ServiceContext,
                nodeToBrowse: UaBrowseDescription,
                additionalInfo: UaBrowseAdditionalInfo,
                handleId: number): UaBrowseTransaction {

                const transaction = new UaBrowseTransaction(
                                context,
                                nodeToBrowse,
                                additionalInfo,
                                handleId);

                transaction.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
                return transaction;
        }

        getReadTransactions(
                context: ReadContext,
                handleIds: Array<number>): Array<UaReadTransaction> {
                return [new UaReadTransaction(context, handleIds)];
        }

        getWriteTransactions(
                context: WriteContext,
                handleIds: Array<number>): Array<UaWriteTransaction> {
                return [new UaWriteTransaction(context, handleIds)];
        }

        getMethodCallTransaction(
                context: CallContext,
                handleId: number): UaMethodCallTransaction {
                return new UaMethodCallTransaction(context, handleId);
        }

        getHistoryReadTransaction(
                context: HistoryReadContext,
                handleId: number): UaHistoryReadTransaction {
                return new UaHistoryReadTransaction(context, handleId);
        }
}
import { ApplicationDescription, BrowseDirection, StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaBrowseDescription,
    UaBrowseResult,
    UaCallMethodRequest,
    UaCallMethodResult,
    UaDataValue,
    UaError,
    UaHistoryReadResult,
    UaHistoryReadValueId,
    UaNodeId,
    UaReadValueId,
    UaStatusCode,
    UaWriteValue,
} from "opcua-webapi-ts";
import { UaExpressServer } from "./UaExpressServer";
import { UaWebServerBase } from "./UaWebServerBase";
import {
    BrowseContext,
    BrowseNextContext,
    CallContext,
    HistoryReadContext,
    NodeManagerBase,
    NodeManagerList,
    ReadContext,
    UaBrowseAdditionalInfo,
    UaBrowseContinuationPoint,
    UaBrowseTransaction,
    UaHistoryReadTransaction,
    UaMethodCallTransaction,
    UaReadTransaction,
    UaTransactionManager,
    UaTransactionManager2,
    UaWriteTransaction,
    WriteContext,
} from "./server";

import { NodeManagerNs0,NodeManagerNs1 } from "./server/addressspace/nodemanager";

export abstract class UaWebServer extends UaWebServerBase {
    constructor(server: UaExpressServer) {
        super(server);
    }

    addNodeManager(nodeManager: NodeManagerBase): void {
        NodeManagerList.nodeManagerList.addNodeManager(nodeManager);
    }

    async start(): Promise<void> {
        try {
            console.log("Starting OPC UA WebServer ...");
            await this.startUp();
            this.expressServer.start();
        } catch (err) {
            console.error("OPC UA WebServer failure", err);
        }
    }

    async startUp(): Promise<void> {
        const ns0 = new NodeManagerNs0();
        NodeManagerList.nodeManagerList.addNodeManager(ns0);

        const ns1 = new NodeManagerNs1();
        NodeManagerList.nodeManagerList.addNodeManager(ns1);

        await this.onStartUp();

        const nsIndexes = NodeManagerList.nodeManagerList.getNsIndexes();        
        const namespaceUris: string[] = [];

        for (const nsIndex of nsIndexes) {
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nsIndex);
            if (nodeManager === null) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));

            await nodeManager.onStartUp();
            namespaceUris.push(nodeManager.namespaceUri());
        }

        ns0.updateNamespaceArray(namespaceUris);
        ns0.updateServerArray([this.getServerConfigure().applicationUri]);
    }

    async shutDown(): Promise<void> {
        const nsIndexes = NodeManagerList.nodeManagerList.getNsIndexes();

        for (const nsIndex of nsIndexes) {
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nsIndex);
            if (nodeManager === null) {
                continue;
            }

            await nodeManager.onShutDown();
        }

        await this.onShutDown();
    }   

    async browse(context: BrowseContext): Promise<Array<UaBrowseResult>> {
        const transactionManager = new UaTransactionManager<UaBrowseDescription,UaBrowseResult>();

        let currentIndex = 0;
        for (const item of context.nodesToBrowse) {
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(item.nodeId.nsIndex);

            let transaction: UaBrowseTransaction;
            let additionalInfo = new UaBrowseAdditionalInfo(
                context.requestedMaxReferencesPerNode,
                0,
                0);

            additionalInfo = additionalInfo.updateTasks(item);

            if (nodeManager !== null) {
                transaction = nodeManager.getBrowseTransaction(
                    context,
                    item,
                    additionalInfo,
                    currentIndex);
            } else {
                transaction = new UaBrowseTransaction(
                    context,
                    item,
                    additionalInfo,
                    currentIndex);
                transaction.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    async browseNext(context: BrowseNextContext): Promise<Array<UaBrowseResult>> {
        const transactionManager = new UaTransactionManager<UaBrowseDescription,UaBrowseResult>();

        let currentIndex = 0;
        for (const item of context.continuationPoints) {
            const continuationPoint = UaBrowseContinuationPoint.fromByteString(item);
            const browseDescription = continuationPoint?.browseDescription ?? null;
            let transaction: UaBrowseTransaction;

            if (continuationPoint !== null && browseDescription !== null) {
                const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                    browseDescription.nodeId.nsIndex,
                );

                if (nodeManager !== null) {
                    transaction = nodeManager.getBrowseTransaction(
                        context,
                        browseDescription,
                        continuationPoint.additionalInfo,
                        currentIndex,
                    );
                } else {
                    transaction = new UaBrowseTransaction(
                        context,
                        browseDescription,
                        continuationPoint.additionalInfo,
                        currentIndex,
                    );
                    transaction.setStatusCode(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
                }
            } else {
                transaction = new UaBrowseTransaction(
                    context,
                    new UaBrowseDescription(UaNodeId.nullNodeId, BrowseDirection.Both, UaNodeId.nullNodeId, false, 0, 0),
                    new UaBrowseAdditionalInfo(0, 0, 0),
                    currentIndex);
                transaction.setStatusCode(
                    makeUaStatusCode(StatusCodes.BadContinuationPointInvalid),
                );
            }

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    async read(context: ReadContext): Promise<Array<UaDataValue>> { 
        const transactionManager = new UaTransactionManager2<UaReadValueId, UaDataValue>();

        const nodeIds : Array<UaNodeId> = [];        
        for (const item of context.nodesToRead) {
            nodeIds.push(item.nodeId);
        }

        const nsIndexes = UaTransactionManager2.getNsIndexes(nodeIds);

        for (const nsIndex of nsIndexes) {
            const handleIds = UaTransactionManager2.getHandleIds(nodeIds, nsIndex);
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nsIndex);

            if (nodeManager !== null) {
                const transactions = nodeManager.getReadTransactions(context, handleIds);
                for (const transaction of transactions) {
                    transactionManager.addTransaction(transaction);
                }
            } else {
                transactionManager.addTransaction(new UaReadTransaction(context, handleIds));
            }
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    async write(context: WriteContext): Promise<Array<UaStatusCode>> {
        const transactionManager = new UaTransactionManager2<UaWriteValue, UaStatusCode>();
        
        const nodeIds : Array<UaNodeId> = [];
        for (const item of context.nodesToWrite) {
            nodeIds.push(item.nodeId);
        }

        const nsIndexes = UaTransactionManager2.getNsIndexes(nodeIds);

        for (const nsIndex of nsIndexes) {
            const handleIds = UaTransactionManager2.getHandleIds(nodeIds, nsIndex);
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nsIndex);

            if (nodeManager !== null) {
                const transactions = nodeManager.getWriteTransactions(context, handleIds);
                for (const transaction of transactions) {
                    transactionManager.addTransaction(transaction);
                }
            } else {
                transactionManager.addTransaction(new UaWriteTransaction(context, handleIds));
            }
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    async call(context: CallContext): Promise<Array<UaCallMethodResult>> {
        const transactionManager = new UaTransactionManager<
            UaCallMethodRequest,
            UaCallMethodResult>();

        let currentIndex = 0;
        for (const item of context.methodsToCall) {
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                item.objectId.nsIndex,
            );

            const transaction = (nodeManager !== null) ?
                nodeManager.getMethodCallTransaction(context, currentIndex) : new UaMethodCallTransaction(context, currentIndex);

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    async historyRead(context: HistoryReadContext): Promise<Array<UaHistoryReadResult>> {
        const transactionManager = new UaTransactionManager<UaHistoryReadValueId, UaHistoryReadResult>();

        let currentIndex = 0;
        for (const item of context.nodesToRead) {
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                item.nodeId.nsIndex,
            );

            const transaction = (nodeManager !== null) ?
                     nodeManager.getHistoryReadTransaction(context, currentIndex)
                    : new UaHistoryReadTransaction(context, currentIndex);

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }
}

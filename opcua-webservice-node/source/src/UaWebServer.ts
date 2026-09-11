import { ApplicationDescription, BrowseDirection, StatusCodes } from "opcua-webapi";
import {
    makeUaStatusCode,
    UaAddNodesItem,
    UaAddNodesResult,
    UaAddReferencesItem,
    UaBrowseDescription,
    UaBrowseResult,
    UaCallMethodRequest,
    UaCallMethodResult,
    UaDataValue,
    UaDeleteNodesItem,
    UaDeleteReferencesItem,
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
    NodeManagerBase,
    NodeManagerList,   
    NodeManagerNs0,
    NodeManagerNs1   
} from "./server/addressspace";
import {
    UaBrowseTransaction,    
    UaReadTransaction,
    UaWriteTransaction,
    UaHistoryReadTransaction,
    UaMethodCallTransaction,
    UaAddNodeTransaction,
    UaAddReferenceTransaction,
    UaDeleteNodeTransaction,
    UaDeleteReferenceTransaction,
    UaTransactionManager,
    UaTransactionManager2,  
    ReadContext,    
    BrowseContext,
    BrowseNextContext,
    CallContext,
    FindServerContext,
    HistoryReadContext,
    WriteContext,
    AddNodesContext,
    DeleteNodesContext,
    AddReferencesContext,
    DeleteReferencesContext
} from "./server/service"

import { UaDiscoveryService } from "./UaDiscoveryService";
import { UaBrowseAdditionalInfo, UaBrowseContinuationPoint } from "./server";

export abstract class UaWebServer extends UaWebServerBase {
    private discoveryService: UaDiscoveryService | null;

    constructor(server: UaExpressServer) {
        super(server);
        this.discoveryService = null;
    }

    setDiscoveryService(discoveryService: UaDiscoveryService | null): void {
        this.discoveryService = discoveryService;
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
            throw err;
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

    override async browse(context: BrowseContext): Promise<Array<UaBrowseResult>> {
        const transactionManager = new UaTransactionManager<UaBrowseDescription,UaBrowseResult>();

        let currentIndex = 0;
        for (const item of context.nodesToBrowse) {
            const nodeManager = NodeManagerList.nodeManagerList.getNodeManager(item.nodeId.nsIndex);

            let transaction: UaBrowseTransaction;
            let additionalInfo = UaBrowseAdditionalInfo.build(item, context.requestedMaxReferencesPerNode);

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

    override async browseNext(context: BrowseNextContext): Promise<Array<UaBrowseResult>> {
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

    override async read(context: ReadContext): Promise<Array<UaDataValue>> { 
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

    override async write(context: WriteContext): Promise<Array<UaStatusCode>> {
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

    override async call(context: CallContext): Promise<Array<UaCallMethodResult>> {
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

    override async historyRead(context: HistoryReadContext): Promise<Array<UaHistoryReadResult>> {
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

    override async addNodes(context : AddNodesContext) : Promise<Array<UaAddNodesResult>>
    {
        const transactionManager = new UaTransactionManager<UaAddNodesItem, UaAddNodesResult>();
    
        let currentIndex = 0;
        for (const item of context.nodesToAdd) {
            let nodeId = item.parentNodeId.getNodeId();

            let nodeManager: NodeManagerBase | null = null;
            if (nodeId != null) {
                nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nodeId.nsIndex);
            }

            const transaction = (nodeManager !== null) ?
                     nodeManager.getAddNodeTransaction(context, currentIndex)
                    : new UaAddNodeTransaction(context, currentIndex);

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    override async deleteNodes(context : DeleteNodesContext) : Promise<Array<UaStatusCode>>
    {
        const transactionManager = new UaTransactionManager<UaDeleteNodesItem, UaStatusCode>();
    
        let currentIndex = 0;
        for (const item of context.nodesToDelete) {
            let nodeId = item.nodeId;
            let nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nodeId.nsIndex);
            
            const transaction = (nodeManager !== null) ?
                     nodeManager.getDeleteNodeTransaction(context, currentIndex)
                    : new UaDeleteNodeTransaction(context, currentIndex);

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    override async addReferences(context : AddReferencesContext) : Promise<Array<UaStatusCode>>
    {
        const transactionManager = new UaTransactionManager<UaAddReferencesItem, UaStatusCode>();
    
        let currentIndex = 0;
        for (const item of context.referencesToAdd) {
            let nodeId = item.sourceNodeId;
            let nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nodeId.nsIndex);
            
            const transaction = (nodeManager !== null) ?
                     nodeManager.getAddReferenceTransaction(context, currentIndex)
                    : new UaAddReferenceTransaction(context, currentIndex);

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    override async deleteReferences(context : DeleteReferencesContext) : Promise<Array<UaStatusCode>>
    {
        const transactionManager = new UaTransactionManager<UaDeleteReferencesItem, UaStatusCode>();
    
        let currentIndex = 0;
        for (const item of context.referencesToDelete) {
            let nodeId = item.sourceNodeId;
            let nodeManager = NodeManagerList.nodeManagerList.getNodeManager(nodeId.nsIndex);
            
            const transaction = (nodeManager !== null) ?
                     nodeManager.getDeleteReferenceTransaction(context, currentIndex)
                    : new UaDeleteReferenceTransaction(context, currentIndex);

            transactionManager.addTransaction(transaction);
            currentIndex += 1;
        }

        await transactionManager.execute();
        return transactionManager.getMergedResults();
    }

    override async findServers(context: FindServerContext): Promise<Array<ApplicationDescription>> {
        if (this.discoveryService === null) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadNotImplemented));
        }
        if (context.endpointUrl.length === 0) {
            throw new UaError(makeUaStatusCode(StatusCodes.BadInvalidArgument));
        }

        const results = await this.discoveryService.find(context.endpointUrl, context.serverUris);
        return UaDiscoveryService.findComplete(results, this.getServerConfigure());
    }
}

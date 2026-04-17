package org.opcfoundation.webserver.service;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webserver.addressspace.nodemanager.*;
import org.opcfoundation.webapi.service.types.*;
import org.opcfoundation.webserver.service.transactions.base.*;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaBrowseContinuationPoint;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public abstract class UaWebServer extends UaWebServerBase {
    @Nullable
    private UaDiscoveryService discoveryService;

    public UaWebServer()
    {
        super();
        discoveryService = null;
    }

    public void addNodeManager(NodeManager nodeManager)
    {
        NodeManagerList.nodeManagerList.addNodeManager(nodeManager);
    }

    public void setDiscoveryService(UaDiscoveryService service)
    {
        discoveryService = service;
    }

    @Override
    public void startUp() throws UaRuntimeException
    {
        // Create built in namespaces
        NodeManagerNs0 ns0 = new NodeManagerNs0();
        NodeManagerList.nodeManagerList.addNodeManager(ns0);

        NodeManagerNS1 ns1 = new NodeManagerNS1();
        NodeManagerList.nodeManagerList.addNodeManager(ns1);

        // Initialize server
        onStartUp();

        // Initialize each namespace
        Set<Integer> nsIndexes = new TreeSet<>(NodeManagerList.nodeManagerList.getNsIndexes());
        ArrayList<String> namespaceUris = new ArrayList<>();

        for (Integer item : nsIndexes)
        {
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(item);
            if (null == nodeManager) throw new UaRuntimeException(StatusCodes.Bad_UnexpectedError);

            nodeManager.onStartUp();
            namespaceUris.add(nodeManager.namespaceUri());
        }

        // Update server information
        ns0.updateNamespaceArray(namespaceUris.toArray(new String[0]));

        String[] serverArray = {getServerConfigure().getApplicationUri()};
        ns0.updateServerArray(serverArray);
    }

    @Override
    public void shutDown() throws UaRuntimeException
    {
        Set<Integer> nsIndexes = new TreeSet<>(NodeManagerList.nodeManagerList.getNsIndexes()).descendingSet();
        for (Integer item : nsIndexes)
        {
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(item);
            if (null == nodeManager) continue;
            nodeManager.onShutDown();
        }

        onShutDown();
    }

    @Override
    public CompletableFuture<List<ApplicationDescription>> findServers(FindServersContext context) throws UaRuntimeException
    {
        if (null == discoveryService || null != context.getServerUri()) throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        if (context.getEndpointUrl().isEmpty()) throw new UaRuntimeException(StatusCodes.Bad_InvalidArgument);

        return discoveryService.find(context.getEndpointUrl(), context.getServerUris()).
                thenApply(results -> UaDiscoveryService.complete(results, getServerConfigure()));
    }

    @Override
    public CompletableFuture<List<BrowseResult>> browse(BrowseContext context) throws UaRuntimeException
    {
        UaTransactionManager<BrowseDescription, BrowseResult> transactionManager = new UaTransactionManager<>();

        int currentIndex = 0;
        for (BrowseDescription item: context.getNodesToBrowse())
        {
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                    item.getNodeId().getNamespaceIndex().intValue());

            UaBrowseTransaction transaction;

            UaBrowseAdditionalInfo additionalInfo = new UaBrowseAdditionalInfo(
                    context.getRequestedMaxReferencesPerNode().intValue(),0,0);

            additionalInfo = additionalInfo.updateTasks(item);

            if (null != nodeManager)
            {
                transaction = nodeManager.getBrowseTransaction(
                        context, item, additionalInfo, currentIndex);
            } else {
                transaction = new UaBrowseTransaction(context,item,additionalInfo,currentIndex);
                transaction.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
            }

            transactionManager.addTransaction(transaction);
            currentIndex++;
        }

        return transactionManager.execute().thenApply(UaTransactionManager::getMergedResults);
    }

    @Override
    public CompletableFuture<List<BrowseResult>> browseNext(BrowseNextContext context) throws UaRuntimeException
    {
        UaTransactionManager<BrowseDescription, BrowseResult> transactionManager = new UaTransactionManager<>();

        int currentIndex = 0;
        for (ByteString item: context.getContinuationPoints())
        {
            UaBrowseContinuationPoint cp = UaBrowseContinuationPoint.fromByteString(item);
            UaBrowseTransaction transaction;

            BrowseDescription browseDescription = null;
            if (null != cp) browseDescription = cp.browseDescription();

            if (null != browseDescription)
            {
                NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                        browseDescription.getNodeId().getNamespaceIndex().intValue());

                if (null != nodeManager)
                {
                    transaction = nodeManager.getBrowseTransaction(
                            context,
                            browseDescription,
                            cp.additionalInfo(),
                            currentIndex);
                } else {
                    transaction = new UaBrowseTransaction(
                            context,
                            browseDescription,
                            cp.additionalInfo(),
                            currentIndex);

                    transaction.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
                }
            } else {
                transaction = new UaBrowseTransaction(
                        context,
                        new BrowseDescription(NodeId.NULL_VALUE, BrowseDirection.Both, NodeId.NULL_VALUE, false, UInteger.valueOf(0), UInteger.valueOf(0)),
                        new UaBrowseAdditionalInfo(0,0,0),
                        currentIndex);

                transaction.setStatusCode(StatusCode.of(StatusCodes.Bad_ContinuationPointInvalid));
            }

            transactionManager.addTransaction(transaction);
            currentIndex++;
        }

        return transactionManager.execute().thenApply(UaTransactionManager::getMergedResults);
    }

    @Override
    public CompletableFuture<List<DataValue>> read(ReadContext context) throws UaRuntimeException
    {
        UaTransactionManager2<ReadValueId, DataValue> transactionManager = new UaTransactionManager2<>();

        List<NodeId> nodeIds = new ArrayList<>();
        for (ReadValueId item: context.getNodesToRead())
        {
            nodeIds.add(item.getNodeId());
        }

        List<Integer> nsIndexes = UaTransactionManager2.getNsIndexes(nodeIds);

        for (Integer item: nsIndexes)
        {
            List<Integer> handleIds = UaTransactionManager2.getHandleIds(nodeIds, item);
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(item);

            if (null != nodeManager)
            {
                List<UaReadTransaction> transactions = nodeManager.getReadTransactions(context, handleIds);
                for (UaReadTransaction transaction: transactions)
                {
                    transactionManager.addTransaction(transaction);
                }
            } else {
                UaReadTransaction transaction = new UaReadTransaction(context,handleIds);
                transactionManager.addTransaction(transaction);
            }
        }

        return transactionManager.execute().thenApply(UaTransactionManager2::getMergedResults);
    }

    @Override
    public CompletableFuture<List<StatusCode>> write(WriteContext context) throws UaRuntimeException
    {
        UaTransactionManager2<WriteValue, StatusCode> transactionManager = new UaTransactionManager2<>();
        List<NodeId> nodeIds = new ArrayList<>();
        for (WriteValue item: context.getNodesToWrite())
        {
            nodeIds.add(item.getNodeId());
        }

        List<Integer> nsIndexes = UaTransactionManager2.getNsIndexes(nodeIds);

        for (Integer item: nsIndexes)
        {
            List<Integer> handleIds = UaTransactionManager2.getHandleIds(nodeIds, item);
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(item);

            if (null != nodeManager)
            {
                List<UaWriteTransaction> transactions = nodeManager.getWriteTransactions(context, handleIds);
                for (UaWriteTransaction transaction: transactions)
                {
                    transactionManager.addTransaction(transaction);
                }
            } else {
                UaWriteTransaction transaction = new UaWriteTransaction(context,handleIds);
                transactionManager.addTransaction(transaction);
            }
        }

        return transactionManager.execute().thenApply(UaTransactionManager2::getMergedResults);
    }

    @Override
    public CompletableFuture<List<CallMethodResult>> call(CallContext context) throws UaRuntimeException
    {
        UaTransactionManager<CallMethodRequest, CallMethodResult> transactionManager = new UaTransactionManager<>();

        int currentIndex = 0;
        for (CallMethodRequest item: context.getMethodsToCall())
        {
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                    item.getObjectId().getNamespaceIndex().intValue());

            UaMethodCallTransaction transaction;
            if (null != nodeManager)
            {
                transaction = nodeManager.getMethodCallTransaction(context, currentIndex);

            } else {
                transaction = new UaMethodCallTransaction(
                        context,
                        currentIndex);
            }

            transactionManager.addTransaction(transaction);
            currentIndex++;
        }

        return transactionManager.execute().thenApply(UaTransactionManager::getMergedResults);
    }

    @Override
    public CompletableFuture<List<HistoryReadResult>> historyRead(HistoryReadContext context) throws UaRuntimeException
    {
        UaTransactionManager<HistoryReadValueId, HistoryReadResult> transactionManager = new UaTransactionManager<>();

        int currentIndex = 0;
        for (HistoryReadValueId item: context.getNodesToRead())
        {
            NodeManagerBase nodeManager = NodeManagerList.nodeManagerList.getNodeManager(
                    item.getNodeId().getNamespaceIndex().intValue());

            UaHistoryReadTransaction transaction;
            if (null != nodeManager)
            {
                transaction = nodeManager.getHistoryReadTransaction(context, currentIndex);
            } else {
                transaction = new UaHistoryReadTransaction(
                        context,
                        currentIndex);
            }

            transactionManager.addTransaction(transaction);
            currentIndex++;
        }

        return transactionManager.execute().thenApply(UaTransactionManager::getMergedResults);
    }
}

package org.opcfoundation.webserver.addressspace.nodemanager;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.service.transactions.UaBrowseTransaction;
import org.opcfoundation.webserver.service.transactions.UaMethodCallTransaction;
import org.opcfoundation.webserver.service.transactions.UaReadTransaction;
import org.opcfoundation.webserver.service.transactions.UaWriteTransaction;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webapi.service.types.CallContext;
import org.opcfoundation.webapi.service.types.ReadContext;
import org.opcfoundation.webapi.service.types.ServiceContext;
import org.opcfoundation.webapi.service.types.WriteContext;

import java.util.ArrayList;
import java.util.List;

public interface NodeManagerBase {
    int nsIndex();

    String namespaceUri();

    @Nullable UaNode getNode(NodeId nodeId);

    default void onStartUp() throws UaRuntimeException {}
    default void onShutDown() throws UaRuntimeException {}

    default UaBrowseTransaction getBrowseTransaction(
            ServiceContext context,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId)
    {
        UaBrowseTransaction ret = new UaBrowseTransaction(
                context,
                nodeToBrowse,
                additionalInfo,
                handleId);

        ret.setStatusCode(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
        return ret;
    }

    default List<UaReadTransaction> getReadTransactions(
            ReadContext context,
            List<Integer> handleIds)
    {
        UaReadTransaction transaction = new UaReadTransaction(
                context,
                handleIds);

        ArrayList<UaReadTransaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        return transactions;
    }

    default List<UaWriteTransaction> getWriteTransactions(
            WriteContext context,
            List<Integer> handleIds)
    {
        UaWriteTransaction transaction = new UaWriteTransaction(
                context,
                handleIds);

        ArrayList<UaWriteTransaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        return transactions;
    }

    default UaMethodCallTransaction getMethodCallTransaction(
            CallContext context,
            int handleId)
    {
       return new UaMethodCallTransaction(
               context,
               handleId);
    }
}

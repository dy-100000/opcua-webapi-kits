package org.opcfoundation.webserver.service.transactions.base;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UaTransactionManager2<T_Request,T_Response> {
    private final List<UaTransaction2<T_Request, T_Response>> transactions;
    private int currentTransactionIndex;

    public UaTransactionManager2()
    {
        transactions = new ArrayList<>();
        currentTransactionIndex = 0;
    }

    public void addTransaction(UaTransaction2<T_Request, T_Response> transaction)
    {
        transactions.add(transaction);
    }

    public List<T_Response> getMergedResults() throws UaRuntimeException
    {
        Map<Integer,T_Response> results = new TreeMap<>();

        for (UaTransaction2<T_Request,T_Response> item : transactions)
        {
            if (item.getResults().size() != item.getHandleIds().size()) {
                throw new UaRuntimeException(StatusCodes.Bad_InternalError);
            }

            for (int i=0;i<item.getHandleIds().size(); ++i)
            {
                results.put(item.getHandleIds().get(i), item.getResults().get(i));
            }
        }

        return new ArrayList<>(results.values());
    }

    public CompletableFuture<UaTransactionManager2<T_Request,T_Response>> execute()
    {
        if (transactions.size() <= currentTransactionIndex)
        {
            return CompletableFuture.completedFuture(this);
        }

        return transactions.get(currentTransactionIndex).
                execute().
                thenCompose(V -> executeComplete());
    }

    public static List<Integer> getNsIndexes(List<NodeId> nodeIds)
    {
        Set<Integer> nsIndexes = new HashSet<>();
        for (NodeId item : nodeIds)
        {
            nsIndexes.add(item.getNamespaceIndex().intValue());
        }

        return new ArrayList<>(nsIndexes);
    }

    public static List<Integer> getHandleIds(List<NodeId> nodeIds, Integer nsIndex)
    {
        ArrayList<Integer> ret = new ArrayList<>();

        for (int i=0; i<nodeIds.size(); ++i)
        {
            if (nodeIds.get(i).getNamespaceIndex().intValue() == nsIndex) ret.add(i);
        }

        return ret;
    }

    private CompletableFuture<UaTransactionManager2<T_Request,T_Response>> executeComplete()
    {
        currentTransactionIndex++;
        return execute();
    }
}

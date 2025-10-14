package org.opcfoundation.webserver.service.transactions;

import org.eclipse.milo.opcua.stack.core.UaRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public class UaTransactionManager<T_Request,T_Response> {
    private final List<UaTransaction<T_Request, T_Response>> transactions;
    private int currentTransactionIndex;

    public UaTransactionManager()
    {
        transactions = new ArrayList<>();
        currentTransactionIndex = 0;
    }

    public void addTransaction(UaTransaction<T_Request, T_Response> transaction)
    {
        transactions.add(transaction);
    }

    public List<T_Response> getMergedResults() throws UaRuntimeException
    {
        Map<Integer,T_Response> results = new TreeMap<>();

        for (UaTransaction<T_Request,T_Response> item : transactions)
        {
            results.put(item.getHandleId(), item.getResult());
        }

        return new ArrayList<>(results.values());
    }

    public CompletableFuture<UaTransactionManager<T_Request,T_Response>> execute()
    {
        if (transactions.size() <= currentTransactionIndex || 0 > currentTransactionIndex)
        {
            return CompletableFuture.completedFuture(this);
        }

        return transactions.get(currentTransactionIndex).
                execute().
                thenCompose(V -> executeComplete());
    }

    private CompletableFuture<UaTransactionManager<T_Request,T_Response>> executeComplete()
    {
        currentTransactionIndex++;
        return execute();
    }
}

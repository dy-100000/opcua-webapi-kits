package org.opcfoundation.webserver.service.transactions.object;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.webserver.service.transactions.base.UaWriteTransaction;
import org.opcfoundation.webapi.service.types.WriteContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaWriteNodeTransaction extends UaWriteTransaction {
    public UaWriteNodeTransaction(
            WriteContext context,
            List<Integer> handleIds)
    {
        super(context, handleIds);
    }

    @Override
    public CompletableFuture<Void> execute()
    {
        return CompletableFuture.supplyAsync(()-> buildErrorResponse(new UaRuntimeException(StatusCodes.Bad_NotWritable)));
    }
}

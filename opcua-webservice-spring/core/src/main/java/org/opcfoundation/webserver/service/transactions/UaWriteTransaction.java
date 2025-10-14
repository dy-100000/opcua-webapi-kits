package org.opcfoundation.webserver.service.transactions;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.opcfoundation.webapi.service.types.WriteContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaWriteTransaction extends UaTransaction2<WriteValue, StatusCode> {
    protected List<StatusCode> results;

    public UaWriteTransaction(
            WriteContext writeContext,
            List<Integer> handleIds)
    {
        super(writeContext,handleIds);
        results = new ArrayList<>();
    }

    public List<WriteValue> getItems()
    {
        return ((WriteContext)serviceContext).getNodesToWrite();
    }

    public List<StatusCode> getResults()
    {
        return results;
    }

    public CompletableFuture<Void> execute()
    {
        for (Integer item: getHandleIds())
        {
            results.add(StatusCode.of(StatusCodes.Bad_NodeIdUnknown));
        }
        return CompletableFuture.completedFuture(null);
    }

    protected Void buildErrorResponse(Throwable exception)
    {
        StatusCode errorCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            errorCode = ((UaRuntimeException)exception).getStatusCode();
        }

        for (Integer item: getHandleIds())
        {
            results.add(errorCode);
        }

        return null;
    }
}

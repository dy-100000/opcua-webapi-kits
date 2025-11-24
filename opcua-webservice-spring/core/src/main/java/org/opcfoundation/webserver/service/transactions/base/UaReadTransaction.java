package org.opcfoundation.webserver.service.transactions.base;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.opcfoundation.webapi.service.types.ReadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaReadTransaction extends UaTransaction2<ReadValueId, DataValue> {
    protected final TimestampsToReturn timestampsToReturn;
    protected List<DataValue> results;

    public UaReadTransaction(
            ReadContext readContext,
            List<Integer> handleIds)
    {
        super(readContext,handleIds);
        this.timestampsToReturn = readContext.getTimestampsToReturn();
        results = new ArrayList<>();
    }

    public List<ReadValueId> getItems()
    {
        return ((ReadContext)serviceContext).getNodesToRead();
    }

    public List<DataValue> getResults()
    {
        return results;
    }

    public CompletableFuture<Void> execute()
    {
        for (Integer item: getHandleIds())
        {
            results.add(new DataValue(Variant.NULL_VALUE, StatusCode.of(StatusCodes.Bad_NodeIdUnknown), null));
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
            results.add(new DataValue(Variant.NULL_VALUE, errorCode, null));
        }

        return null;
    }
}

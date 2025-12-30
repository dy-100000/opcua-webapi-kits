package org.opcfoundation.webserver.service.transactions.base;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webapi.service.types.HistoryReadContext;
import java.util.concurrent.CompletableFuture;

public class UaHistoryReadTransaction extends UaTransaction<HistoryReadValueId, HistoryReadResult> {
    protected StatusCode statusCode;
    protected ByteString continuationPoint;
    protected ExtensionObject historyData;

    protected final UaStructuredType details;

    public UaHistoryReadTransaction(
            HistoryReadContext historyReadContext,
            int handleId)
    {
        super(historyReadContext,handleId);

        statusCode = StatusCode.GOOD;
        continuationPoint = ByteString.NULL_VALUE;
        historyData = ExtensionObject.of(ByteString.NULL_VALUE,NodeId.NULL_VALUE);

        details = historyReadContext.getHistoryReadDetails();
    }

    public HistoryReadValueId getItem()
    {
        return ((HistoryReadContext)serviceContext).getNodesToRead().get(getHandleId());
    }

    public HistoryReadResult getResult()
    {
        return new HistoryReadResult(statusCode, continuationPoint, historyData);
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public CompletableFuture<Void> execute()
    {
        statusCode = StatusCode.of(StatusCodes.Bad_NotReadable);
        return CompletableFuture.completedFuture(null);
    }

    protected Void buildErrorResponse(Throwable exception)
    {
        statusCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            statusCode = ((UaRuntimeException)exception).getStatusCode();
        }

        return null;
    }
}

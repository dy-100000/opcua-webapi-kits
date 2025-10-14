package org.opcfoundation.webserver.service.transactions;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.opcfoundation.webapi.service.types.CallContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaMethodCallTransaction extends UaTransaction<CallMethodRequest, CallMethodResult> {
    protected StatusCode statusCode;
    protected List<Variant> outputArguments;

    public UaMethodCallTransaction(
            CallContext callContext,
            int index)
    {
        super(callContext,index);
        statusCode = StatusCode.GOOD;
        outputArguments = new ArrayList<>();
    }

    public CallMethodRequest getItem()
    {
        return ((CallContext)serviceContext).getMethodsToCall().get(getHandleId());
    }

    public CallMethodResult getResult()
    {
        return new CallMethodResult(
                statusCode,
                new StatusCode[0],
                new DiagnosticInfo[0],
                outputArguments.toArray(new Variant[0]));
    }

    public CompletableFuture<Void> execute()
    {
        statusCode = StatusCode.of(StatusCodes.Bad_NodeIdUnknown);
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

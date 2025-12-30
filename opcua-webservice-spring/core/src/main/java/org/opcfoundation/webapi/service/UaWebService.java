package org.opcfoundation.webapi.service;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.opcfoundation.webapi.service.types.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UaWebService {
    UaServerConfigure getServerConfigure();

    default CompletableFuture<List<EndpointDescription>> getEndpoints(GetEndpointContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<ApplicationDescription>> findServers(FindServersContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<BrowseResult>> browse(BrowseContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<BrowseResult>> browseNext(BrowseNextContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<BrowsePathResult>> translate(TranslateContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<DataValue>> read(ReadContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<StatusCode>> write(WriteContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<CallMethodResult>> call(CallContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<List<HistoryReadResult>> historyRead(HistoryReadContext context) throws UaRuntimeException
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }
}

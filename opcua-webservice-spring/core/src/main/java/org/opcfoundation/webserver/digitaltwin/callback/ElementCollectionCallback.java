package org.opcfoundation.webserver.digitaltwin.callback;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.webserver.types.message.digitaltwin.*;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public interface ElementCollectionCallback {
    default CompletableFuture<ReadPropertyValuesResponse> onReadPropertyValues(ReadPropertyValuesRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<WritePropertyValuesResponse> onWritePropertyValues(WritePropertyValuesRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<InvokeOperationResponse> onInvokeOperation(InvokeOperationRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<GetElementsResponse> onGetElements(GetElementsRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            return new GetElementsResponse(new HashSet<>());
        });
    }

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }
}

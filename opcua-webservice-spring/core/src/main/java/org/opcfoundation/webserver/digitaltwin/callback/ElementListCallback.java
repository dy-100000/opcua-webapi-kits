package org.opcfoundation.webserver.digitaltwin.callback;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.webserver.service.message.digitaltwin.*;

import java.util.concurrent.CompletableFuture;

public interface ElementListCallback {
    default CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default boolean supportObjectElementList()
    {
        return false;
    }

    default CompletableFuture<GetPropertyElementListResponse> onGetPropertyElementList(GetPropertyElementListRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default boolean supportPropertyElementList()
    {
        return false;
    }

    default CompletableFuture<GetPropertyDescriptorResponse> onGetPropertyDescriptor(GetPropertyDescriptorRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<ReadPropertyListValueResponse> onReadPropertyValues(ReadPropertyListValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<WritePropertyListValuesResponse> onWritePropertyValues(WritePropertyListValuesRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    default CompletableFuture<GetPropertySubElementsResponse> onGetPropertySubElements(GetPropertySubElementsRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            return new GetPropertySubElementsResponse();
        });
    }

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }
}

package org.opcfoundation.webserver.digitaltwin.callback;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
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

    default CompletableFuture<ReadPropertyHistoryValuesResponse> onReadPropertyHistoryValues(ReadPropertyHistoryValuesRequest request)
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
        UaObject instance = request.getContext().getObjectId().getInstance();
        GetDescriptorResponse response;

        if (null == instance)
        {
            response = new GetDescriptorResponse(
                    new LocalizedText("NotImplemented"),
                    LocalizedText.NULL_VALUE);
        } else {
            response = new GetDescriptorResponse(
                    instance.displayName(),
                    instance.description());
        }

        return CompletableFuture.completedFuture(response);
    }
}

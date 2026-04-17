package org.opcfoundation.webserver.digitaltwin.callback;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.webserver.addressspace.nodes.UaInstanceNode;
import org.opcfoundation.webserver.service.message.digitaltwin.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubmodelCallback {
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

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }

    default CompletableFuture<GetElementsResponse> onGetElements(GetElementsRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            GetElementsResponse response = new GetElementsResponse();
            List<UaInstanceNode> elements = getElements();

            for (UaInstanceNode item: elements)
            {
                response.add(item.browseName());
            }

            return response;
        });
    }

    default CompletableFuture<ReadPropertyHistoryValuesResponse> onReadPropertyHistoryValues(ReadPropertyHistoryValuesRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            throw new UaRuntimeException(StatusCodes.Bad_NotImplemented);
        });
    }

    List<UaInstanceNode> getElements();
}

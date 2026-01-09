package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetEventResponse;

import java.util.concurrent.CompletableFuture;

public interface EventElementCallback {
    default CompletableFuture<GetEventResponse> onGetEvent(GetEventRequest request)
    {
        return CompletableFuture.completedFuture(new GetEventResponse());
    }

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }
}

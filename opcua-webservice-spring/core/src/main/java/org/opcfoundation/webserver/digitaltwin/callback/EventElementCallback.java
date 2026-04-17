package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadEventsRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.ReadEventsResponse;

import java.util.concurrent.CompletableFuture;

public interface EventElementCallback {
    default CompletableFuture<ReadEventsResponse> onReadEvents(ReadEventsRequest request)
    {
        return CompletableFuture.completedFuture(new ReadEventsResponse());
    }

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }
}

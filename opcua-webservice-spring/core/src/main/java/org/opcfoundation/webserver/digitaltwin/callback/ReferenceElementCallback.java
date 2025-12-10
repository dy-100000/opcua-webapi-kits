package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkResponse;

import java.util.concurrent.CompletableFuture;

public interface ReferenceElementCallback {
    CompletableFuture<GetLinkResponse> onGetLinks(GetLinkRequest request);

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }
}

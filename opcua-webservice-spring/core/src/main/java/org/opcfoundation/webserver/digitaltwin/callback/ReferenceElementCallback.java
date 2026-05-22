package org.opcfoundation.webserver.digitaltwin.callback;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkResponse;

import java.util.concurrent.CompletableFuture;

public interface ReferenceElementCallback {
    CompletableFuture<GetLinkResponse> onGetLinks(GetLinkRequest request);

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

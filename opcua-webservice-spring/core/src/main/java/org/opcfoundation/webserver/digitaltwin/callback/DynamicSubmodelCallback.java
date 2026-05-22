package org.opcfoundation.webserver.digitaltwin.callback;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListResponse;

import java.util.concurrent.CompletableFuture;

public interface DynamicSubmodelCallback {
    CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request);

    default CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        UaObject instance = request.getContext().getObjectId().getInstance();
        GetDescriptorResponse response;

        if (null == instance)
        {
            response = new GetDescriptorResponse(
                    new LocalizedText("GetDescriptor not implemented"),
                    LocalizedText.NULL_VALUE);
        } else {
            response = new GetDescriptorResponse(
                    instance.displayName(),
                    instance.description());
        }

        return CompletableFuture.completedFuture(response);
    }
}

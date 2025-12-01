package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.types.SubmodelDescriptor;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsResponse;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DigitalTwinCallback {
    CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request);

    default CompletableFuture<GetSubmodelsResponse> onGetSubmodels(GetSubmodelsRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            String digitalTwinId = request.getId();
            List<UaObject> submodels = getSubmodels();

            GetSubmodelsResponse response = new GetSubmodelsResponse();
            for (UaObject item: submodels)
            {
                response.add(new SubmodelDescriptor(digitalTwinId, item));
            }

            return response;
        });
    }

    List<UaObject> getSubmodels();
}

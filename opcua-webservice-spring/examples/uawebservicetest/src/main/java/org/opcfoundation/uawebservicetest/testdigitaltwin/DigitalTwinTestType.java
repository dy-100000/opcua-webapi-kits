package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinType;
import org.opcfoundation.webserver.types.SubmodelDescriptor;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DigitalTwinTestType extends DigitalTwinType {
    public DigitalTwinTestType(
            SubmodelTestType submodel,
            ElementListSubmodelTestType elementListSubmodel,
            DigitalTwinSpace space) {
        super("TestDigitalTwin", new LocalizedText("TestDigitalTwin"), space);

        addSubmodel(
                submodel,
                "Submodel",
                new LocalizedText("Submodel"),
                new LocalizedText("Test Submodel"));

        addSubmodel(
                elementListSubmodel,
                "ElementListSubmodel",
                new LocalizedText("ElementListSubmodel"),
                new LocalizedText("Test ElementListSubmodel"));
    }

    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request)
    {
        GetDescriptorResponse response = new GetDescriptorResponse(
                new LocalizedText("DT " + request.getId()),
                new LocalizedText("Test Digital Twin number " + request.getId()));

        return CompletableFuture.completedFuture(response);
    }

    public CompletableFuture<GetSubmodelsResponse> onGetSubmodels(GetSubmodelsRequest request) {
        List<UaObject> submodels = getSubmodels();
        GetSubmodelsResponse response = new GetSubmodelsResponse();

        String id = request.getId();
        if (id.equals("5"))
        {
            id = id + "-16";
        }

        for (UaObject item : submodels)
        {
            if (id.equals("4") && item.browseName().equals("ElementListSubmodel")) continue;
            response.add(new SubmodelDescriptor(id, item));
        }

        if (id.equals("3")) {
            response.add(new SubmodelDescriptor(id, new LocalizedText("Submodel-" + id), DigitalTwinSpaceTest.submodelTestType));
        }

        return CompletableFuture.completedFuture(response);
    }
}

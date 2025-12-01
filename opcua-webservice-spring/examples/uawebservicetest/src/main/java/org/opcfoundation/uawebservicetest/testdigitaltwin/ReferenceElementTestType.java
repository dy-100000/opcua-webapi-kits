package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.ReferenceElementType;
import org.opcfoundation.webserver.types.ReferenceTargetDescriptor;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorResponse;
import org.opcfoundation.webserver.types.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetLinkResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReferenceElementTestType extends ReferenceElementType {

    public ReferenceElementTestType(DigitalTwinSpace space) {
        super("ReferenceElementTestType",new LocalizedText("ReferenceElementTestType"), space);
    }

    @Override
    public CompletableFuture<GetLinkResponse> onGetLinks(GetLinkRequest request)
    {
        GetLinkResponse response = new GetLinkResponse();

        for (int i = request.getOffset(); i<request.getOffset()+5; ++i)
        {
            response.add(new ReferenceTargetDescriptor(
                    Integer.toString(i),
                    new LocalizedText("DT " + i),
                    DigitalTwinSpaceTest.digitalTwinTestType));
        }

        response.setContainsMoreData(0==request.getOffset());

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(request.getId()));
    }
}

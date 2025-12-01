package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.submodel.ElementListSubmodelType;
import org.opcfoundation.webserver.types.ObjectElementDescriptor;
import org.opcfoundation.webserver.types.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetObjectElementListResponse;

import java.util.concurrent.CompletableFuture;

public class ElementListSubmodelTestType extends ElementListSubmodelType {
    public ElementListSubmodelTestType(DigitalTwinSpace space) {
        super("ElementListSubmodelTestType", new LocalizedText("ElementListSubmodelTestType"), space);
    }

    @Override
    public CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request)
    {
        GetObjectElementListResponse response = new GetObjectElementListResponse();

        for (int i=request.getOffset();i<request.getOffset()+5; ++i)
        {
            response.add(new ObjectElementDescriptor(Integer.toString(i), new LocalizedText(Integer.toString(i)), DigitalTwinSpaceTest.elementCollectionTestAType));
        }

        response.setContainsMoreData(request.getOffset() == 0);

        return CompletableFuture.completedFuture(response);
    }
}

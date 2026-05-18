package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.submodel.DynamicSubmodelType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListResponse;
import org.opcfoundation.webserver.types.digitaltwin.ObjectElementDescriptor;
import java.util.concurrent.CompletableFuture;

public class DynamicSubmodelTestType extends DynamicSubmodelType {
    public DynamicSubmodelTestType(DigitalTwinSpace space) {
        super("DynamicSubmodelTestType", new LocalizedText("DynamicSubmodelTestType"), space);
        setDescription(new LocalizedText("DynamicSubmodelTestType"));
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

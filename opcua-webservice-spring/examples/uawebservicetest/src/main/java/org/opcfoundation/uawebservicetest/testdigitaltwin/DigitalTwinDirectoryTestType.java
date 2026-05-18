package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.digitaltwin.DigitalTwinRepositoryType;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListResponse;
import org.opcfoundation.webserver.types.digitaltwin.DigitalTwinDescriptor;

import java.util.concurrent.CompletableFuture;

public class DigitalTwinDirectoryTestType extends DigitalTwinRepositoryType {
    public DigitalTwinDirectoryTestType(DigitalTwinSpace space) {
        super("TestDigitalTwinDirectory", new LocalizedText("TestDigitalTwinDirectory"), space);
        setDescription(new LocalizedText("TestDigitalTwinDirectory"));
    }

    @Override
    public CompletableFuture<GetDigitalTwinListResponse> onGetDigitalTwinList(GetDigitalTwinListRequest request)
    {
        GetDigitalTwinListResponse response = new GetDigitalTwinListResponse();

        for (int i = request.getOffset(); i<request.getOffset()+5; ++i)
        {
            response.add(new DigitalTwinDescriptor(
                    Integer.toString(i),
                    new LocalizedText("DT " + i),
                    DigitalTwinSpaceTest.digitalTwinTestType));
        }

        response.setContainsMoreData(0 == request.getOffset());

        return CompletableFuture.completedFuture(response);
    }
}

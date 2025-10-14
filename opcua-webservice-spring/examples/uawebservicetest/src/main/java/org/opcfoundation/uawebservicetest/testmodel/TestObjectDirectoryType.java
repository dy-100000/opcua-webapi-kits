package org.opcfoundation.uawebservicetest.testmodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.models.UaObjectDirectoryType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildRequest;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestObjectDirectoryType extends UaObjectDirectoryType {
    private final TestDataObjectType testDataObjectType;

    public TestObjectDirectoryType(TestDataObjectType testDataObjectType, NodeManager nodeManager)
    {
        super("TestObjectDirectoryType", new LocalizedText("TestObjectDirectoryType"),nodeManager);
        this.testDataObjectType = testDataObjectType;
    }

    @Override
    public CompletableFuture<GetObjectDirectoryChildResponse> getChildren(GetObjectDirectoryChildRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            List<UaChildObjectDescriptor> descriptions = new ArrayList<>();

            for (int i= request.getOffset(); i< request.getOffset()+10; ++i)
            {
                String id = Integer.toString(i);
                String displayName = "DataObject-" + id;
                descriptions.add(new UaChildObjectDescriptor(id, new LocalizedText(displayName), testDataObjectType));
            }

            return new GetObjectDirectoryChildResponse(descriptions, 0 == request.getOffset());
        });
    }
}

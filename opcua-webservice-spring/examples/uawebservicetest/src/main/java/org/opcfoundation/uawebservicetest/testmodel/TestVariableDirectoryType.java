package org.opcfoundation.uawebservicetest.testmodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.opcfoundation.webserver.addressspace.models.UaVariableDirectoryType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.*;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TestVariableDirectoryType extends UaVariableDirectoryType {
    public TestVariableDirectoryType(NodeManager nodeManager)
    {
        super("TestVariableDirectoryType", new LocalizedText("TestVariableDirectoryType"),nodeManager);
    }

    @Override
    public CompletableFuture<GetVariableDirectoryChildResponse> getChildren(GetVariableDirectoryChildRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            List<UaChildVariableDescriptor> descriptions = new ArrayList<>();

            for (int i= 0; i<5; ++i)
            {
                String id = Integer.toString(i);
                String displayName = "Variable-" + id;
                descriptions.add(new UaChildVariableDescriptor(id, new LocalizedText(displayName), UaVariableTypes.BaseAnalogType));
            }

            return new GetVariableDirectoryChildResponse(descriptions, false);
        });
    }

    @Override
    public CompletableFuture<ReadVariableAttributeResponse> getChildVariableDescriptor(ReadVariableAttributeRequest request)
    {
        return CompletableFuture.supplyAsync(()-> new ReadVariableAttributeResponse(
                new LocalizedText("Variable-" + request.getVariableId()),
                new LocalizedText("Variable Number " + request.getVariableId()),
                UaDataTypes.Int32,
                true,
                false,
                null));
    }

    @Override
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            Integer value = 0;
            ReadVariableValueResponse response = new ReadVariableValueResponse();

            for (UaChildId item: request.getVariableIds())
            {
                if (null == item.getSubElementId())
                {
                    response.setValue(item, new Variant(value));
                } else if (item.getSubElementId().equals("EURange")) {
                    response.setValue(item,UaStructureUtilities.toVariant(new Range(0.0,100.0)));
                } else if (item.getSubElementId().equals("EngineeringUnits")) {
                    response.setValue(item,UaStructureUtilities.toVariant(new EUInformation(null,1,new LocalizedText("C"), new LocalizedText("Temperature"))));
                }

                value++;
            }

            return response;
        });
    }
}

package org.opcfoundation.uawebservicetest.testmodel;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.message.ReadVariableValueRequest;
import org.opcfoundation.webserver.types.message.ReadVariableValueResponse;
import org.opcfoundation.webserver.types.message.WriteVariableValueRequest;
import org.opcfoundation.webserver.types.message.WriteVariableValueResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TestDataObject2Type extends UaDataObjectType {
    TestDataObject2Type(NodeManager nodeManager)
    {
        super("TestDataObject2Type",new LocalizedText("TestDataObject2Type"), null, nodeManager);

        addVariable(
                "String",
                true,
                new LocalizedText("String"),
                new LocalizedText("String member"),
                UaDataTypes.String,true,
                true,
                null,
                null);

        addVariable(
                "Int",
                true,
                new LocalizedText("Int"),
                new LocalizedText("Int member"),
                UaDataTypes.Int32,
                false,
                true,
                null,
                null);
    }

    @Override
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            ReadVariableValueResponse response = new ReadVariableValueResponse();

            for (UaChildId item: request.getVariableIds())
            {
                if (item.getPathId().equals("String"))
                {
                    response.setValue(item, Variant.ofString("abc"));
                } else if (item.getPathId().equals("Int")) {
                    response.setValue(item, Variant.ofInt32(100));
                }
            }

            return response;
        });
    }

    @Override
    public CompletableFuture<WriteVariableValueResponse> setVariableValues(WriteVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            System.out.println("Write object, " + request.getObjectId().toString());

            WriteVariableValueResponse response = new WriteVariableValueResponse();

            for (Map.Entry<UaChildId, Variant> item: request.getVariableValues().entrySet())
            {
                System.out.println(item.toString());
                response.setOperationResult(item.getKey(), StatusCode.GOOD);
            }

            return response;
        });
    }
}

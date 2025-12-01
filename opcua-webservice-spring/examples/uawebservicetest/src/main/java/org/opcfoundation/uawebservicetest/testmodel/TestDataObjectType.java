package org.opcfoundation.uawebservicetest.testmodel;

import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.addressspace.models.UaDataObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.types.*;
import org.opcfoundation.webserver.types.message.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TestDataObjectType extends UaDataObjectType {
    TestDataObjectType(NodeManager nodeManager, TestDataObject2Type dataObject2Type)
    {
        super("TestDataObjectType",new LocalizedText("TestDataObjectType"), null, nodeManager);

        addVariable(
                "Bool",
                true,
                new LocalizedText("Bool"),
                new LocalizedText("Bool member"),
                UaDataTypes.Boolean,
                false,
                true,
                null,
                null);

        UaVariable doubleVariable = addVariable(
                "Double",
                false,
                new LocalizedText("Double"),
                new LocalizedText("Double member"),
                UaDataTypes.Double,
                true,
                false,
                null,
                UaVariableTypes.BaseAnalogType);

        UaNode range = doubleVariable.getMember("EURange");
        if (null != range && range.nodeClass() == NodeClass.Variable) ((UaVariable)range).setAccessLevel(3);

        addObject(
                "TestDataObject2",
                true,
                new LocalizedText("TestDataObject2"),
                new LocalizedText("TestDataObject2"),
                dataObject2Type);

        List<Argument> inputArguments = new ArrayList<>();
        inputArguments.add(new Argument("In1", NodeIds.Int32, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));
        inputArguments.add(new Argument("In2", NodeIds.Double, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));

        List<Argument> outArguments = new ArrayList<>();
        outArguments.add(new Argument("Out", NodeIds.Double, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));

        addMethod(
                "TestMethod",
                true,
                new LocalizedText("TestMethod"),
                new LocalizedText("Method member"),
                inputArguments,
                outArguments);
    }

    @Override
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request)
    {
        String objectId = request.getObjectId().getId();

        return CompletableFuture.supplyAsync(() -> {
            return new ReadObjectAttributeResponse(
                    objectId,
                    new LocalizedText("DataObject-" + objectId),
                    new LocalizedText("DataObject number " + objectId));
        });
    }

    @Override
    public CompletableFuture<ReadVariableValueResponse> getVariableValues(ReadVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            ReadVariableValueResponse response = new ReadVariableValueResponse();

            for (UaChildId item: request.getVariableIds())
            {
                if (item.getPathId().equals("Bool"))
                {
                    response.setValue(item, new Variant(true));
                } else if (item.getPathId().equals("Double")) {
                    if (null == item.getSubElementId())
                    {
                        response.setValue(item, Variant.ofDouble(1.5));
                    } else if (item.getSubElementId().equals("EURange")) {
                        response.setValue(item, UaStructureUtilities.toVariant(new Range(0.0,50.0)));
                    } else if (item.getSubElementId().equals("EngineeringUnits")) {
                        response.setValue(item,UaStructureUtilities.toVariant(new EUInformation(null,2,new LocalizedText("m/s"), new LocalizedText("Temperature"))));
                    }
                }
            }

            return response;
        });
    }

    @Override
    public CompletableFuture<WriteVariableValueResponse> setVariableValues(WriteVariableValueRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            System.out.println("Write object, " + request.getObjectId());

            WriteVariableValueResponse response = new WriteVariableValueResponse();

            for (Map.Entry<UaChildId, Variant> item: request.getVariableValues().entrySet())
            {
                System.out.println(item);
                response.setOperationResult(item.getKey(), StatusCode.GOOD);
            }

            return response;
        });
    }

    @Override
    public CompletableFuture<MethodCallResponse> methodCall(MethodCallRequest request)
    {
        return CompletableFuture.supplyAsync(()->{
            System.out.println("Method call: " + request.getMethodName());
            System.out.println("Input arguments: " + request.getInputArguments());

            List<Variant> outputArguments = new ArrayList<>();
            outputArguments.add(Variant.ofDouble(15.5));

            return new MethodCallResponse(outputArguments);
        });
    }
}

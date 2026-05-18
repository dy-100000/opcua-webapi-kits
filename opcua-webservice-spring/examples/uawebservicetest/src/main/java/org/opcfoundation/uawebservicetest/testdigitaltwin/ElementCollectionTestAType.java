package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.opcfoundation.webserver.addressspace.nodes.UaMethod;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.ElementCollectionType;
import org.opcfoundation.webserver.service.message.digitaltwin.*;
import org.opcfoundation.webserver.types.common.UaStructureUtilities;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ElementCollectionTestAType extends ElementCollectionType {
    private final UaVariable Bool;
    private final UaVariable Enum;
    private final UaVariable Double;
    private final UaVariable Range;
    private final UaMethod   Method;

    public ElementCollectionTestAType(
            EnumTestDataType enumTestDataType,
            DigitalTwinSpace space)
    {
        super("ElementCollectionTestAType", new LocalizedText("ElementCollectionTestAType"), space);
        setDescription(new LocalizedText("ElementCollectionTestAType"));

        Bool = addPropertyElement(
                "Bool",
                new LocalizedText("Bool"),
                new LocalizedText("Bool member"),
                UaDataTypes.Boolean,
                true);

        Enum = addPropertyElement(
                "Enum",
                new LocalizedText("Enum"),
                new LocalizedText("Enum member"),
                enumTestDataType,
                false,
                false,
                null,
                null,
                false);

        Double = addPropertyElement(
                "Double",
                new LocalizedText("Double"),
                new LocalizedText("Double member"),
                UaDataTypes.Double,
                true,
                false,
                null,
                UaVariableTypes.BaseAnalogType,
                false);

        addSubElementOfProperty(
                Double,
                "EURange",
                UaStructureUtilities.toVariant(new Range(0.0,50.0)));

        addSubElementOfProperty(
                Double,
                "EngineeringUnits",
                UaStructureUtilities.toVariant(new EUInformation(null,1, new LocalizedText("C"), new LocalizedText("Temperature C"))));

        Range = addPropertyElement(
                "Range",
                new LocalizedText("Range"),
                new LocalizedText("Range member"),
                UaDataTypes.Range,
                true);


        List<Argument> inputArguments = new ArrayList<>();
        inputArguments.add(new Argument("In1", NodeIds.Int32, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));
        inputArguments.add(new Argument("In2", NodeIds.Double, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));

        List<Argument> outArguments = new ArrayList<>();
        outArguments.add(new Argument("Out", NodeIds.Double, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));

        Method = addOperationElement(
                "Method",
                new LocalizedText("Method"),
                new LocalizedText("Test method interface"),
                inputArguments,
                outArguments,
                false);
    }

    public CompletableFuture<ReadPropertyValuesResponse> onReadPropertyValues(ReadPropertyValuesRequest request)
    {
        ReadPropertyValuesResponse response = new ReadPropertyValuesResponse();

        for (String item: request.getPropertyNames())
        {
            if (item.equals(Bool.browseName()))
            {
                response.setValue(item, new Variant(true));
            } else if (item.equals(Enum.browseName())) {
                response.setValue(item, Variant.ofInt32(2));
            } else if (item.equals(Double.browseName())) {
                response.setValue(item, Variant.ofDouble(11.5));
            } else if (item.equals(Range.browseName())) {
                response.setValue(item, UaStructureUtilities.toVariant(new Range(100.0, 150.0)));
            }
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<WritePropertyValuesResponse> onWritePropertyValues(WritePropertyValuesRequest request) {
        System.out.println("Write object, " + request.getId());

        WritePropertyValuesResponse response = new WritePropertyValuesResponse();

        for (Map.Entry<String, Variant> item: request.getPropertyNamesAndValues().entrySet())
        {
            if (item.getValue().getDataType().isEmpty()) continue;

            OpcUaDataType dataType = item.getValue().getDataType().get();
            if (!dataType.equals(OpcUaDataType.ExtensionObject))
            {
                System.out.println("Key: " + item.getKey() + " Value: " + item.getValue().getValue());
            } else {
                UaStructuredType structure = UaStructureUtilities.toStructure(item.getValue());
                System.out.println("Key: " + item.getKey() + " Value: " + structure);
            }

            response.setWriteValueResult(item.getKey(), StatusCode.GOOD);
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<InvokeOperationResponse> onInvokeOperation(InvokeOperationRequest request) {
        System.out.println("Object id: " + request.getId());
        System.out.println("Method call: " + request.getOperationName());
        System.out.println("Input arguments: " + request.getInputArguments());

        List<Variant> outputArguments = new ArrayList<>();
        outputArguments.add(Variant.ofDouble(255.5));

        InvokeOperationResponse response = new InvokeOperationResponse(outputArguments);

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<GetElementsResponse> onGetElements(GetElementsRequest request) {
        GetElementsResponse response = new GetElementsResponse();

        if (request.getId().equals("2"))
        {
            response.add(Double.browseName());
        } else if (request.getId().equals("3")) {
            response.add(Method.browseName());
        } else {
            response.add(Enum.browseName());
            response.add(Double.browseName());
            response.add(Method.browseName());
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            return new GetDescriptorResponse(
                    new LocalizedText("Element-" + request.getId()),
                    new LocalizedText("Element with id " + request.getId()));
        });
    }
}

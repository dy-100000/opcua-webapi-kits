package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.sdk.core.ValueRank;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.opcfoundation.webserver.addressspace.nodes.UaMethod;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.addressspace.nodes.UaVariable;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelType;
import org.opcfoundation.webserver.types.UaStructureUtilities;
import org.opcfoundation.webserver.types.message.digitaltwin.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SubmodelTestType extends SubmodelType {
    private final UaVariable Bool;
    private final UaVariable Double;
    private final UaMethod   Method;

    private final UaObject ElementCollectionTestA;
    private final UaObject ElementCollectionTestB;
    private final UaObject Reference;
    private final UaObject ElementList;

    public SubmodelTestType(
            ElementCollectionTestAType elementCollectionTestAType,
            ElementCollectionTestBType elementCollectionTestBType,
            ReferenceElementTestType referenceElementTestType,
            ElementListTestType elementListTestType,
            DigitalTwinSpace space)
    {
        super("SubmodelTestType", new LocalizedText("SubmodelTestType"), space);

        Bool = addPropertyElement(
                "Bool",
                new LocalizedText("Bool"),
                new LocalizedText("Bool member"),
                UaDataTypes.Boolean,
                true);

        Map<NodeId, Variant> values = new HashMap<>();
        values.put(NodeIds.BaseAnalogType_EURange, UaStructureUtilities.toVariant(new Range(0.0,50.0)));
        values.put(NodeIds.BaseAnalogType_EngineeringUnits, UaStructureUtilities.toVariant(new EUInformation(null,1, new LocalizedText("C"), new LocalizedText("Temperature C"))));

        Double = addPropertyElement(
                "Double",
                new LocalizedText("Double"),
                new LocalizedText("Double member"),
                UaDataTypes.Double,
                true,
                false,
                null,
                UaVariableTypes.BaseAnalogType,
                values,
                false);

        List<Argument> inputArguments = new ArrayList<>();
        inputArguments.add(new Argument("In", NodeIds.String, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));

        List<Argument> outArguments = new ArrayList<>();
        outArguments.add(new Argument("Out", NodeIds.String, ValueRank.Scalar.getValue(), null, LocalizedText.NULL_VALUE));

        Method = addOperationElement(
                "Method",
                new LocalizedText("Method"),
                new LocalizedText("Test method interface"),
                inputArguments,
                outArguments,
                false);

        ElementCollectionTestA = addElementCollection(
                elementCollectionTestAType,
                "CollectionA",
                new LocalizedText("CollectionA"),
                new LocalizedText("CollectionA member"),
                true);

        ElementCollectionTestB = addElementCollection(
                elementCollectionTestBType,
                "CollectionB",
                new LocalizedText("CollectionB"),
                new LocalizedText("CollectionB member"),
                true);

        Reference = addReferenceElement(
                referenceElementTestType,
                "Reference",
                new LocalizedText("Reference"),
                new LocalizedText("Reference member"),
                false);

        ElementList = addElementList(
                elementListTestType,
                "ElementList",
                new LocalizedText("ElementList"),
                new LocalizedText("Element list member"),
                false);
    }

    public CompletableFuture<ReadPropertyValuesResponse> onReadPropertyValues(ReadPropertyValuesRequest request)
    {
        return CompletableFuture.supplyAsync(()-> {
            ReadPropertyValuesResponse response = new ReadPropertyValuesResponse();

            for (String item: request.getPropertyNames())
            {
                if (item.equals(Bool.browseName()))
                {
                    response.setValue(item, new Variant(true));
                } else if (item.equals(Double.browseName())) {
                    response.setValue(item, new Variant(25.5));
                }
            }

            return response;
        });
    }

    @Override
    public CompletableFuture<WritePropertyValuesResponse> onWritePropertyValues(WritePropertyValuesRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Write object, " + request.getId());

            WritePropertyValuesResponse response = new WritePropertyValuesResponse();

            for (Map.Entry<String, Variant> item: request.getPropertyNamesAndValues().entrySet())
            {
                System.out.println(item);
                response.setWriteValueResult(item.getKey(), StatusCode.GOOD);
            }

            return response;
        });
    }

    @Override
    public CompletableFuture<InvokeOperationResponse> onInvokeOperation(InvokeOperationRequest request) {
        return CompletableFuture.supplyAsync(()->{
            List<Variant> outputArguments = new ArrayList<>();

            if (Method.browseName().equals(request.getOperationName()))
            {
                System.out.println("Object id: " + request.getId());
                System.out.println("Method call: " + request.getOperationName());

                String output = "Object " + request.getId();
                output += " receive value ";

                if (!request.getInputArguments().isEmpty())
                {
                    output += request.getInputArguments().get(0).getValue();
                }

                outputArguments.add(Variant.ofString(output));
            }

            return new InvokeOperationResponse(outputArguments);
        });
    }

    @Override
    public CompletableFuture<GetElementsResponse> onGetElements(GetElementsRequest request) {
        GetElementsResponse response = new GetElementsResponse();

        if (request.getId().equals("1")) {
            response.add(Method.browseName());
            response.add(Reference.browseName());
        } else if (request.getId().equals("2")) {
            response.add(Method.browseName());
        } else {
            response.add(Double.browseName());
            response.add(Method.browseName());
            response.add(Reference.browseName());
            response.add(ElementList.browseName());
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            return new GetDescriptorResponse(
                    new LocalizedText("Submodel-" + request.getId()),
                    new LocalizedText("Submodel with id " + request.getId()));
        });
    }
}

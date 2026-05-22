package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaDataTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaVariableTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.ElementListType;
import org.opcfoundation.webserver.service.message.digitaltwin.*;
import org.opcfoundation.webserver.types.common.UaChildId;
import org.opcfoundation.webserver.types.digitaltwin.ObjectElementDescriptor;
import org.opcfoundation.webserver.types.digitaltwin.PropertyElementDescriptor;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ElementListTestType extends ElementListType {
    public ElementListTestType(DigitalTwinSpace space) {
        super("ElementListTestType", new LocalizedText("ElementListTestType"), space);
        setDescription(new LocalizedText("ElementListTestType"));
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

    @Override
    public boolean supportObjectElementList() {
        return true;
    }

    @Override
    public CompletableFuture<GetPropertyElementListResponse> onGetPropertyElementList(GetPropertyElementListRequest request) {
        GetPropertyElementListResponse response = new GetPropertyElementListResponse();

        for (int i=request.getOffset();i<request.getOffset()+5; ++i)
        {
            response.add(new PropertyElementDescriptor(Integer.toString(i), new LocalizedText("Var-"+Integer.toString(i)), UaVariableTypes.BaseAnalogType));
        }

        response.setContainsMoreData(request.getOffset() == 0);

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public boolean supportPropertyElementList() {
        return true;
    }

    @Override
    public CompletableFuture<GetPropertyDescriptorResponse> onGetPropertyDescriptor(GetPropertyDescriptorRequest request) {
        GetPropertyDescriptorResponse response;

        if (null == request.getSubElementName())
        {
            response = new GetPropertyDescriptorResponse(
                new LocalizedText("Var-" + request.getPropertyId()),
                new LocalizedText("Variable number " + request.getPropertyId()),
                UaDataTypes.String,
                true);
        } else {
            response = new GetPropertyDescriptorResponse(
                    new LocalizedText("Element-" + request.getSubElementName()),
                    new LocalizedText("Sub-element " + request.getSubElementName()),
                    UaDataTypes.String,
                    false);
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<ReadPropertyListValueResponse> onReadPropertyValues(ReadPropertyListValueRequest request) {
        ReadPropertyListValueResponse response = new ReadPropertyListValueResponse();

        for (String item: request.getPropertyIds())
        {
            response.setValue(item, Variant.ofString(item));
        }

        for (UaChildId item: request.getSubPropertyIds())
        {
            if (null == item.getSubElementName()) continue;
            response.setValue(item, Variant.ofString(item.getSubElementName()));
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<WritePropertyListValuesResponse> onWritePropertyValues(WritePropertyListValuesRequest request) {
        System.out.println("Write object, " + request.getId());

        WritePropertyListValuesResponse response = new WritePropertyListValuesResponse();

        for (Map.Entry<String, Variant> item: request.getPropertyIdsAndValues().entrySet())
        {
            System.out.println(item);
            response.setWriteValueResult(item.getKey(), StatusCode.GOOD);
        }

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<GetPropertySubElementsResponse> onGetPropertySubElements(GetPropertySubElementsRequest request) {
        GetPropertySubElementsResponse response = new GetPropertySubElementsResponse();
        response.add("Sub-" + request.getPropertyId());
        return CompletableFuture.completedFuture(response);
    }
}

package org.opcfoundation.uawebservicetest.testdigitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.element.ElementCollectionType;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorResponse;

import java.util.concurrent.CompletableFuture;

public class ElementCollectionTestBType extends ElementCollectionType {
    private final UaObject Reference;
    private final UaObject ElementList;

    public ElementCollectionTestBType(
            ReferenceElementTestType referenceElementTestType,
            ElementListTestType elementListTestType,
            DigitalTwinSpace space)
    {
        super("ElementCollectionTestBType", new LocalizedText("ElementCollectionTestBType"), space);

        Reference = addReferenceElement(
                referenceElementTestType,
                "Reference",
                new LocalizedText("Reference"),
                new LocalizedText("Reference member"),
                true);

        ElementList = addElementList(
                elementListTestType,
                "ElementList",
                new LocalizedText("ElementList"),
                new LocalizedText("Element list member"),
                true);
    }

    @Override
    public CompletableFuture<GetDescriptorResponse> onGetDescriptor(GetDescriptorRequest request) {
        return CompletableFuture.completedFuture(new GetDescriptorResponse(
                new LocalizedText("Element-" + request.getId()),
                new LocalizedText("Element with id " + request.getId())));
    }
}

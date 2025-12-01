package org.opcfoundation.webserver.digitaltwin.digitaltwin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.DigitalTwinDirectoryCallback;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDigitalTwinListResponse;
import org.opcfoundation.webserver.types.DigitalTwinDescriptor;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.BrowseObjectRequest;
import org.opcfoundation.webserver.types.message.BrowseObjectResponse;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class DigitalTwinDirectoryType extends UaObjectType implements DigitalTwinDirectoryCallback {
    public DigitalTwinDirectoryType(String typeId,
                                    LocalizedText displayName,
                                    DigitalTwinSpace namespace)
    {
        super(typeId, displayName, UaObjectTypes.DigitalTwinDirectoryType, namespace);
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        if (request.getBrowseDescription().getReferenceTypeId().equals(NodeIds.HasComponent) ||
                !request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK))
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        ServiceContext context = new ServiceContext(request.getObjectId());
        GetDigitalTwinListRequest getElementListRequest= new GetDigitalTwinListRequest(
                context,
                request.getAdditionalInfo().getMaxReferencesPerNode(),
                request.getAdditionalInfo().getReferenceOffset());

        return onGetDigitalTwinList(getElementListRequest).
                thenApply(this::processBrowseObjectChildrenResponse);
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request) {
        return CompletableFuture.completedFuture(new ReadObjectAttributeResponse(
                request.getObjectId().getId(),
                new LocalizedText(request.getObjectId().getId()),
                LocalizedText.NULL_VALUE));
    }

    private BrowseObjectResponse processBrowseObjectChildrenResponse(GetDigitalTwinListResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (DigitalTwinDescriptor item: response.getDigitalTwins())
        {
            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.getId(),
                    NodeClass.Object,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.Organizes,
                    false);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, response.containsMoreData());
    }
}

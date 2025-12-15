package org.opcfoundation.webserver.digitaltwin.digitaltwin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.DigitalTwinDirectoryCallback;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListResponse;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectResponse;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeResponse;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaInstanceIdentifier;
import org.opcfoundation.webserver.types.common.UaObjectIdentifier;
import org.opcfoundation.webserver.types.common.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.digitaltwin.DigitalTwinDescriptor;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class DigitalTwinRepositoryType extends UaReactiveObjectType implements DigitalTwinDirectoryCallback {
    public DigitalTwinRepositoryType(String typeId,
                                     LocalizedText displayName,
                                     DigitalTwinSpace twinSpace)
    {
        super(typeId, displayName, UaObjectTypes.DigitalTwinRepositoryType, twinSpace);
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        if (request.getBrowseDescription().getReferenceTypeId().equals(NodeIds.HasComponent) ||
                request.getBrowseDescription().getReferenceTypeId().equals(NodeIds.Aggregates) ||
                !request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK))
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        GetDigitalTwinListRequest getElementListRequest= new GetDigitalTwinListRequest(
                context,
                request.getAdditionalInfo().getMaxReferencesPerNode(),
                request.getAdditionalInfo().getReferenceOffset());

        return onGetDigitalTwinList(getElementListRequest).
                thenApply(this::processBrowseObjectChildrenResponse);
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request) {
        UaInstanceIdentifier objectIdentifier = new UaInstanceIdentifier(
                new UaObjectIdentifier(nodeId().toParseableString(), request.getObjectId().getId(), null),
                null);

        NodeId directoryId = new NodeId(nodeManager.nsIndex(), objectIdentifier.toByteString());
        UaNode directoryNode = nodeManager.getNode(directoryId);

        if (null == directoryNode || NodeClass.Object != directoryNode.nodeClass()) throw new UaRuntimeException(StatusCodes.Bad_NodeIdUnknown);

        return CompletableFuture.completedFuture(new ReadObjectAttributeResponse(
                request.getObjectId().getId(),
                directoryNode.displayName(),
                directoryNode.description()));
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

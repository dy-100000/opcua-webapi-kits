package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.types.UaChildDescriptor;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class UaObjectDirectoryType extends UaSubmodelType {
    public UaObjectDirectoryType(
            String objectTypeId,
            LocalizedText displayName,
            NodeManager nodeManager)
    {
        super(objectTypeId, displayName, UaObjectTypes.FolderType, nodeManager);
    }

    // Developer need to override this method to return child object information
    public abstract CompletableFuture<GetObjectDirectoryChildResponse> getChildren(GetObjectDirectoryChildRequest request);

    @Override
    public final CompletableFuture<BrowseChildResponse> onBrowseObjectChildren(BrowseChildrenRequest request)
    {
        boolean isReferenceAllowed = false;

        UaNode referenceType = nodeManager.getNode(NodeIds.HasComponent);
        if (null != referenceType && referenceType.nodeClass() == NodeClass.ReferenceType)
        {
            isReferenceAllowed = ((UaReferenceType)referenceType).isSubtypeOf(request.getReferenceId());
        }

        if (!isReferenceAllowed ||
                (request.getNodeClassMask() & NodeClass.Object.getValue()) == 0)
            return CompletableFuture.completedFuture(new BrowseChildResponse(new ArrayList<>(), false));

        GetObjectDirectoryChildRequest getChildRequest = new GetObjectDirectoryChildRequest(
                request.getObjectId(),
                request.getLimit(),
                request.getOffset());

        return getChildren(getChildRequest).
                thenApply(this::processBrowseChildResponse);
    }

    private BrowseChildResponse processBrowseChildResponse(GetObjectDirectoryChildResponse response)
    {
        List<UaChildDescriptor> childDescriptors = new ArrayList<>();

        for (UaChildObjectDescriptor item: response.getChildren())
        {
            UaChildDescriptor descriptor = new UaChildDescriptor(
                    item.getId(),
                    NodeClass.Object,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.HasComponent);

            childDescriptors.add(descriptor);
        }

        return new BrowseChildResponse(childDescriptors, response.containsMoreData());
    }
}

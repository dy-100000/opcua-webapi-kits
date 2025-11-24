package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildRequest;
import org.opcfoundation.webserver.types.message.GetObjectDirectoryChildResponse;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.UaChildObjectDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Deprecated
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
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK))
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        GetObjectDirectoryChildRequest getChildRequest = new GetObjectDirectoryChildRequest(
                request.getObjectId(),
                request.getAdditionalInfo().getMaxReferencesPerNode(),
                request.getAdditionalInfo().getReferenceOffset());

        return getChildren(getChildRequest).
                thenApply(this::processBrowseChildResponse);
    }

    private BrowseObjectResponse processBrowseChildResponse(GetObjectDirectoryChildResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (UaChildObjectDescriptor item: response.getChildren())
        {
            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.getId(),
                    NodeClass.Object,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.Organizes,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, response.containsMoreData());
    }
}

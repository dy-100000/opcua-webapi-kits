package org.opcfoundation.webserver.digitaltwin.submodel;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.ElementListSubmodelCallback;
import org.opcfoundation.webserver.types.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetObjectElementListResponse;
import org.opcfoundation.webserver.types.ObjectElementDescriptor;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ElementListSubmodelType extends SubmodelTypeBase implements ElementListSubmodelCallback {
    public ElementListSubmodelType(String typeId,
                                   LocalizedText displayName,
                                   DigitalTwinSpace namespace)
    {
        super(typeId, displayName, namespace);
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK))
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        ServiceContext context = new ServiceContext(request.getObjectId());
        GetObjectElementListRequest getObjectElementListRequest = new GetObjectElementListRequest(
                context,
                request.getAdditionalInfo().getMaxReferencesPerNode(),
                request.getAdditionalInfo().getReferenceOffset());

        return onGetObjectElementList(getObjectElementListRequest).
                thenApply(this::processBrowseChildResponse);
    }

    private BrowseObjectResponse processBrowseChildResponse(GetObjectElementListResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (ObjectElementDescriptor item: response.getElements())
        {
            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    item.getId(),
                    NodeClass.Object,
                    item.getId(),
                    item.getDisplayName(),
                    item.getTypeId(),
                    NodeIds.HasComponent,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, response.containsMoreData());
    }
}


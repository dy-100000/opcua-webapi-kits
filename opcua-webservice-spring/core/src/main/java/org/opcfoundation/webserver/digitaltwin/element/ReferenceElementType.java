package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.ReferenceElementCallback;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.digitaltwin.ReferenceTargetDescriptor;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaReferenceDescriptor;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetLinkResponse;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ReferenceElementType extends ElementType implements ReferenceElementCallback {
    public ReferenceElementType(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace twinSpace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.ReferenceElementType,
                twinSpace);
    }

    @Override
    public final boolean isGetLinkSupported()
    {
        return true;
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

        return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
            return new ReadObjectAttributeResponse(request.getObjectId(), response.getDisplayName(), response.getDescription());
        });
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectLinks(BrowseObjectRequest request)
    {
        if (!NodeIds.References.equals(request.getBrowseDescription().getReferenceTypeId()) &&
                !NodeIds.NonHierarchicalReferences.equals(request.getBrowseDescription().getReferenceTypeId()))
        {
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));
        }

        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        GetLinkRequest getLinkRequest = new GetLinkRequest(
                context,
                request.getAdditionalInfo().getMaxReferencesPerNode(),
                request.getAdditionalInfo().getReferenceOffset());

        return onGetLinks(getLinkRequest).
                thenApply(this::processBrowseLinkResponse);
    }

    private BrowseObjectResponse processBrowseLinkResponse(GetLinkResponse response) {
        List<UaReferenceDescriptor> linkDescriptors = new ArrayList<>();

        for (ReferenceTargetDescriptor item: response.getTargets())
        {
            UaReferenceDescriptor descriptor;

            if (null == item.getInstanceDeclaration())
            {
                descriptor = new UaReferenceDescriptor(
                        item.getId(),
                        NodeClass.Object,
                        item.getId(),
                        item.getDisplayName(),
                        item.getTypeId(),
                        UaReferenceTypes.HasLink.nodeId(),
                        true);
            } else {
                descriptor = new UaReferenceDescriptor(
                        item.getId(),
                        item.getInstanceDeclaration(),
                        UaReferenceTypes.HasLink.nodeId(),
                        true);
            }

            linkDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(linkDescriptors, response.containsMoreData(), UaBrowseAdditionalInfo.GET_LINK_TASK);
    }
}

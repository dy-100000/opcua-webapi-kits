package org.opcfoundation.webserver.digitaltwin.element;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaReferenceTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.ReferenceElementCallback;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.ReferenceTargetDescriptor;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.BrowseObjectRequest;
import org.opcfoundation.webserver.types.message.BrowseObjectResponse;
import org.opcfoundation.webserver.types.message.digitaltwin.GetLinkRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetLinkResponse;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.types.message.ReadObjectAttributeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class ReferenceElementType extends ElementType implements ReferenceElementCallback {
    public ReferenceElementType(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace namespace)
    {
        super(
                typeId,
                displayName,
                UaObjectTypes.ReferenceElementType,
                namespace);
    }

    @Override
    public final boolean isGetLinkSupported()
    {
        return true;
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        UaObject instanceDeclaration = request.getObjectId().getInstance();

        if (null == instanceDeclaration)
        {
            ServiceContext context = new ServiceContext(request.getObjectId());
            GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

            return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
                return new ReadObjectAttributeResponse(response.getDisplayName(), response.getDescription());
            });
        } else {
            ReadObjectAttributeResponse response = new ReadObjectAttributeResponse(
                    instanceDeclaration.displayName(),
                    instanceDeclaration.description());

            return CompletableFuture.completedFuture(new ReadObjectAttributeResponse(response.getDisplayName(), response.getDescription()));
        }
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(),false));
    }

    @Override
    public CompletableFuture<BrowseObjectResponse> onBrowseObjectLinks(BrowseObjectRequest request)
    {
        ServiceContext context = new ServiceContext(request.getObjectId());
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

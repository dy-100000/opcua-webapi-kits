package org.opcfoundation.webserver.digitaltwin.digitaltwin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.DigitalTwinCallback;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelTypeBase;
import org.opcfoundation.webserver.types.digitaltwin.SubmodelDescriptor;
import org.opcfoundation.webserver.service.message.digitaltwin.GetSubmodelsRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetSubmodelsResponse;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.common.UaReferenceDescriptor;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectResponse;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.ReadObjectAttributeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class DigitalTwinType extends UaReactiveObjectType implements DigitalTwinCallback {
    public DigitalTwinType(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace twinSpace)
    {
        super(typeId, displayName, UaObjectTypes.DigitalTwinType, twinSpace);
    }

    public final UaObject addSubmodel(
            SubmodelTypeBase type,
            String           name,
            LocalizedText    displayName,
            LocalizedText    description)
    {
        UaObject newObject = addObjectNode(name, displayName, type);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule(UaModellingRule.Optional);
        return newObject;
    }

    public final List<UaObject> getSubmodels()
    {
        List<UaObject> submodels = new ArrayList<>();
        List<UaInstanceNode> members = getMembers();

        for (UaInstanceNode item: members)
        {
            if (NodeClass.Object == item.nodeClass())
            {
                submodels.add((UaObject)item);
            }
        }

        return submodels;
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

        return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
            return new ReadObjectAttributeResponse(request.getObjectId().getId(), response.getDisplayName(), response.getDescription());
        });
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK))
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        ObjectServiceContext context = new ObjectServiceContext(request.getObjectId());
        GetSubmodelsRequest getSubmodelsRequest = new GetSubmodelsRequest(context);

        return onGetSubmodels(getSubmodelsRequest).thenApply(this::processBrowseObjectChildrenResponse);
    }

    private BrowseObjectResponse processBrowseObjectChildrenResponse(GetSubmodelsResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();

        for (SubmodelDescriptor item: response.getSubmodels())
        {
            UaReferenceDescriptor descriptor;

            if (null == item.getInstance())
            {
                descriptor = new UaReferenceDescriptor(
                        item.getId(),
                        NodeClass.Object,
                        item.getId(),
                        item.getDisplayName(),
                        item.getTypeId(),
                        NodeIds.HasComponent,
                        true);

            } else {
                descriptor = new UaReferenceDescriptor(
                        item.getId(),
                        item.getInstance(),
                        NodeIds.HasComponent,
                        true);
            }

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, false);
    }
}

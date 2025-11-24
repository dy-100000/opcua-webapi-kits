package org.opcfoundation.webserver.digitaltwin.digitaltwin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.digitaltwin.DigitalTwinSpace;
import org.opcfoundation.webserver.digitaltwin.callback.DigitalTwinCallback;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsResponse;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDescriptorRequest;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.digitaltwin.submodel.SubmodelType;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class DigitalTwinType extends UaObjectType implements DigitalTwinCallback {
    public DigitalTwinType(
            String typeId,
            LocalizedText displayName,
            DigitalTwinSpace namespace)
    {
        super(typeId, displayName, UaObjectTypes.DigitalTwinType, namespace);
    }

    public final UaObject addSubmodel(
            SubmodelType  type,
            String        name,
            LocalizedText displayName,
            LocalizedText description,
            boolean       mandatory)
    {
        UaObject newObject = addObjectNode(name, displayName, type);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
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
        ServiceContext context = new ServiceContext(request.getObjectId());
        GetDescriptorRequest getDescriptorRequest = new GetDescriptorRequest(context);

        return onGetDescriptor(getDescriptorRequest).thenApply(response -> {
            return new ReadObjectAttributeResponse(response.getDisplayName(), response.getDescription());
        });
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectChildren(BrowseObjectRequest request)
    {
        List<UaInstanceNode> members = getMembers();
        final List<UaInstanceNode> membersToReturn = new ArrayList<>();

        for (UaInstanceNode item: members)
        {
            if (item.nodeClass() == NodeClass.Object)
            {
                if (!request.getAdditionalInfo().isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK)) continue;
            }

            membersToReturn.add(item);
        }

        if (membersToReturn.isEmpty()) return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));

        ServiceContext context = new ServiceContext(request.getObjectId());
        GetSubmodelsRequest getSubmodelsRequest = new GetSubmodelsRequest(context);

        return onGetSubmodels(getSubmodelsRequest).thenApply(response -> {
            return processBrowseObjectChildrenResponse(request.getObjectId().getId(), membersToReturn,response);
        });
    }

    private BrowseObjectResponse processBrowseObjectChildrenResponse(
            String parentId,
            List<UaInstanceNode> members,
            GetSubmodelsResponse response)
    {
        List<UaReferenceDescriptor> childDescriptors = new ArrayList<>();
        Map<String,String> submodelIdsByName = response.getSubmodelIdsByName();

        for (UaInstanceNode item: members)
        {
            String childId = submodelIdsByName.get(item.browseName());

            if (item.modellingRule() == UaModellingRule.Optional &&
                    null == childId) continue;

            if (null == childId) childId = parentId;

            NodeId referenceType = (item.nodeClass() == NodeClass.Variable) ? NodeIds.HasProperty : NodeIds.Organizes;

            UaReferenceDescriptor descriptor = new UaReferenceDescriptor(
                    childId,
                    item,
                    referenceType,
                    true);

            childDescriptors.add(descriptor);
        }

        return new BrowseObjectResponse(childDescriptors, false);
    }
}

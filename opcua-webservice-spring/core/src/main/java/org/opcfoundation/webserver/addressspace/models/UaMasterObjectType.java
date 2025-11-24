package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaBrowseAdditionalInfo;
import org.opcfoundation.webserver.types.message.GetChildObjectIdRequest;
import org.opcfoundation.webserver.types.message.GetChildObjectIdResponse;
import org.opcfoundation.webserver.types.message.GetParentObjectRequest;
import org.opcfoundation.webserver.types.message.GetParentObjectResponse;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.*;
import org.opcfoundation.webserver.types.message.digitaltwin.GetSubmodelsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Deprecated
public abstract class UaMasterObjectType extends UaObjectType {
    public UaMasterObjectType(
            String objectTypeId,
            LocalizedText displayName,
            @Nullable UaMasterObjectType parentType,
            NodeManager nodeManager)
    {
        super(
            objectTypeId,
            displayName,
            (null == parentType) ? UaObjectTypes.BaseObjectType : parentType,
            nodeManager);
    }

    public final UaObject addSubmodel(
            String childId,
            boolean mandatory,
            LocalizedText displayName,
            LocalizedText description,
            UaSubmodelType submodel)
    {
        UaObject newObject = addObjectNode(childId, displayName, submodel);
        if (description.isNotNull()) newObject.setDescription(description);
        newObject.setModellingRule((mandatory) ? UaModellingRule.Mandatory : UaModellingRule.Optional);
        return newObject;
    }

    // If developer need to return human readable name and description, this method can be overridden
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request)
    {
        return CompletableFuture.supplyAsync(() -> {
            return new ReadObjectAttributeResponse(
                    new LocalizedText(request.getObjectId().getId()),
                    LocalizedText.NULL_VALUE);
        });
    }

    public CompletableFuture<GetParentObjectResponse> getParent(GetParentObjectRequest request)
    {
        return CompletableFuture.supplyAsync(() -> {
            return new GetParentObjectResponse();
        });
    }

    // If developer need to return member object id different with current object, this method can be overridden
    public CompletableFuture<GetChildObjectIdResponse> getChildId(GetChildObjectIdRequest request)
    {
        return CompletableFuture.supplyAsync(() -> {
            GetChildObjectIdResponse response = new GetChildObjectIdResponse();

            List<UaInstanceNode> members = getMembers();

            for (UaInstanceNode item : members)
            {
                if (NodeClass.Object != item.nodeClass()) continue;
                response.addChildId(request.getObjectId().getId(),item.browseName());
            }

            return response;
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

        GetChildObjectIdRequest getChildIdRequest = new GetChildObjectIdRequest(request.getObjectId());
        return getChildId(getChildIdRequest).thenApply(response -> processBrowseChildResponse(request.getObjectId().getId(), membersToReturn, response));
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectParent(BrowseObjectRequest request)
    {
        NodeId referenceTypeId = request.getBrowseDescription().getReferenceTypeId();

        if (!referenceTypeId.equals(NodeIds.HierarchicalReferences) &&
                !referenceTypeId.equals(NodeIds.References) &&
                !referenceTypeId.equals(NodeIds.Organizes))
        {
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));
        }

        GetParentObjectRequest getParentObjectRequest = new GetParentObjectRequest(request.getObjectId());
        return getParent(getParentObjectRequest).thenApply(this::processBrowseParentResponse);
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        return getObjectAttribute(request);
    }

    private BrowseObjectResponse processBrowseChildResponse(
            String masterObjectId,
            List<UaInstanceNode> members,
            GetChildObjectIdResponse response)
    {
        List<UaReferenceDescriptor> childrenToReturn = new ArrayList<>();
        Map<String, String> children = response.getChildrenIdsByName();

        for (UaInstanceNode item : members)
        {
            if (NodeClass.Object == item.nodeClass())
            {
                String childId = children.get(item.browseName());

                if (null == childId)
                {
                    if (UaModellingRule.Optional == item.modellingRule()) continue;
                    childId = masterObjectId;
                }

                childrenToReturn.add(new UaReferenceDescriptor(childId, item, NodeIds.HasComponent, true));
            } else {
                childrenToReturn.add(new UaReferenceDescriptor(
                        masterObjectId,
                        item,
                        (item.nodeClass() == NodeClass.Variable) ? NodeIds.HasProperty : NodeIds.HasComponent,
                        true));
            }
        }

        return new BrowseObjectResponse(childrenToReturn, false);
    }

    private BrowseObjectResponse processBrowseParentResponse(GetParentObjectResponse response)
    {
        List<UaReferenceDescriptor> childToReturn = new ArrayList<>();

        if (response.isEmpty()) return new BrowseObjectResponse(childToReturn, false);

        childToReturn.add(new UaReferenceDescriptor(
                response.getId(),
                NodeClass.Object,
                response.getId(),
                response.getDisplayName(),
                response.getTypeId(),
                NodeIds.Organizes,
                false));

        return new BrowseObjectResponse(childToReturn, false);
    }
}

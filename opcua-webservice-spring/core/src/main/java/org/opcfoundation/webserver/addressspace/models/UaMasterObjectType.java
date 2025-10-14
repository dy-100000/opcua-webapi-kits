package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.*;
import org.opcfoundation.webserver.addressspace.nodes.builtin.UaObjectTypes;
import org.opcfoundation.webserver.types.UaChildDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
    public final CompletableFuture<BrowseChildResponse> onBrowseObjectChildren(BrowseChildrenRequest request)
    {
        UaNode hasComponentType = nodeManager.getNode(NodeIds.HasComponent);
        UaNode hasPropertyType = nodeManager.getNode(NodeIds.HasProperty);

        if (null == hasComponentType || hasComponentType.nodeClass() != NodeClass.ReferenceType ||
                null == hasPropertyType || hasPropertyType.nodeClass() != NodeClass.ReferenceType)
        {
            throw new UaRuntimeException(StatusCodes.Bad_InternalError);
        }

        List<UaInstanceNode> members = getMembers();
        final List<UaInstanceNode> membersToReturn = new ArrayList<>();

        for (UaInstanceNode item: members)
        {
            if ((item.nodeClass().getValue() & request.getNodeClassMask()) == 0) continue;

            if (item.nodeClass() == NodeClass.Object || item.nodeClass() == NodeClass.Method)
            {
                if (!((UaReferenceType)hasComponentType).isSubtypeOf(request.getReferenceId())) continue;
            } else if (item.nodeClass() == NodeClass.Variable) {
                if (!((UaReferenceType)hasPropertyType).isSubtypeOf(request.getReferenceId())) continue;
            }

            membersToReturn.add(item);
        }

        GetChildObjectIdRequest getChildIdRequest = new GetChildObjectIdRequest(request.getObjectId());
        return getChildId(getChildIdRequest).thenApply(response -> processBrowseChildResponse(request.getObjectId().getId(), membersToReturn, response));
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request)
    {
        return getObjectAttribute(request);
    }

    private BrowseChildResponse processBrowseChildResponse(
            String masterObjectId,
            List<UaInstanceNode> members,
            GetChildObjectIdResponse response)
    {
        List<UaChildDescriptor> childrenToReturn = new ArrayList<>();
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

                childrenToReturn.add(new UaChildDescriptor(childId, item, NodeIds.HasComponent));
            } else {
                childrenToReturn.add(new UaChildDescriptor(
                        masterObjectId,
                        item,
                        (item.nodeClass() == NodeClass.Variable) ? NodeIds.HasProperty : NodeIds.HasComponent));
            }
        }

        return new BrowseChildResponse(childrenToReturn, false);
    }
}

package org.opcfoundation.webserver.addressspace.models;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.addressspace.nodes.BaseUaObjectType;
import org.opcfoundation.webserver.addressspace.nodes.UaNode;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;
import org.opcfoundation.webserver.types.message.GetParentObjectRequest;
import org.opcfoundation.webserver.types.message.GetParentObjectResponse;
import org.opcfoundation.webserver.types.UaReferenceDescriptor;
import org.opcfoundation.webserver.types.message.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Deprecated
public class UaSubmodelType extends UaObjectType {
    public UaSubmodelType(
            String objectTypeId,
            LocalizedText displayName,
            BaseUaObjectType parentType,
            NodeManager nodeManager) {
        super(
                objectTypeId,
                displayName,
                parentType,
                nodeManager);
    }

    // Can be overridden
    public CompletableFuture<ReadObjectAttributeResponse> getObjectAttribute(ReadObjectAttributeRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            return new ReadObjectAttributeResponse(
                    new LocalizedText(request.getObjectId().getId()),
                    LocalizedText.NULL_VALUE);
        });
    }

    // Can be overridden
    public CompletableFuture<GetParentObjectResponse> getParent(GetParentObjectRequest request)
    {
        return CompletableFuture.supplyAsync(() -> {
            UaObject instanceDeclaration = request.getObjectId().getInstance();

            if (null != instanceDeclaration)
            {
                UaNode parentNode = instanceDeclaration.parentNode();
                if (null == parentNode ||
                        NodeClass.ObjectType != parentNode.nodeClass()) return new GetParentObjectResponse();

                return new GetParentObjectResponse(
                        request.getObjectId().getId(),
                        new LocalizedText("Parentof " + instanceDeclaration.displayName().getText()),
                        ((BaseUaObjectType)parentNode).nodeId());
            } else {
                return new GetParentObjectResponse();
            }
        });
    }

    @Override
    public final CompletableFuture<ReadObjectAttributeResponse> onReadObjectAttributes(ReadObjectAttributeRequest request) {
        UaObject instanceDeclaration = request.getObjectId().getInstance();

        if (null == instanceDeclaration) {
            return getObjectAttribute(request);
        } else {
            return CompletableFuture.supplyAsync(() -> {
                return new ReadObjectAttributeResponse(
                        instanceDeclaration.displayName(),
                        instanceDeclaration.description());
            });
        }
    }

    @Override
    public final CompletableFuture<BrowseObjectResponse> onBrowseObjectParent(BrowseObjectRequest request)
    {
        NodeId referenceTypeId = request.getBrowseDescription().getReferenceTypeId();

        if (!referenceTypeId.equals(NodeIds.HierarchicalReferences) &&
                !referenceTypeId.equals(NodeIds.References) &&
                !referenceTypeId.equals(NodeIds.HasComponent))
        {
            return CompletableFuture.completedFuture(new BrowseObjectResponse(new ArrayList<>(), false));
        }

        GetParentObjectRequest getParentObjectRequest = new GetParentObjectRequest(request.getObjectId());
        return getParent(getParentObjectRequest).thenApply(this::processBrowseParentResponse);
    }

    @Override
    public boolean isGetParentSupported() {
        return true;
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



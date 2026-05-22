package org.opcfoundation.webserver.service.transactions.reactiveobject;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.opcfoundation.webserver.addressspace.reactiveobject.UaReactiveObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.service.transactions.base.UaBrowseTransaction;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectRequest;
import org.opcfoundation.webserver.service.message.reactiveobject.BrowseObjectResponse;
import org.opcfoundation.webapi.service.types.ServiceContext;
import org.opcfoundation.webserver.types.common.*;

import java.util.concurrent.CompletableFuture;

public class UaBrowseObjectTransaction extends UaBrowseTransaction {
    private final UaReactiveObjectType objectType;
    private final UaObjectId objectId;
    private final NodeManager nodeManager;

    public UaBrowseObjectTransaction(
            ServiceContext serviceContext,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId,
            UaReactiveObjectType objectType,
            UaObjectId objectId,
            NodeManager nodeManager)
    {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this.objectType = objectType;
        this.objectId = objectId;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute() {
        try {
            // Add type definition
            if (additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_DEFINITION_TASK))
            {
                addTypeDefinitionReference(
                        objectType.nodeId(),
                        objectType.browseName(),
                        objectType.displayName());

                additionalInfo = additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_DEFINITION_TASK);
            }

//            if (!objectType.isGetParentSupported())
//            {
            additionalInfo = additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_PARENT_TASK);
//            }

            if (!objectType.isGetLinkSupported())
            {
                additionalInfo = additionalInfo.taskComplete(UaBrowseAdditionalInfo.GET_LINK_TASK);
            }

            if (additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_LINK_TASK)) {
                BrowseObjectRequest request = new BrowseObjectRequest(
                        objectId,
                        additionalInfo,
                        getItem());

                return objectType.onBrowseObjectLinks(request).
                        thenApply(this::browseObjectResult).
                        exceptionally(ex -> buildErrorResponse(ex.getCause()));
            }

            if (additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_OBJECT_TASK) ||
                    additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) ||
                    additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_METHOD_TASK)) {
                BrowseObjectRequest request = new BrowseObjectRequest(
                        objectId,
                        additionalInfo,
                        getItem());

                return objectType.onBrowseObjectChildren(request).
                        thenApply(this::browseObjectResult).
                        exceptionally(ex -> buildErrorResponse(ex.getCause()));
            }
        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private Void browseObjectResult(BrowseObjectResponse response)
    {
        for (UaReferenceDescriptor item: response.getChildren())
        {
            if (item.getNodeClass() == NodeClass.ObjectType)
            {
                references.add(new ReferenceDescription(
                        item.getReferenceTypeId(),
                        item.isForward(),
                        item.getTypeDefinitionId().expanded(),
                        new QualifiedName(0,item.getBrowseName()),
                        item.getDisplayName(),
                        item.getNodeClass(),
                        NodeId.NULL_VALUE.expanded()));

                continue;
            }

            if (item.getId().isEmpty()) continue;

            if (item.getNodeClass() != NodeClass.Object &&
                    item.getNodeClass() != NodeClass.Variable &&
                    item.getNodeClass() != NodeClass.Method) continue;

            UaInstanceIdentifier newIdentifier;

            if (item.getNodeClass() == NodeClass.Object)
            {
                UaObjectIdentifier objectIdentifier = new UaObjectIdentifier(
                        item.getTypeDefinitionId().toParseableString(),
                        item.getId(),
                        (item.getInstanceDeclarationId().isNull()) ? null : item.getInstanceDeclarationId().toParseableString());

                newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        null);
            } else {
                UaObjectIdentifier objectIdentifier = new UaObjectIdentifier(
                        objectType.nodeId().toParseableString(),
                        objectId.getId(),
                        (null == objectId.getInstance()) ? null : objectId.getInstance().nodeId().toParseableString());

                UaChildIdentifier memberIdentifier = new UaChildIdentifier(
                        item.getId(),
                        null,
                        item.getNodeClass() == NodeClass.Method);

                newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        memberIdentifier);
            }

            references.add(new ReferenceDescription(
                    item.getReferenceTypeId(),
                    item.isForward(),
                    new NodeId(nodeManager.nsIndex(), newIdentifier.toByteString()).expanded(),
                    new QualifiedName(0,item.getBrowseName()),
                    item.getDisplayName(),
                    item.getNodeClass(),
                    item.getTypeDefinitionId().expanded()));
        }

        if (response.containsMoreData() && !references.isEmpty())
        {
            additionalInfo = additionalInfo.updateOffset(references.size());
        } else {
            additionalInfo = additionalInfo.taskComplete(response.getTaskMask());
        }

        if (!additionalInfo.isAllTaskComplete())
        {
            continuationPoint = new UaBrowseContinuationPoint(getItem(), additionalInfo).toByteString();
        }

        return null;
    }
}

package org.opcfoundation.webserver.addressspace.transactions;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.service.transactions.UaBrowseTransaction;
import org.opcfoundation.webserver.types.*;
import org.opcfoundation.webserver.types.message.BrowseChildrenRequest;
import org.opcfoundation.webserver.types.message.BrowseChildResponse;
import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.concurrent.CompletableFuture;

public class UaBrowseChildTransaction extends UaBrowseTransaction {
    private final UaObjectType objectType;
    private final UaObjectId objectId;
    private final NodeManager nodeManager;

    public UaBrowseChildTransaction(
            ServiceContext serviceContext,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId,
            UaObjectType objectType,
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
            BrowseChildrenRequest request = new BrowseChildrenRequest(
                    objectId,
                    getItem().getReferenceTypeId(),
                    getItem().getNodeClassMask().intValue(),
                    additionalInfo.getMaxReferencesPerNode(),
                    additionalInfo.getReferenceOffset());

            return objectType.onBrowseObjectChildren(request).
                    thenApply(this::browseObjectChildResult).
                    exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            buildErrorResponse(e);
            return CompletableFuture.completedFuture(null);
        }
    }

    private Void browseObjectChildResult(BrowseChildResponse response)
    {
        for (UaChildDescriptor item: response.getChildren())
        {
            if (item.getChildId().isEmpty()) continue;

            if (item.getNodeClass() != NodeClass.Object &&
                    item.getNodeClass() != NodeClass.Variable &&
                    item.getNodeClass() != NodeClass.Method) continue;

            UaInstanceIdentifier newIdentifier;

            if (item.getNodeClass() == NodeClass.Object)
            {
                UaObjectIdentifier objectIdentifier = new UaObjectIdentifier(
                        item.getTypeDefinitionId().toParseableString(),
                        item.getChildId(),
                        (item.getInstanceDeclarationId().isNull()) ? null : item.getInstanceDeclarationId().toParseableString());

                newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        null);
            } else {
                UaObjectIdentifier objectIdentifier = new UaObjectIdentifier(
                        objectType.nodeId().toParseableString(),
                        objectId.getId(),
                        (null == objectId.getInstanceDeclaration()) ? null : objectId.getInstanceDeclaration().nodeId().toParseableString());

                String variableTypeId = null;
                if (item.getNodeClass() == NodeClass.Variable)
                {
                    if (item.getTypeDefinitionId().isNull() ||
                            item.getTypeDefinitionId().equals(NodeIds.BaseDataVariableType) ||
                            item.getTypeDefinitionId().equals(NodeIds.PropertyType))
                    {
                        variableTypeId = "";
                    } else {
                        variableTypeId = item.getTypeDefinitionId().toParseableString();
                    }
                }

                UaMemberIdentifier memberIdentifier = new UaMemberIdentifier(
                        item.getChildId(),
                        null,
                        variableTypeId);

                newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        memberIdentifier);
            }

            //System.out.println("Result: " + newIdentifier);

            references.add(new ReferenceDescription(
                    item.getReferenceTypeId(),
                    true,
                    new NodeId(nodeManager.nsIndex(), newIdentifier.toByteString()).expanded(),
                    new QualifiedName(0,item.getBrowseName()),
                    item.getDisplayName(),
                    item.getNodeClass(),
                    item.getTypeDefinitionId().expanded()));
        }

        UaBrowseAdditionalInfo newAdditionalInfo;

        if (response.containsMoreData() && !references.isEmpty())
        {
            newAdditionalInfo = additionalInfo.updateOffset(references.size());
        } else {
            newAdditionalInfo = additionalInfo.browseChildComplete();
        }

        if (newAdditionalInfo.isBrowseTypeDefinitionRequired())
        {
            addTypeDefinitionReference(
                    objectType.nodeId(),
                    objectType.browseName(),
                    objectType.displayName());

            newAdditionalInfo = newAdditionalInfo.browseTypeDefinitionComplete();
        }

        if (!newAdditionalInfo.isAllTaskComplete())
        {
            continuationPoint = new UaBrowseContinuationPoint(getItem(), newAdditionalInfo).toByteString();
        }

        return null;
    }
}

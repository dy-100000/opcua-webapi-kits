package org.opcfoundation.webserver.service.transactions.object;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.opcfoundation.webserver.addressspace.models.UaObjectType;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.service.transactions.base.UaBrowseTransaction;
import org.opcfoundation.webserver.types.*;
import org.opcfoundation.webapi.service.types.ServiceContext;
import org.opcfoundation.webserver.types.message.BrowseMemberRequest;
import org.opcfoundation.webserver.types.message.BrowseMemberResponse;

import java.util.concurrent.CompletableFuture;

public class UaBrowseMemberTransaction extends UaBrowseTransaction {
    private final UaObjectType objectType;
    private final UaObjectId objectId;
    private final UaChildIdentifier memberId;
    private final NodeManagerWebService nodeManager;

    public UaBrowseMemberTransaction(
            ServiceContext serviceContext,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId,
            UaObjectType objectType,
            UaObjectId objectId,
            UaChildIdentifier memberId,
            NodeManagerWebService nodeManager)
    {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this.objectType = objectType;
        this.objectId = objectId;
        this.memberId = memberId;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        try {
            if (!additionalInfo.isTaskRequired(UaBrowseAdditionalInfo.GET_CHILD_VARIABLE_TASK) ||
                    null != memberId.getPathL2()) return null;

            BrowseMemberRequest request = new BrowseMemberRequest(
                    objectId,
                    memberId.getPath(),
                    null != memberId.getMethodNode() && memberId.getMethodNode(),
                    getItem());

            return objectType.onBrowseMemberChildren(request).
                    thenApply(this::browseMemberChildResult).
                    exceptionally(ex -> buildErrorResponse(ex.getCause()));
        } catch (Exception e) {
            buildErrorResponse(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    private Void browseMemberChildResult(BrowseMemberResponse response)
    {
        for (UaReferenceDescriptor item: response.getChildren())
        {
            if (item.getId().isEmpty() || !item.isForward() || item.getNodeClass() != NodeClass.Variable) continue;

            UaObjectIdentifier objectIdentifier = new UaObjectIdentifier(
                        objectType.nodeId().toParseableString(),
                        objectId.getId(),
                        (null == objectId.getInstance()) ? null : objectId.getInstance().nodeId().toParseableString());

            UaChildIdentifier memberIdentifier = new UaChildIdentifier(
                        memberId.getPath(),
                        item.getId(),
                        item.getNodeClass() == NodeClass.Method);

            UaInstanceIdentifier newIdentifier = new UaInstanceIdentifier(
                        objectIdentifier,
                        memberIdentifier);

            // System.out.println("Result: " + newIdentifier);

            references.add(new ReferenceDescription(
                    item.getReferenceTypeId(),
                    item.isForward(),
                    new NodeId(nodeManager.nsIndex(), newIdentifier.toByteString()).expanded(),
                    new QualifiedName(0,item.getBrowseName()),
                    item.getDisplayName(),
                    NodeClass.Variable,
                    item.getTypeDefinitionId().expanded()));
        }

        return null;
    }
}

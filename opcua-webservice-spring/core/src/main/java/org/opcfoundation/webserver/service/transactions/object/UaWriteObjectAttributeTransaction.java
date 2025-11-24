package org.opcfoundation.webserver.service.transactions.object;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.opcfoundation.webapi.service.types.WriteContext;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManagerWebService;
import org.opcfoundation.webserver.service.transactions.base.UaWriteTransaction;
import org.opcfoundation.webserver.types.UaObjectIdentifier;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaWriteObjectAttributeTransaction extends UaWriteTransaction {
    private final UaObjectIdentifier objectId;
    private final NodeManagerWebService nodeManager;

    public UaWriteObjectAttributeTransaction(
            WriteContext context,
            List<Integer> handleIds,
            UaObjectIdentifier objectId,
            NodeManagerWebService nodeManager)
    {
        super(context,handleIds);
        this.objectId = objectId;
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        try {
            throw new UaRuntimeException(StatusCodes.Bad_NotWritable);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(buildErrorResponse(e));
        }
    }
}

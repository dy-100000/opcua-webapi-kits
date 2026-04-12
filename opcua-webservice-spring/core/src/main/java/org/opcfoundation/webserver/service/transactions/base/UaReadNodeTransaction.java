package org.opcfoundation.webserver.service.transactions.base;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webapi.service.types.ReadContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaReadNodeTransaction extends UaReadTransaction {
    private final NodeManager nodeManager;

    public UaReadNodeTransaction(
            ReadContext context,
            List<Integer> handleIds,
            NodeManager nodeManager)
    {
        super(context, handleIds);
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        return CompletableFuture.supplyAsync(()-> {
            try
            {
                DateTime now = DateTime.now();
                List<ReadValueId> nodesToRead = getRequestedItems();

                for (ReadValueId item: nodesToRead)
                {
                    DataValue result = nodeManager.read(item.getNodeId(), item.getAttributeId().intValue(), now, timestampsToReturn);
                    results.add(result);
                }

                return null;
            } catch (Exception e) {
                buildErrorResponse(e);
            }

            return null;
        });
    }
}

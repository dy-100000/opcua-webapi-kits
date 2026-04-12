package org.opcfoundation.webserver.service.transactions.base;

import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.opcfoundation.webserver.addressspace.nodemanager.NodeManager;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class UaBrowseNodeTransaction extends UaBrowseTransaction {
    private final NodeManager nodeManager;

    public UaBrowseNodeTransaction(
            ServiceContext serviceContext,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId,
            NodeManager nodeManager)
    {
        super(serviceContext, nodeToBrowse, additionalInfo, handleId);
        this.nodeManager = nodeManager;
    }

    public CompletableFuture<Void> execute()
    {
        return CompletableFuture.supplyAsync(()-> {
            try
            {
                BrowseDescription nodeToBrowse = getItem();
                BrowseResult result = nodeManager.browse(nodeToBrowse, additionalInfo);
                statusCode = result.getStatusCode();
                continuationPoint = result.getContinuationPoint();
                if (null != result.getReferences()) references = Arrays.asList(result.getReferences());
            } catch (Exception e) {
                buildErrorResponse(e);
            }

            return null;
        });
    }
}

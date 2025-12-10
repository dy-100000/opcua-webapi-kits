package org.opcfoundation.webserver.service.transactions.base;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.opcfoundation.webserver.types.common.UaBrowseAdditionalInfo;
import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UaBrowseTransaction extends UaTransaction<BrowseDescription, BrowseResult> {
    protected final BrowseDescription nodeToBrowse;
    protected UaBrowseAdditionalInfo additionalInfo;

    protected StatusCode statusCode;
    protected ByteString continuationPoint;
    protected List<ReferenceDescription> references;

    public UaBrowseTransaction(
            ServiceContext serviceContext,
            BrowseDescription nodeToBrowse,
            UaBrowseAdditionalInfo additionalInfo,
            int handleId)
    {
        super(serviceContext,handleId);
        this.nodeToBrowse = nodeToBrowse;
        this.additionalInfo = additionalInfo;

        this.statusCode = StatusCode.GOOD;
        continuationPoint = ByteString.NULL_VALUE;
        references = new ArrayList<>();
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void addTypeDefinitionReference(
            NodeId typeId,
            String browseName,
            LocalizedText displayName)
    {
        references.add(
                0,
                new ReferenceDescription(
                        NodeIds.HasTypeDefinition,
                        true,
                        typeId.expanded(),
                        new QualifiedName(0, browseName),
                        displayName,
                        NodeClass.ObjectType,
                        NodeId.NULL_VALUE.expanded()));
    }

    public BrowseDescription getItem()
    {
        return nodeToBrowse;
    }

    public BrowseResult getResult()
    {
        return new BrowseResult(statusCode, continuationPoint, references.toArray(new ReferenceDescription[0]));
    }

    public CompletableFuture<Void> execute()
    {
        return CompletableFuture.completedFuture(null);
    }

    protected Void buildErrorResponse(Throwable exception)
    {
        statusCode = StatusCode.of(StatusCodes.Bad_UnexpectedError);

        if (exception instanceof UaRuntimeException)
        {
            statusCode = ((UaRuntimeException)exception).getStatusCode();
        }

        return null;
    }
}

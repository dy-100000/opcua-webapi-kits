package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadAtTimeDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadProcessedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;
import org.springframework.lang.Nullable;

public class ReadPropertyHistoryValuesRequest {
    private final String id;
    private final String propertyToRead;
    private final UaStructuredType details;
    private final ObjectServiceContext context;

    public ReadPropertyHistoryValuesRequest(
            ObjectServiceContext context,
            String propertyToRead,
            UaStructuredType details)
    {
        this.id = context.getObjectId().getId();
        this.propertyToRead = propertyToRead;
        this.details = details;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getPropertyToRead() {
        return propertyToRead;
    }

    public @Nullable ReadRawModifiedDetails getReadRawDetails() {
        if (details instanceof ReadRawModifiedDetails) return (ReadRawModifiedDetails)details;
        return null;
    }

    public @Nullable ReadAtTimeDetails getReadAtTimeDetails() {
        if (details instanceof ReadAtTimeDetails) return (ReadAtTimeDetails)details;
        return null;
    }

    public @Nullable ReadProcessedDetails getReadProcessedDetails() {
        if (details instanceof ReadProcessedDetails) return (ReadProcessedDetails)details;
        return null;
    }

    public ObjectServiceContext getContext() {
        return context;
    }
}

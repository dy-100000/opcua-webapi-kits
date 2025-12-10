package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

public class GetPropertySubElementsRequest {
    private final String id;
    private final String propertyId;
    private final ObjectServiceContext context;

    public GetPropertySubElementsRequest(
            ObjectServiceContext context,
            String propertyId)
    {
        this.id = context.getObjectId().getId();
        this.propertyId = propertyId;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public ObjectServiceContext getContext() {
        return context;
    }
}

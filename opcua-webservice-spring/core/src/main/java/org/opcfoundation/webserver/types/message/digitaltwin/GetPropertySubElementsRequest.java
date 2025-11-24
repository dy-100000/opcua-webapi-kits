package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ServiceContext;

public class GetPropertySubElementsRequest {
    private final String id;
    private final String propertyId;
    private final ServiceContext context;

    public GetPropertySubElementsRequest(
            ServiceContext context,
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

    public ServiceContext getContext() {
        return context;
    }
}

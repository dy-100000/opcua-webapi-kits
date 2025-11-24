package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ServiceContext;

public class GetElementsRequest {
    private final String id;
    private final ServiceContext context;

    public GetElementsRequest(ServiceContext context)
    {
        this.id = context.getObjectId().getId();
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public ServiceContext getContext() {
        return context;
    }
}

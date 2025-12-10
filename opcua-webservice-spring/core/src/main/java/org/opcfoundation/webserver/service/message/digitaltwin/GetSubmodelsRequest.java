package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

public class GetSubmodelsRequest {
    private final String id;
    private final ObjectServiceContext context;

    public GetSubmodelsRequest(ObjectServiceContext context)
    {
        this.id = context.getObjectId().getId();
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public ObjectServiceContext getContext() {
        return context;
    }
}

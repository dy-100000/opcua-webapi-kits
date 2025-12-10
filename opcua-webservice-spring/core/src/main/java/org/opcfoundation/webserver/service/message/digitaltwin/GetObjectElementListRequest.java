package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

public class GetObjectElementListRequest {
    private final String id;
    private final int limit;
    private final int offset;
    private final ObjectServiceContext context;

    public GetObjectElementListRequest(
            ObjectServiceContext context,
            int limit,
            int offset)
    {
        this.id = context.getObjectId().getId();
        this.limit = limit;
        this.offset = offset;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public ObjectServiceContext getContext() {
        return context;
    }
}

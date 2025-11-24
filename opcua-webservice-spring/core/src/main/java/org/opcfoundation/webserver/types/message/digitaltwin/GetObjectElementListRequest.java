package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ServiceContext;

public class GetObjectElementListRequest {
    private final String id;
    private final int limit;
    private final int offset;
    private final ServiceContext context;

    public GetObjectElementListRequest(
            ServiceContext context,
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

    public ServiceContext getContext() {
        return context;
    }
}

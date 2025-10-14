package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaObjectId;

public class GetVariableDirectoryChildRequest {
    private final UaObjectId objectId;
    private final int limit;
    private final int offset;

    public GetVariableDirectoryChildRequest(
            UaObjectId objectId,
            int limit,
            int offset)
    {
        this.objectId = objectId;
        this.limit = limit;
        this.offset = offset;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
}

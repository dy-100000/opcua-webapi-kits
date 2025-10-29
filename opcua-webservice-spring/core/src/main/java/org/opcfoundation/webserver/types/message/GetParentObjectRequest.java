package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaObjectId;

public class GetParentObjectRequest {
    private final UaObjectId objectId;

    public GetParentObjectRequest(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }
}

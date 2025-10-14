package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaObjectId;

import java.util.Set;

public class GetChildObjectIdRequest {
    private final UaObjectId objectId;

    public GetChildObjectIdRequest(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }
}

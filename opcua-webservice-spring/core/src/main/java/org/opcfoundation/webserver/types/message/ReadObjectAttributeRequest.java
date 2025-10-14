package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaObjectId;

public class ReadObjectAttributeRequest {
    private final UaObjectId objectId;

    public ReadObjectAttributeRequest(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }
}

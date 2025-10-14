package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaObjectId;

public class GetOptionalMemberRequest {
    private final UaObjectId objectId;

    public GetOptionalMemberRequest(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }
}

package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.UaObjectId;

public class ReadMemberAttributeRequest {
    private final UaObjectId objectId;
    private final UaChildId childId;

    public ReadMemberAttributeRequest(
            UaObjectId objectId,
            UaChildId childId)
    {
        this.objectId = objectId;
        this.childId = childId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public UaChildId getChildId() {
        return childId;
    }
}

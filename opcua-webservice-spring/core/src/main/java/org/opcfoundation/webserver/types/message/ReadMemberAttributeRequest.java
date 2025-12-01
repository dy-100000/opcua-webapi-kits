package org.opcfoundation.webserver.types.message;

import org.opcfoundation.webserver.types.UaChildId;
import org.opcfoundation.webserver.types.UaObjectId;

public class ReadMemberAttributeRequest {
    private final UaObjectId objectId;
    private final UaChildId childId;
    private final boolean isMethod;

    public ReadMemberAttributeRequest(
            UaObjectId objectId,
            UaChildId childId,
            boolean isMethod)
    {
        this.objectId = objectId;
        this.childId = childId;
        this.isMethod = isMethod;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public UaChildId getChildId() {
        return childId;
    }

    public boolean isMethod() {
        return isMethod;
    }
}

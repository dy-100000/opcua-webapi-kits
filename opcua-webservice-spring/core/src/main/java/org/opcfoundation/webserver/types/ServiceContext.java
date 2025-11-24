package org.opcfoundation.webserver.types;

import java.util.ArrayList;
import java.util.List;

public class ServiceContext {
    private final UaObjectId objectId;

    public ServiceContext(UaObjectId objectId)
    {
        this.objectId = objectId;
    }

    public UaObjectId getObjectId() {
        return objectId;
    }

    public String getCurrentObjectName()
    {
        return (null == objectId.getInstance()) ? objectId.getId() : objectId.getInstance().browseName();
    }

    public List<String> getCurrentObjectPath()
    {
        // To be implemented
        return new ArrayList<>();
    }
}

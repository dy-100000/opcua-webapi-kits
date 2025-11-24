package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaChildId;

import java.util.Set;

public class ReadPropertyListValueRequest {
    private final String id;
    private final Set<String> propertyIds;
    private final Set<UaChildId> subPropertyIds;
    private final ServiceContext context;

    public ReadPropertyListValueRequest(
            ServiceContext context,
            Set<String> propertyIds,
            Set<UaChildId> subPropertyIds)
    {
        this.id = context.getObjectId().getId();
        this.propertyIds = propertyIds;
        this.subPropertyIds = subPropertyIds;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public Set<String> getPropertyIds() {
        return propertyIds;
    }

    public Set<UaChildId> getSubPropertyIds() {
        return subPropertyIds;
    }

    public ServiceContext getContext() {
        return context;
    }
}

package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.Set;

public class ReadPropertyListValueRequest {
    private final String id;
    private final Set<String> propertyIds;
    private final Set<UaChildId> subPropertyIds;
    private final ObjectServiceContext context;

    public ReadPropertyListValueRequest(
            ObjectServiceContext context,
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

    public ObjectServiceContext getContext() {
        return context;
    }
}

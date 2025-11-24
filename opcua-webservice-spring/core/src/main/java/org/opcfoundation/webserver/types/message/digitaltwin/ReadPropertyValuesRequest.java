package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ServiceContext;

import java.util.Set;

public class ReadPropertyValuesRequest {
    private final String id;
    private final Set<String> propertyNames;
    private final ServiceContext context;

    public ReadPropertyValuesRequest(
            ServiceContext context,
            Set<String> propertyNames)
    {
        this.id = context.getObjectId().getId();
        this.propertyNames = propertyNames;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public Set<String> getPropertyNames() {
        return propertyNames;
    }

    public ServiceContext getContext() {
        return context;
    }
}

package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

import java.util.Set;

public class ReadPropertyValuesRequest {
    private final String id;
    private final Set<String> propertyNames;
    private final ObjectServiceContext context;

    public ReadPropertyValuesRequest(
            ObjectServiceContext context,
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

    public ObjectServiceContext getContext() {
        return context;
    }
}

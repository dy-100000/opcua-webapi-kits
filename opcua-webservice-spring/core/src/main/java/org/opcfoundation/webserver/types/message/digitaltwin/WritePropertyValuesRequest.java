package org.opcfoundation.webserver.types.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.ServiceContext;

import java.util.Map;

public class WritePropertyValuesRequest {
    private final String id;
    private final Map<String, Variant> propertyNamesAndValues;
    private final ServiceContext context;

    public WritePropertyValuesRequest(
            ServiceContext context,
            Map<String, Variant> propertyNamesAndValues)
    {
        this.id = context.getObjectId().getId();
        this.propertyNamesAndValues = propertyNamesAndValues;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public Map<String, Variant> getPropertyNamesAndValues() {
        return propertyNamesAndValues;
    }

    public ServiceContext getContext() {
        return context;
    }
}

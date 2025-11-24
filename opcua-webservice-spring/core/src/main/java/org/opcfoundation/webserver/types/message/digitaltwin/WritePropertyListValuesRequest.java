package org.opcfoundation.webserver.types.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.ServiceContext;
import org.opcfoundation.webserver.types.UaChildId;

import java.util.Map;

public class WritePropertyListValuesRequest {
    private final String id;
    private final Map<String, Variant> propertyIdsAndValues;
    private final Map<UaChildId, Variant> subPropertyIdsAndValues;
    private final ServiceContext context;

    public WritePropertyListValuesRequest(
            ServiceContext context,
            Map<String, Variant> propertyIdsAndValues,
            Map<UaChildId, Variant> subPropertyIdsAndValues)
    {
        this.id = context.getObjectId().getId();
        this.propertyIdsAndValues = propertyIdsAndValues;
        this.subPropertyIdsAndValues = subPropertyIdsAndValues;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public Map<String, Variant> getPropertyIdsAndValues() {
        return propertyIdsAndValues;
    }

    public Map<UaChildId, Variant> getSubPropertyIdsAndValues() {
        return subPropertyIdsAndValues;
    }

    public ServiceContext getContext() {
        return context;
    }
}

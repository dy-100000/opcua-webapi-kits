package org.opcfoundation.webserver.service.message.digitaltwin;

import org.jspecify.annotations.Nullable;
import org.opcfoundation.webserver.types.digitaltwin.ObjectServiceContext;

public class GetPropertyDescriptorRequest {
    private final String id;
    private final String propertyId;
    private final @Nullable String subElementName;
    private final ObjectServiceContext context;

    public GetPropertyDescriptorRequest(
            ObjectServiceContext context,
            String propertyId,
            @Nullable String subElementName)
    {
        this.id = context.getObjectId().getId();
        this.propertyId = propertyId;
        this.subElementName = subElementName;
        this.context = context;
    }

    public String getId() {
        return id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public @Nullable String getSubElementName() {
        return subElementName;
    }

    public ObjectServiceContext getContext() {
        return context;
    }
}

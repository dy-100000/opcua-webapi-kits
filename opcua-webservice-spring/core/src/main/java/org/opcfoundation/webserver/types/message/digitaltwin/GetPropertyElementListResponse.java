package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.PropertyElementDescriptor;

import java.util.List;

public class GetPropertyElementListResponse {
    private final List<PropertyElementDescriptor> elements;
    private final boolean containsMoreData;

    public GetPropertyElementListResponse(
            List<PropertyElementDescriptor> elements,
            boolean containsMoreData)
    {
        this.elements = elements;
        this.containsMoreData = containsMoreData;
    }

    public List<PropertyElementDescriptor> getElements() {
        return elements;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }
}

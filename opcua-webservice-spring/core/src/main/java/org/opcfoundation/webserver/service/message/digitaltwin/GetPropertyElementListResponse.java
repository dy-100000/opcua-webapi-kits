package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.PropertyElementDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GetPropertyElementListResponse {
    private final List<PropertyElementDescriptor> elements;
    private boolean containsMoreData;

    public GetPropertyElementListResponse()
    {
        this.elements = new ArrayList<>();
        this.containsMoreData = false;
    }

    public void add(PropertyElementDescriptor descriptor)
    {
        elements.add(descriptor);
    }

    public List<PropertyElementDescriptor> getElements() {
        return elements;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }

    public void setContainsMoreData(boolean containsMoreData) {
        this.containsMoreData = containsMoreData;
    }
}

package org.opcfoundation.webserver.service.message.digitaltwin;

import org.opcfoundation.webserver.types.digitaltwin.ObjectElementDescriptor;

import java.util.ArrayList;
import java.util.List;

public class GetObjectElementListResponse {
    private final List<ObjectElementDescriptor> elements;
    private boolean containsMoreData;

    public GetObjectElementListResponse()
    {
        this.elements = new ArrayList<>();
        this.containsMoreData = false;
    }

    public List<ObjectElementDescriptor> getElements() {
        return elements;
    }

    public void add(ObjectElementDescriptor descriptor)
    {
        elements.add(descriptor);
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }

    public void setContainsMoreData(boolean containsMoreData) {
        this.containsMoreData = containsMoreData;
    }
}

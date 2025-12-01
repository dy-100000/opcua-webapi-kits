package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ObjectElementDescriptor;
import org.opcfoundation.webserver.types.ReferenceTargetDescriptor;

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

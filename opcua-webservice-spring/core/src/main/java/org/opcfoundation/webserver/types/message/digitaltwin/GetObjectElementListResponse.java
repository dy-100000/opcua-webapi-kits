package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ObjectElementDescriptor;

import java.util.List;

public class GetObjectElementListResponse {
    private final List<ObjectElementDescriptor> elements;
    private final boolean containsMoreData;

    public GetObjectElementListResponse(
            List<ObjectElementDescriptor> elements,
            boolean containsMoreData)
    {
        this.elements = elements;
        this.containsMoreData = containsMoreData;
    }

    public List<ObjectElementDescriptor> getElements() {
        return elements;
    }

    public boolean containsMoreData()
    {
        return containsMoreData;
    }
}

package org.opcfoundation.webserver.types.message.digitaltwin;

import org.opcfoundation.webserver.types.ObjectElementDescriptor;

import java.util.HashSet;
import java.util.Set;

public class GetPropertySubElementsResponse {
    private final Set<String> subElementNames;

    public GetPropertySubElementsResponse()
    {
        this.subElementNames = new HashSet<>();
    }

    public void add(String subElementName)
    {
        subElementNames.add(subElementName);
    }

    public Set<String> getSubElementNames() {
        return subElementNames;
    }
}

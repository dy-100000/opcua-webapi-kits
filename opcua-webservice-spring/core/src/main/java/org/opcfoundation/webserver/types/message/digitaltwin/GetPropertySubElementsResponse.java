package org.opcfoundation.webserver.types.message.digitaltwin;

import java.util.Set;

public class GetPropertySubElementsResponse {
    private final Set<String> subElementNames;

    public GetPropertySubElementsResponse(Set<String> subElementNames)
    {
        this.subElementNames = subElementNames;
    }

    public Set<String> getSubElementNames() {
        return subElementNames;
    }
}

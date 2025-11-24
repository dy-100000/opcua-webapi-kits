package org.opcfoundation.webserver.types.message.digitaltwin;

import java.util.Set;

public class GetElementsResponse {
    private final Set<String> elementNames;

    public GetElementsResponse(Set<String> elementNames)
    {
        this.elementNames = elementNames;
    }

    public Set<String> getElementNames() {
        return elementNames;
    }
}

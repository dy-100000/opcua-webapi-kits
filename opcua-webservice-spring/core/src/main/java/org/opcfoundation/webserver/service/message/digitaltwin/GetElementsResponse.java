package org.opcfoundation.webserver.service.message.digitaltwin;

import java.util.HashSet;
import java.util.Set;

public class GetElementsResponse {
    private final Set<String> elementNames;

    public GetElementsResponse()
    {
        this.elementNames = new HashSet<>();
    }

    public void add(String elementName)
    {
        elementNames.add(elementName);
    }

    public Set<String> getElementNames() {
        return elementNames;
    }
}

package org.opcfoundation.webserver.types.message.digitaltwin;

import java.util.HashMap;
import java.util.Map;

public class GetSubmodelsResponse {
    private final Map<String,String> submodelIdsByName;

    public GetSubmodelsResponse()
    {
        this.submodelIdsByName = new HashMap<>();
    }

    public void add(String submodelId, String submodelName)
    {
        submodelIdsByName.put(submodelName, submodelId);
    }

    public Map<String, String> getSubmodelIdsByName() {
        return submodelIdsByName;
    }
}

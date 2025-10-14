package org.opcfoundation.webserver.types.message;

import java.util.HashMap;
import java.util.Map;

public class GetChildObjectIdResponse {
    private final Map<String, String> childObjectIdsByName;

    public GetChildObjectIdResponse()
    {
        childObjectIdsByName = new HashMap<>();
    }

    public void addChildId(String id, String name)
    {
        childObjectIdsByName.put(name, id);
    }

    public Map<String, String> getChildrenIdsByName() {
        return childObjectIdsByName;
    }
}

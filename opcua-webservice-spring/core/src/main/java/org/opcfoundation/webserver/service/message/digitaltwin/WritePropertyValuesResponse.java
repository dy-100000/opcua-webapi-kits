package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.HashMap;
import java.util.Map;

public class WritePropertyValuesResponse {
    private final Map<UaChildId, StatusCode> results;

    public WritePropertyValuesResponse()
    {
        results = new HashMap<>();
    }

    public void setWriteValueResult(String propertyName, StatusCode code)
    {
        results.put(new UaChildId(propertyName), code);
    }

    public Map<UaChildId, StatusCode> getResults() {
        return results;
    }
}

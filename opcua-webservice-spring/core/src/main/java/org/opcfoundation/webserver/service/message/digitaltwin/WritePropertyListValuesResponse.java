package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.HashMap;
import java.util.Map;

public class WritePropertyListValuesResponse {
    private final Map<UaChildId, StatusCode> results;

    public WritePropertyListValuesResponse()
    {
        results = new HashMap<>();
    }

    public void setWriteValueResult(String propertyId, StatusCode code)
    {
        results.put(new UaChildId(propertyId), code);
    }

    public void setWriteValueResult(UaChildId subPropertyId,StatusCode code)
    {
        results.put(subPropertyId, code);
    }

    public Map<UaChildId, StatusCode> getResults() {
        return results;
    }
}

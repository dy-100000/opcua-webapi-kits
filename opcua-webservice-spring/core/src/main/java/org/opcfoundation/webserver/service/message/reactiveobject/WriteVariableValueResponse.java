package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WriteVariableValueResponse {
    private final Map<UaChildId, StatusCode> results;

    public WriteVariableValueResponse()
    {
        results = new HashMap<>();
    }

    public WriteVariableValueResponse(Map<UaChildId, StatusCode> results) { this.results = results; }

    public void setOperationResults(WriteVariableValueRequest request, StatusCode code)
    {
        Set<UaChildId> childIds = request.getVariableValues().keySet();
        for (UaChildId item : childIds)
        {
            results.put(item, code);
        }
    }

    public void setOperationResult(String id, StatusCode code)
    {
        results.put(new UaChildId(id), code);
    }

    public void setOperationResult(UaChildId id, StatusCode code)
    {
        results.put(id, code);
    }

    public Map<UaChildId, StatusCode> getResults() {
        return results;
    }
}

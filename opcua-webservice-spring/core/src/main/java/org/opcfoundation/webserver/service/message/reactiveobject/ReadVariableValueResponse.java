package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.HashMap;
import java.util.Map;

public class ReadVariableValueResponse {
    private final Map<UaChildId, DataValue> results;

    public ReadVariableValueResponse()
    {
        results = new HashMap<>();
    }

    public ReadVariableValueResponse(Map<UaChildId, DataValue> results) { this.results = results; }

    public void setValue(String path, Variant value)
    {
        results.put(new UaChildId(path), new DataValue(value, StatusCode.GOOD, null, null));
    }

    public void setValue(UaChildId path, Variant value)
    {
        results.put(path, new DataValue(value, StatusCode.GOOD, null, null));
    }

    public void setError(String path, StatusCode error)
    {
        results.put(new UaChildId(path), new DataValue(Variant.NULL_VALUE, error, null, null));
    }

    public void setError(UaChildId path, StatusCode error)
    {
        results.put(path, new DataValue(Variant.NULL_VALUE, error, null, null));
    }

    public Map<UaChildId, DataValue> getResults() {
        return results;
    }
}

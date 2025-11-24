package org.opcfoundation.webserver.types.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.UaChildId;

import java.util.HashMap;
import java.util.Map;

public class ReadPropertyListValueResponse {
    private final Map<UaChildId, DataValue> results;

    public ReadPropertyListValueResponse()
    {
        results = new HashMap<>();
    }

    public void setValue(String propertyId, Variant value)
    {
        results.put(new UaChildId(propertyId), new DataValue(value, StatusCode.GOOD, null, null));
    }

    public void setValue(String propertyId, String subPropertyName, Variant value)
    {
        results.put(new UaChildId(propertyId,subPropertyName), new DataValue(value, StatusCode.GOOD, null, null));
    }

    public void setError(String propertyId, StatusCode errorCode)
    {
        results.put(new UaChildId(propertyId), new DataValue(Variant.NULL_VALUE, errorCode, null, null));
    }

    public void setError(String propertyId, String subPropertyName, StatusCode errorCode)
    {
        results.put(new UaChildId(propertyId,subPropertyName), new DataValue(Variant.NULL_VALUE, errorCode, null, null));
    }

    public Map<UaChildId, DataValue> getResults() {
        return results;
    }
}

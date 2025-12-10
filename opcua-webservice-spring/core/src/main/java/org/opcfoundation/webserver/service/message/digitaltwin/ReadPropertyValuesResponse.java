package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.opcfoundation.webserver.types.common.UaChildId;

import java.util.HashMap;
import java.util.Map;

public class ReadPropertyValuesResponse {
    private final Map<UaChildId, DataValue> results;

    public ReadPropertyValuesResponse()
    {
        results = new HashMap<>();
    }

    public void setValue(String elementName, Variant value)
    {
        results.put(new UaChildId(elementName), new DataValue(value, StatusCode.GOOD, null, null));
    }

    public void setError(String elementName, StatusCode errorCode)
    {
        results.put(new UaChildId(elementName), new DataValue(Variant.NULL_VALUE, errorCode, null, null));
    }

    public Map<UaChildId, DataValue> getResults() {
        return results;
    }
}

package org.opcfoundation.webserver.service.message.reactiveobject;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.List;

public class ReadHistoryDataResponse {
    private final List<DataValue> values;
    private final Boolean containsMoreData;

    public ReadHistoryDataResponse(List<DataValue> values, Boolean containsMoreData)
    {
        this.values = values;
        this.containsMoreData = containsMoreData;
    }

    public List<DataValue> getValues() {
        return values;
    }

    public Boolean getContainsMoreData() {
        return containsMoreData;
    }
}

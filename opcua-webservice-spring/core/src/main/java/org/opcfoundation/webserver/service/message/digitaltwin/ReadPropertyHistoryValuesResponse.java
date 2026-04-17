package org.opcfoundation.webserver.service.message.digitaltwin;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

import java.util.ArrayList;
import java.util.List;

public class ReadPropertyHistoryValuesResponse {
    List<DataValue> dataValues;
    boolean containsMoreData;

    public ReadPropertyHistoryValuesResponse()
    {
        dataValues = new ArrayList<>();
        containsMoreData = false;
    }

    public ReadPropertyHistoryValuesResponse(
            List<DataValue> dataValues,
            boolean containsMoreData)
    {
        this.dataValues = dataValues;
        this.containsMoreData = containsMoreData;
    }

    public void addDataValue(DataValue value)
    {
        dataValues.add(value);
    }

    public List<DataValue> getDataValues() {
        return dataValues;
    }

    public void setContainsMoreData(boolean containsMoreData) {
        this.containsMoreData = containsMoreData;
    }

    public boolean containsMoreData() {
        return containsMoreData;
    }
}

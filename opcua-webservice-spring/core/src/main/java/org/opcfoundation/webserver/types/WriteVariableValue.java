package org.opcfoundation.webserver.types;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

public class WriteVariableValue {
    private final UaChildId variableId;
    private final DataValue value;

    public WriteVariableValue(
            UaChildId variableId,
            DataValue value)
    {
        this.variableId = variableId;
        this.value = value;
    }

    public UaChildId getVariableId() {
        return variableId;
    }

    public DataValue getValue() {
        return value;
    }
}

package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaVariableType;

public class UaVariableTypes {
    public final static UaVariableType BaseVariableType = new UaVariableType(NodeIds.BaseVariableType,"BaseVariableType", new LocalizedText("BaseVariableType"), true, NodeIds.BaseDataType, -2);
    public final static UaVariableType BaseDataVariableType = new UaVariableType(NodeIds.BaseDataVariableType,"BaseDataVariableType", new LocalizedText("BaseDataVariableType"), false, NodeIds.BaseDataType,-2);
    public final static UaVariableType PropertyType = new UaVariableType(NodeIds.PropertyType,"PropertyType", new LocalizedText("PropertyType"), false, NodeIds.BaseDataType,-2);

    public final static UaVariableType DataItemType = new UaVariableType(NodeIds.DataItemType,"DataItemType", new LocalizedText("DataItemType"), false, NodeIds.BaseDataVariableType,-2);
    public final static UaVariableType BaseAnalogType = new UaVariableType(NodeIds.BaseAnalogType,"BaseAnalogType", new LocalizedText("BaseAnalogType"), false, NodeIds.Number,-2);
}

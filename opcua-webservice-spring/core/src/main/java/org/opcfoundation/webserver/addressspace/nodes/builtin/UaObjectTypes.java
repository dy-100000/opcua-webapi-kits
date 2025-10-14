package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.BaseUaObjectType;

public class UaObjectTypes {
    public final static BaseUaObjectType BaseObjectType = new BaseUaObjectType(NodeIds.BaseObjectType,"BaseObjectType", new LocalizedText("BaseObjectType"), false, null);
    public final static BaseUaObjectType FolderType = new BaseUaObjectType(NodeIds.FolderType,"FolderType", new LocalizedText("FolderType"), false, BaseObjectType);
    public final static BaseUaObjectType ServerType = new BaseUaObjectType(NodeIds.ServerType,"ServerType", new LocalizedText("ServerType"), false, BaseObjectType);

    public final static BaseUaObjectType ModellingRuleType = new BaseUaObjectType(NodeIds.ModellingRuleType,"ModellingRuleType", new LocalizedText("ModellingRuleType"), false, BaseObjectType);

    public final static BaseUaObjectType BaseEventType = new BaseUaObjectType(NodeIds.BaseEventType,"BaseEventType", new LocalizedText("BaseEventType"), false, BaseObjectType);
}

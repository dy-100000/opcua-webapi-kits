package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.BaseUaObjectType;

public class UaObjectTypes {
    public final static BaseUaObjectType BaseObjectType = new BaseUaObjectType(NodeIds.BaseObjectType,"BaseObjectType", new LocalizedText("BaseObjectType"), false, null);
    public final static BaseUaObjectType FolderType = new BaseUaObjectType(NodeIds.FolderType,"FolderType", new LocalizedText("FolderType"), false, BaseObjectType);
    public final static BaseUaObjectType ServerType = new BaseUaObjectType(NodeIds.ServerType,"ServerType", new LocalizedText("ServerType"), false, BaseObjectType);

    public final static BaseUaObjectType ModellingRuleType = new BaseUaObjectType(NodeIds.ModellingRuleType,"ModellingRuleType", new LocalizedText("ModellingRuleType"), false, BaseObjectType);

    public final static BaseUaObjectType BaseEventType = new BaseUaObjectType(NodeIds.BaseEventType,"BaseEventType", new LocalizedText("BaseEventType"), false, BaseObjectType);

    public final static BaseUaObjectType DigitalTwinType = new BaseUaObjectType(new NodeId(1,10),"DigitalTwinType", new LocalizedText("DigitalTwinType"), true, BaseObjectType);
    public final static BaseUaObjectType DigitalTwinDirectoryType = new BaseUaObjectType(new NodeId(1,11),"DigitalTwinDirectoryType", new LocalizedText("DigitalTwinDirectoryType"), true, BaseObjectType);
    public final static BaseUaObjectType SubmodelType = new BaseUaObjectType(new NodeId(1,12),"SubmodelType", new LocalizedText("SubmodelType"), true, BaseObjectType);
    public final static BaseUaObjectType ElementType = new BaseUaObjectType(new NodeId(1,13),"ElementType", new LocalizedText("ElementType"), true, BaseObjectType);
    public final static BaseUaObjectType ReferenceElementType = new BaseUaObjectType(new NodeId(1,14),"ReferenceElementType", new LocalizedText("ReferenceElementType"), true, ElementType);
}

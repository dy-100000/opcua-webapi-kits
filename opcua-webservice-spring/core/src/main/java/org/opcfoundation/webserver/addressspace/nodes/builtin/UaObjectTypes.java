package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.UaObjectType;

public class UaObjectTypes {
    public final static UaObjectType BaseObjectType = new UaObjectType(NodeIds.BaseObjectType,"BaseObjectType", new LocalizedText("BaseObjectType"), false, null);
    public final static UaObjectType FolderType = new UaObjectType(NodeIds.FolderType,"FolderType", new LocalizedText("FolderType"), false, BaseObjectType);
    public final static UaObjectType ServerType = new UaObjectType(NodeIds.ServerType,"ServerType", new LocalizedText("ServerType"), false, BaseObjectType);

    public final static UaObjectType ModellingRuleType = new UaObjectType(NodeIds.ModellingRuleType,"ModellingRuleType", new LocalizedText("ModellingRuleType"), false, BaseObjectType);

    public final static UaObjectType BaseEventType = new UaObjectType(NodeIds.BaseEventType,"BaseEventType", new LocalizedText("BaseEventType"), false, BaseObjectType);

    public final static UaObjectType DigitalTwinType = new UaObjectType(new NodeId(1,10),"DigitalTwinType", new LocalizedText("DigitalTwinType"), true, BaseObjectType);
    public final static UaObjectType DigitalTwinRepositoryType = new UaObjectType(new NodeId(1,11),"DigitalTwinRepositoryType", new LocalizedText("DigitalTwinRepositoryType"), true, BaseObjectType);
    public final static UaObjectType SubmodelType = new UaObjectType(new NodeId(1,12),"SubmodelType", new LocalizedText("SubmodelType"), true, BaseObjectType);
    public final static UaObjectType ElementType = new UaObjectType(new NodeId(1,13),"ElementType", new LocalizedText("ElementType"), true, BaseObjectType);
    public final static UaObjectType ReferenceElementType = new UaObjectType(new NodeId(1,14),"ReferenceElementType", new LocalizedText("ReferenceElementType"), true, ElementType);
    public final static UaObjectType ElementCollectionType = new UaObjectType(new NodeId(1,15),"ElementCollectionType", new LocalizedText("ElementCollectionType"), true, ElementType);
    public final static UaObjectType ElementListType = new UaObjectType(new NodeId(1,16),"ElementListType", new LocalizedText("ElementListType"), true, ElementType);
    public final static UaObjectType EventElementType = new UaObjectType(new NodeId(1,17),"EventElementType", new LocalizedText("EventElementType"), true, ElementType);
}

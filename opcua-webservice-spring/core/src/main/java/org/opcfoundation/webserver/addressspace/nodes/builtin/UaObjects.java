package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.opcfoundation.webserver.addressspace.nodes.UaObject;

public class UaObjects {
    public final static UaObject RootFolder = new UaObject(NodeIds.RootFolder, "Root", new LocalizedText("Root"), UaObjectTypes.FolderType);
    public final static UaObject ObjectsFolder = new UaObject(NodeIds.ObjectsFolder, "Objects", new LocalizedText("Objects"), UaObjectTypes.FolderType);
    public final static UaObject Server = new UaObject(NodeIds.Server, "Server", new LocalizedText("Server"), UaObjectTypes.ServerType);

    public final static UaObject TypesFolder = new UaObject(NodeIds.TypesFolder, "Types", new LocalizedText("Types"), UaObjectTypes.FolderType);
    public final static UaObject ObjectTypesFolder = new UaObject(NodeIds.ObjectTypesFolder, "ObjectTypes", new LocalizedText("ObjectTypes"), UaObjectTypes.FolderType);
    public final static UaObject VariableTypesFolder = new UaObject(NodeIds.VariableTypesFolder, "VariableTypes", new LocalizedText("VariableTypes"), UaObjectTypes.FolderType);
    public final static UaObject EventTypesFolder = new UaObject(NodeIds.EventTypesFolder, "EventTypes", new LocalizedText("EventTypes"), UaObjectTypes.FolderType);
    public final static UaObject DataTypesFolder = new UaObject(NodeIds.DataTypesFolder, "DataTypes", new LocalizedText("DataTypes"), UaObjectTypes.FolderType);
    public final static UaObject ReferenceTypesFolder = new UaObject(NodeIds.ReferenceTypesFolder, "ReferenceTypes", new LocalizedText("ReferenceTypes"), UaObjectTypes.FolderType);
    public final static UaObject ViewsFolder = new UaObject(NodeIds.ViewsFolder, "Views", new LocalizedText("Views"), UaObjectTypes.FolderType);

    public final static UaObject ModellingRule_Mandatory = new UaObject(NodeIds.ModellingRule_Mandatory, "Mandatory", new LocalizedText("Mandatory"),UaObjectTypes.ModellingRuleType);
    public final static UaObject ModellingRule_Optional = new UaObject(NodeIds.ModellingRule_Optional, "Optional", new LocalizedText("Optional"),UaObjectTypes.ModellingRuleType);
    public final static UaObject ModellingRule_MandatoryPlaceHolder = new UaObject(NodeIds.ModellingRule_MandatoryPlaceholder, "MandatoryPlaceholder", new LocalizedText("MandatoryPlaceholder"),UaObjectTypes.ModellingRuleType);
    public final static UaObject ModellingRule_OptionalPlaceHolder = new UaObject(NodeIds.ModellingRule_OptionalPlaceholder, "OptionalPlaceholder", new LocalizedText("OptionalPlaceholder"),UaObjectTypes.ModellingRuleType);
}

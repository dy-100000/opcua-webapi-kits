package org.opcfoundation.webserver.addressspace.nodes.builtin;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.opcfoundation.webserver.addressspace.nodes.UaReferenceType;

public class UaReferenceTypes {
    public final static UaReferenceType References = new UaReferenceType(NodeIds.References,"References", new LocalizedText("References"), true,LocalizedText.NULL_VALUE, true);
    public final static UaReferenceType HierarchicalReferences = new UaReferenceType(NodeIds.HierarchicalReferences,"HierarchicalReferences", new LocalizedText("HierarchicalReferences"), true,new LocalizedText("InverseHierarchicalReferences"), false);
    public final static UaReferenceType NonHierarchicalReferences = new UaReferenceType(NodeIds.NonHierarchicalReferences,"NonHierarchicalReferences", new LocalizedText("NonHierarchicalReferences"), true, LocalizedText.NULL_VALUE, true);

    public final static UaReferenceType Organizes = new UaReferenceType(NodeIds.Organizes,"Organizes", new LocalizedText("Organizes"), false,new LocalizedText("OrganizedBy"), false);
    public final static UaReferenceType HasChild = new UaReferenceType(NodeIds.HasChild,"HasChild", new LocalizedText("HasChild"), true,new LocalizedText("ChildOf"), false);
    public final static UaReferenceType HasSubType = new UaReferenceType(NodeIds.HasSubtype,"HasSubtype", new LocalizedText("HasSubtype"), false,new LocalizedText("SubtypeOf"), false);
    public final static UaReferenceType Aggregates = new UaReferenceType(NodeIds.Aggregates,"Aggregates", new LocalizedText("Aggregates"), true,new LocalizedText("AggregatedBy"), false);
    public final static UaReferenceType HasComponent = new UaReferenceType(NodeIds.HasComponent,"HasComponent", new LocalizedText("HasComponent"), false,new LocalizedText("ComponentOf"), false);
    public final static UaReferenceType HasProperty = new UaReferenceType(NodeIds.HasProperty,"HasProperty", new LocalizedText("HasProperty"), false,new LocalizedText("PropertyOf"), false);

    public final static UaReferenceType HasTypeDefinition = new UaReferenceType(NodeIds.HasTypeDefinition,"HasTypeDefinition", new LocalizedText("HasTypeDefinition"), false,new LocalizedText("TypeDefinitionOf"), false);
    public final static UaReferenceType HasModellingRule = new UaReferenceType(NodeIds.HasModellingRule,"HasModellingRule", new LocalizedText("HasModellingRule"), false,new LocalizedText("ModellingRuleOf"), false);

    public final static UaReferenceType HasLink = new UaReferenceType(new NodeId(1,1),"HasLink", new LocalizedText("HasLink"), false,new LocalizedText("HasLink"), true);
}

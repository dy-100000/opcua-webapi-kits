import { ReferenceTypeIds, UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaReferenceType } from "../UaReferenceType";

export class UaReferenceTypes {
    static References = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.References),"References",new UaLocalizedText("References"),true,UaLocalizedText.nullText,true);
    static HierarchicalReferences = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HierarchicalReferences),"HierarchicalReferences",new UaLocalizedText("HierarchicalReferences"),true,new UaLocalizedText("InverseHierarchicalReferences"),false);
    static NonHierarchicalReferences = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.NonHierarchicalReferences),"NonHierarchicalReferences",new UaLocalizedText("NonHierarchicalReferences"),true,UaLocalizedText.nullText,true);

    static Organizes = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.Organizes),"Organizes",new UaLocalizedText("Organizes"),false,new UaLocalizedText("OrganizedBy"),false);
    static HasChild = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HasChild),"HasChild",new UaLocalizedText("HasChild"),true,new UaLocalizedText("ChildOf"),false);
    static HasSubtype = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HasSubtype),"HasSubtype",new UaLocalizedText("HasSubtype"),false,new UaLocalizedText("SubtypeOf"),false);
    static Aggregates = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.Aggregates),"Aggregates",new UaLocalizedText("Aggregates"),true,new UaLocalizedText("AggregatedBy"),false);
    static HasComponent = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HasComponent),"HasComponent",new UaLocalizedText("HasComponent"),false,new UaLocalizedText("ComponentOf"),false);
    static HasProperty = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HasProperty),"HasProperty",new UaLocalizedText("HasProperty"),false,new UaLocalizedText("PropertyOf"),false);

    static HasTypeDefinition = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HasTypeDefinition),"HasTypeDefinition",new UaLocalizedText("HasTypeDefinition"),false,new UaLocalizedText("TypeDefinitionOf"),false);
    static HasModellingRule = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.HasModellingRule),"HasModellingRule",new UaLocalizedText("HasModellingRule"),false,new UaLocalizedText("ModellingRuleOf"),false);
    static GeneratesEvent = new UaReferenceType(UaNodeId.from(ReferenceTypeIds.GeneratesEvent),"GeneratesEvent",new UaLocalizedText("GeneratesEvent"),false,new UaLocalizedText("GeneratedBy"),false);
    static HasLink = new UaReferenceType(new UaNodeId(1,1),"HasLink",new UaLocalizedText("HasLink"),false,new UaLocalizedText("HasLink"),true);
}

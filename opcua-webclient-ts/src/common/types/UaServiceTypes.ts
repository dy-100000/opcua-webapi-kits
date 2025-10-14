import { UaNodeId, UaLocalizedText, UaExpandedNodeId, UaVariant } from ".";

export type UaReferenceDescriptor = {
    nodeId: UaExpandedNodeId;
    nodeClass: number;
    browseName: string;
    displayName: UaLocalizedText;    
    referenceTypeId: UaNodeId;
    isForward: boolean;
    typeDefinition?: UaExpandedNodeId;
}

export type UaBrowseResult = {
    results: Array<UaReferenceDescriptor>;
    continuationPoint?: string
}

export type UaWriteValue = {
    nodeId : UaNodeId;
    value : UaVariant;
}
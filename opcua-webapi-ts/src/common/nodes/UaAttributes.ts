import { UaLocalizedText, UaNodeId } from "../types";

export type OpcUaNodeAttributes = {
    nodeClass : number;
    browseName: string;
    displayName: UaLocalizedText;
    description: UaLocalizedText | null;
}

export type OpcUaVariableAttributes = {
    dataType : UaNodeId;
    valueRank : number;
    accessLevel : number;
    userAccessLevel : number;
    historizing : boolean;
}
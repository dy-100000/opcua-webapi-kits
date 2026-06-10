import { UaLocalizedText, UaNodeId } from "../types";

export type OpcUaNodeAttributes = {
    nodeClass : number;
    browseName: string;
    displayName: UaLocalizedText;
    writeMask: number;
    description: UaLocalizedText | null;
}

export type OpcUaObjectAttributes = {
    eventNotifier : number;
}

export type OpcUaVariableAttributes = {
    dataType : UaNodeId;
    valueRank : number;
    accessLevel : number;
    userAccessLevel : number;
    historizing : boolean;
}
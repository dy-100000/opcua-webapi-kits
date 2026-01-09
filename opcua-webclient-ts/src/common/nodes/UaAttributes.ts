import { UaLocalizedText, UaNodeId } from "../types";

export type UaNodeAttributes = {
    nodeClass : number;
    browseName: string;
    displayName: UaLocalizedText;
    writeMask: number;
    description: UaLocalizedText | null;
}

export type UaObjectAttributes = {
    eventNotifier : number;
}

export type UaVariableAttributes = {
    dataType : UaNodeId;
    valueRank : number;
    accessLevel : number;
    userAccessLevel : number;
    historizing : boolean;
}
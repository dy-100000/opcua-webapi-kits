import { LocalizedText } from "node-opcua-data-model";
import { NodeId } from "node-opcua-nodeid";
export type UaNodeAttributes = {
    nodeClass: number;
    browseName: string;
    displayName: LocalizedText;
    description?: LocalizedText;
};
export type UaVariableAttributes = {
    dataType: NodeId | undefined;
    valueRank: number | undefined;
    accessLevel: number | undefined;
    userAccessLevel: number | undefined;
    historizing: boolean | undefined;
};

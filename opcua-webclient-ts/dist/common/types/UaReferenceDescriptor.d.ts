import { NodeId, ExpandedNodeId } from "node-opcua-nodeid";
import { LocalizedText } from "node-opcua-data-model";
export declare class UaReferenceDescriptor {
    readonly nodeId: ExpandedNodeId;
    readonly nodeClass: number;
    readonly browseName: string;
    readonly displayName: LocalizedText;
    readonly referenceTypeId: NodeId;
    readonly isForward: boolean;
    readonly typeDefinition?: ExpandedNodeId;
    toString(): string;
}

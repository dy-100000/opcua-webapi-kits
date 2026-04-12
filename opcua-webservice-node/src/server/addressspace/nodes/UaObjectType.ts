import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, VariableTypeIds } from "opcua-webapi-ts";
import { UaDefinitionNode } from "./UaDefinitionNode";
import type { UaInstanceNode } from "./UaInstanceNode";

export class UaObjectType extends UaDefinitionNode {
    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean) {
        super(nodeId, browseName, displayName, isAbstract);
    }

    get nodeClass(): NodeClass {
        return NodeClass.ObjectType;
    }

    addMember(member: UaInstanceNode): void {
        this.addMemberNode(member);
    }
}
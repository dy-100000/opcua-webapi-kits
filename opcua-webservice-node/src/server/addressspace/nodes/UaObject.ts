import { Attributes, BrowseDirection, NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaVariant, UaVariantType } from "opcua-webapi-ts";
import { UaInstanceNode } from "./UaInstanceNode";
import { UaNode } from "./UaNode";
import { UaReference } from "./UaReference";
import { UaReferenceTypes } from "./builtin/UaReferenceTypes";
import type { UaObjectType } from "./UaObjectType";

export class UaObject extends UaInstanceNode {
    private readonly _typeDefinition: UaObjectType;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        typeDefinition: UaObjectType) {
        super(nodeId, browseName, displayName);
        this._typeDefinition = typeDefinition;
        this.addReference(new UaReference(typeDefinition, UaReferenceTypes.HasTypeDefinition, true));
    }

    get nodeClass(): NodeClass {
        return NodeClass.Object;
    }

    get typeDefinition(): UaObjectType {
        return this._typeDefinition;
    }

    get parentNode(): UaNode | null {
        const referenceList = this.getReferences(BrowseDirection.Inverse);

        for (const item of referenceList) {
            if (item.reference.nodeId.equal(UaReferenceTypes.HasComponent.nodeId)) {
                return item.linkedNode;
            }
        }

        return null;
    }

    addMember(member: UaInstanceNode): void {
        this.addMemberNode(member);
    }

    organizes(node: UaNode): void {
        this.addReference(new UaReference(node, UaReferenceTypes.Organizes, true));
        node.addReference(new UaReference(this, UaReferenceTypes.Organizes, false));
    }

    getAttribute(attributeId: number): UaVariant {
        if (Attributes.EventNotifier === attributeId) {
            return UaVariant.integer(0, UaVariantType.Byte);
        }

        return super.getAttribute(attributeId);
    }
}
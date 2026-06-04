import { Attributes, BrowseDirection, NodeClass } from "opcua-webapi";
import { ReferenceTypeIds, UaLocalizedText, UaNodeId, UaVariant, UaVariantType, VariableTypeIds } from "opcua-webapi-ts";
import { UaReference } from "./UaReference";
import type { UaInstanceNode } from "./UaInstanceNode";
import type { UaVariable } from "./UaVariable";

function getUaReferenceTypes(): typeof import("./builtin/UaReferenceTypes").UaReferenceTypes {
    return require("./builtin/UaReferenceTypes").UaReferenceTypes;
}

export abstract class UaNode {
    private readonly _nodeId: UaNodeId;
    protected _browseName: string;
    protected _displayName: UaLocalizedText;
    protected _writeMask: number;
    protected _description: UaLocalizedText;
    protected _references: Array<UaReference>;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText) {
        this._nodeId = nodeId;
        this._browseName = browseName;
        this._displayName = displayName;
        this._writeMask = 0;
        this._description = UaLocalizedText.nullText;
        this._references = [];
    }

    get nodeId(): UaNodeId {
        return this._nodeId;
    }

    abstract get nodeClass(): NodeClass;

    public isDefinitionNode(): boolean {
        return (NodeClass.ObjectType === this.nodeClass ||
                NodeClass.VariableType === this.nodeClass ||
                NodeClass.DataType === this.nodeClass ||
                NodeClass.ReferenceType === this.nodeClass);
    }

    public isInstanceNode(): boolean {
        return (NodeClass.Object === this.nodeClass ||
                NodeClass.Variable === this.nodeClass ||
                NodeClass.Method === this.nodeClass);
    }

    get name(): string { 
        return this._browseName; 
    }

    get browseName(): string {
        return this._browseName;
    }

    set browseName(browseName: string) {
        this._browseName = browseName;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    set displayName(displayName: UaLocalizedText) {
        this._displayName = displayName;
    }

    get writeMask(): number {
        return this._writeMask;
    }

    set writeMask(writeMask: number) {
        this._writeMask = writeMask;
    }

    get description(): UaLocalizedText {
        return this._description;
    }

    set description(description: UaLocalizedText) {
        this._description = description;
    }

    getAttribute(attributeId: number): UaVariant {
        let value: UaVariant = UaVariant.null();
        
        if (Attributes.NodeId === attributeId) {
            value = UaVariant.nodeId(this._nodeId);
        } else if (Attributes.NodeClass === attributeId) {
            value = UaVariant.integer(this.nodeClass, UaVariantType.Int32);
        } else if (Attributes.BrowseName === attributeId) {
            value = UaVariant.qualifiedName(this._browseName);
        } else if (Attributes.DisplayName === attributeId) {
            value = UaVariant.localizedText(this._displayName);
        } else if (Attributes.Description === attributeId) {
            if (this._description.text.length != 0) value = UaVariant.localizedText(this._description);            
        } else if (Attributes.WriteMask === attributeId) {
            value = UaVariant.integer(this._writeMask, UaVariantType.UInt32);
        } else if (Attributes.UserWriteMask === attributeId) {
            value = UaVariant.integer(0, UaVariantType.UInt32);
        }

        return value;
    }
    
    getReferences(direction: BrowseDirection): UaReference[] {
        const ret: UaReference[] = [];

        for (let item of this._references) {
            if ((BrowseDirection.Forward === direction && !item.isForward) ||
                (BrowseDirection.Inverse === direction && item.isForward)) continue;

            ret.push(item);
        }

        return ret;
    }

    addReference(reference: UaReference): void {
        this._references.push(reference);
    }
    
    getMembers(): UaInstanceNode[] {
        const referenceList = this.getReferences(BrowseDirection.Forward);
        const members: UaInstanceNode[] = [];

        for (const item of referenceList) {
            if (!item.linkedNode.isInstanceNode()) continue;
            if (!item.reference.isSubtypeOf(UaNodeId.from(ReferenceTypeIds.Aggregates))) continue;
            members.push(item.linkedNode as UaInstanceNode);
        }

        return members;
    }

    getMember(path: string): UaInstanceNode | null {
        for (const item of this._references) {
            if (item.isForward &&
                item.linkedNode.browseName === path &&
                item.linkedNode.isInstanceNode() &&
                item.reference.isSubtypeOf(UaNodeId.from(ReferenceTypeIds.Aggregates))) {
                return item.linkedNode as UaInstanceNode;
            }
        }
        return null;
    }

    protected addMemberNode(member: UaInstanceNode) {
        const UaReferenceTypes = getUaReferenceTypes();
        let reference = UaReferenceTypes.HasComponent;

        if (NodeClass.Variable == member.nodeClass) {
            if ((member as UaVariable).typeDefinition.nodeId.equal(UaNodeId.from(VariableTypeIds.PropertyType)))
            {
                reference = UaReferenceTypes.HasProperty;
            }
        }

        this.addReference(new UaReference(member, reference, true));
        member.addReference(new UaReference(this,reference, false));
    }
}

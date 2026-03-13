import { Attributes, NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaVariant, UaVariantType, VariableIds, VariableTypeIds } from "opcua-webapi-ts";
import { UaDefinitionNode,UaVariable } from ".";

export class UaVariableType extends UaDefinitionNode {
    private readonly _dataType: UaNodeId;
    private readonly _valueRank: number;
    private _value: UaVariant;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        isAbstract: boolean,
        dataType: UaNodeId,
        valueRank: number,
        parentType: UaVariableType | null) {
        super(nodeId, browseName, displayName, isAbstract);
        if (parentType !== null) {
            this.setParentType(parentType);
        }

        this._dataType = dataType;
        this._valueRank = valueRank;
        this._value = UaVariant.null();
    }

    get nodeClass(): NodeClass {
        return NodeClass.VariableType;
    }

    get dataType(): UaNodeId {
        return this._dataType;
    }

    get valueRank(): number {
        return this._valueRank;
    }

    get value(): UaVariant {
        return this._value;
    }

    set value(value: UaVariant) {
        this._value = value;
    }

    addMember(member: UaVariable): void {
        this.addMemberNode(member);
    }

    getAttribute(attributeId: number): UaVariant {
        if (Attributes.Value === attributeId) {
            return this._value;
        }
        if (Attributes.DataType === attributeId) {
            return UaVariant.nodeId(this._dataType);
        }
        if (Attributes.ValueRank === attributeId) {
            return UaVariant.integer(this._valueRank, UaVariantType.Int32);
        }

        return super.getAttribute(attributeId);
    }
}

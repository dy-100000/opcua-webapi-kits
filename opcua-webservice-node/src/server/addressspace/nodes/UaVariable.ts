import { Attributes, NodeClass } from "opcua-webapi";
import { UaAccessLevel, UaLocalizedText, UaNodeId, UaVariant, UaVariantType, VariableTypeIds } from "opcua-webapi-ts";
import { UaInstanceNode, UaReference, UaReferenceTypes, UaVariableType } from ".";

export class UaVariable extends UaInstanceNode {
    private readonly _typeDefinition: UaVariableType;
    private readonly _dataType: UaNodeId;
    private _valueRank: number;
    private _accessLevel: number;
    private _historizing: boolean;
    private _value: UaVariant;

    constructor(
        nodeId: UaNodeId,
        browseName: string,
        displayName: UaLocalizedText,
        dataType: UaNodeId,
        valueRank: number,
        accessLevel: number,
        typeDefinition: UaVariableType
    ) {
        super(nodeId, browseName, displayName);
        this._typeDefinition = typeDefinition;
        this.addReference(new UaReference(typeDefinition, UaReferenceTypes.HasTypeDefinition, true));
        this._dataType = dataType;
        this._valueRank = valueRank;
        this._accessLevel = accessLevel;
        this._historizing = false;
        this._value = UaVariant.null();
    }

    get nodeClass(): NodeClass {
        return NodeClass.Variable;
    }

    get typeDefinition(): UaVariableType {
        return this._typeDefinition;
    }

    get dataType(): UaNodeId {
        return this._dataType;
    }

    get valueRank(): number {
        return this._valueRank;
    }

    set valueRank(valueRank: number) {
        this._valueRank = valueRank;
    }

    get accessLevel(): number {
        return this._accessLevel;
    }

    set accessLevel(accessLevel: number) {
        this._accessLevel = accessLevel;
    }

    get historizing(): boolean {
        return this._historizing;
    }

    set historizing(historizing: boolean) {
        this._historizing = historizing;
    }

    get value(): UaVariant {
        return this._value;
    }

    set value(value: UaVariant) {
        this._value = value;
    }

    addMember(member: UaVariable)
    {
        this.addMemberNode(member);
    }

    addMemberByName(name: string): UaVariable | null {
        const member = this.typeDefinition.getMember(name);
        if (member === null || member.nodeClass !== NodeClass.Variable) {
            return null;
        }

        const memberVariable = member as UaVariable;
        const newMemberIdValue = `${this.nodeId.value.toString()}-${memberVariable.browseName}`;
        const newVariable = new UaVariable(
            new UaNodeId(newMemberIdValue, this.nodeId.nsIndex),
            memberVariable.browseName,
            memberVariable.displayName,
            memberVariable.dataType,
            memberVariable.valueRank,
            UaAccessLevel.CurrentRead,
            memberVariable.typeDefinition
        );

        this.addMemberNode(newVariable);
        return newVariable;
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
        if (Attributes.AccessLevel === attributeId) {
            return UaVariant.integer(this._accessLevel, UaVariantType.Byte);
        }
        if (Attributes.UserAccessLevel === attributeId) {
            return UaVariant.integer(this._accessLevel, UaVariantType.Byte);
        }
        if (Attributes.MinimumSamplingInterval === attributeId) {
            return UaVariant.double(0);
        }
        if (Attributes.Historizing === attributeId) {
            return UaVariant.boolean(this._historizing);
        }

        return super.getAttribute(attributeId);
    }
}
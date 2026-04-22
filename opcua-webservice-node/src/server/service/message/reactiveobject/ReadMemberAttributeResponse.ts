import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId, UaVariant } from "opcua-webapi-ts";
import { UaInstanceNode } from "../../../addressspace/nodes/UaInstanceNode";
import { UaVariable } from "../../../addressspace/nodes/UaVariable";

export class ReadMemberAttributeResponse {
    private readonly _nodeClass: NodeClass;
    private readonly _browseName: string;
    private readonly _displayName: UaLocalizedText;
    private readonly _description: UaLocalizedText;
    private readonly _dataTypeId: UaNodeId | null;
    private readonly _valueRank: number | null;
    private readonly _accessLevel: number | null;
    private readonly _historizing: boolean | null;
    private readonly _value: UaVariant | null;

    constructor(
        nodeClass: NodeClass,
        browseName: string,
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        dataTypeId: UaNodeId | null,
        valueRank: number | null,
        accessLevel: number | null,
        historizing: boolean | null,
        value: UaVariant | null,
    ) {
        this._nodeClass = nodeClass;
        this._browseName = browseName;
        this._displayName = displayName;
        this._description = description;
        this._dataTypeId = dataTypeId;
        this._valueRank = valueRank;
        this._accessLevel = accessLevel;
        this._historizing = historizing;
        this._value = value;
    }

    get nodeClass(): NodeClass {
        return this._nodeClass;
    }

    get browseName(): string {
        return this._browseName;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get description(): UaLocalizedText {
        return this._description;
    }

    get dataTypeId(): UaNodeId | null {
        return this._dataTypeId;
    }

    get valueRank(): number | null {
        return this._valueRank;
    }

    get accessLevel(): number | null {
        return this._accessLevel;
    }

    get historizing(): boolean | null {
        return this._historizing;
    }

    get value(): UaVariant | null {
        return this._value;
    }

    static fromInstanceDeclaration(instanceDeclaration: UaInstanceNode): ReadMemberAttributeResponse {
        if (instanceDeclaration.nodeClass === NodeClass.Variable) {
            const variable = instanceDeclaration as UaVariable;

            return new ReadMemberAttributeResponse(
                instanceDeclaration.nodeClass,
                instanceDeclaration.browseName,
                instanceDeclaration.displayName,
                instanceDeclaration.description,
                variable.dataType,
                variable.valueRank,
                variable.accessLevel,
                variable.historizing,
                variable.value,
            );
        }

        return new ReadMemberAttributeResponse(
            instanceDeclaration.nodeClass,
            instanceDeclaration.browseName,
            instanceDeclaration.displayName,
            instanceDeclaration.description,
            null,
            null,
            null,
            null,
            null,
        );
    }
}
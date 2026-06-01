import { NodeClass } from "opcua-webapi";
import { UaLocalizedText, UaNodeId } from "opcua-webapi-ts";
import { UaInstanceNode } from "../../../addressspace/nodes/UaInstanceNode";
import { UaVariable } from "../../../addressspace/nodes/UaVariable";

export class ReadChildAttributeResponse {
    private readonly _nodeClass: NodeClass;
    private readonly _displayName: UaLocalizedText;
    private readonly _description: UaLocalizedText;
    private readonly _dataTypeId: UaNodeId | null;
    private readonly _valueRank: number | null;
    private readonly _accessLevel: number | null;
    private readonly _historizing: boolean | null;

    constructor(
    nodeClass: NodeClass,
    displayName: UaLocalizedText,
    description: UaLocalizedText,
    dataTypeId: UaNodeId | null,
    valueRank: number | null,
    accessLevel: number | null,
        historizing: boolean | null,
    ) {
        this._nodeClass = nodeClass;
    this._displayName = displayName;
    this._description = description;
    this._dataTypeId = dataTypeId;
    this._valueRank = valueRank;
    this._accessLevel = accessLevel;
    this._historizing = historizing;
    }

    get nodeClass(): NodeClass {
        return this._nodeClass;
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

    static fromInstanceDeclaration(instanceDeclaration: UaInstanceNode): ReadChildAttributeResponse {
        if (instanceDeclaration.nodeClass === NodeClass.Variable) {
            const variable = instanceDeclaration as UaVariable;

            return new ReadChildAttributeResponse(
                instanceDeclaration.nodeClass,
                instanceDeclaration.displayName,
                instanceDeclaration.description,
                variable.dataType,
                variable.valueRank,
                variable.accessLevel,
                variable.historizing,
            );
        }
        return new ReadChildAttributeResponse(
            instanceDeclaration.nodeClass,
            instanceDeclaration.displayName,
            instanceDeclaration.description,
            null,
            null,
            null,
            null,
        );
    }
}
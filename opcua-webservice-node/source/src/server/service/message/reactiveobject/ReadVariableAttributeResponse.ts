import { UaAccessLevel, UaLocalizedText, UaNodeId, UaValueRank } from "opcua-webapi-ts";
import { UaDataType } from "../../../addressspace/nodes/UaDataType";

export class ReadVariableAttributeResponse {
    private readonly _displayName: UaLocalizedText;
    private readonly _description: UaLocalizedText;
    private readonly _dataTypeId: UaNodeId;
    private readonly _valueRank: number;
    private readonly _accessLevel: number;
    private readonly _historizing: boolean;

    constructor(
        displayName: UaLocalizedText,
        description: UaLocalizedText,
        dataType: UaDataType,
        writable: boolean,
        historizing: boolean,
        valueRank: number | null,
    ) {
        this._displayName = displayName;
        this._description = description;
        this._dataTypeId = dataType.nodeId;
        this._valueRank = valueRank ?? UaValueRank.Scalar;
        this._accessLevel = writable
            ? UaAccessLevel.CurrentWrite | UaAccessLevel.CurrentRead
            : UaAccessLevel.CurrentRead;
        this._historizing = historizing;
    }

    get displayName(): UaLocalizedText {
        return this._displayName;
    }

    get description(): UaLocalizedText {
        return this._description;
    }

    get dataTypeId(): UaNodeId {
        return this._dataTypeId;
    }

    get valueRank(): number {
        return this._valueRank;
    }

    get accessLevel(): number {
        return this._accessLevel;
    }

    get historizing(): boolean {
        return this._historizing;
    }
}
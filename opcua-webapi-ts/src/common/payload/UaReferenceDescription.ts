import { ReferenceDescription } from "opcua-webapi";
import { UaPayloadMapper } from ".";
import { DataTypeIds } from "../nodes";
import { parseUaExpandedNodeIdOrNull, parseUaNodeIdOrNull, UaExpandedNodeId, UaExtensionObject, UaLocalizedText, UaNodeId } from "../types";

export class UaReferenceDescription
{
	static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.ReferenceDescription);

	private _nodeId: UaExpandedNodeId;
	private _nodeClass?: number;
	private _browseName?: string;
	private _displayName?: UaLocalizedText;
	private _referenceTypeId?: UaNodeId;
	private _isForward?: boolean;
	private _typeDefinition?: UaExpandedNodeId;

	constructor(
		nodeId: UaExpandedNodeId,
		nodeClass?: number,
		browseName?: string,
		displayName?: UaLocalizedText,
		referenceTypeId?: UaNodeId,
		isForward?: boolean,
		typeDefinition?: UaExpandedNodeId)
	{
		this._nodeId = nodeId;
		this._nodeClass = nodeClass;
		this._browseName = browseName;
		this._displayName = displayName;
		this._referenceTypeId = referenceTypeId;
		this._isForward = isForward;
		this._typeDefinition = typeDefinition;
	}

	get nodeId() : UaExpandedNodeId
	{
		return this._nodeId;
	}

	get nodeClass() : number | undefined
	{
		return this._nodeClass;
	}

	get browseName() : string | undefined
	{
		return this._browseName;
	}

	get displayName() : UaLocalizedText | undefined
	{
		return this._displayName;
	}

	get referenceTypeId() : UaNodeId | undefined
	{
		return this._referenceTypeId;
	}

	get isForward() : boolean | undefined
	{
		return this._isForward;
	}

	get typeDefinition() : UaExpandedNodeId | undefined
	{
		return this._typeDefinition;
	}

	toStruct() : ReferenceDescription
	{
		let referenceDescription : ReferenceDescription = {
			ReferenceTypeId: (this._referenceTypeId) ? this._referenceTypeId.toString() : undefined,
			IsForward: this._isForward,
			NodeId: this._nodeId.toString(),
			BrowseName: this._browseName,
			DisplayName: (this._displayName) ? UaPayloadMapper.localizedTextToWebApi(this._displayName) : undefined,
			NodeClass: this._nodeClass,
			TypeDefinition: (this._typeDefinition) ? this._typeDefinition.toString() : undefined
		};

		return referenceDescription;
	}

	static fromStruct(referenceDescription : ReferenceDescription) : UaReferenceDescription | null
	{
		let nodeId = parseUaExpandedNodeIdOrNull(referenceDescription.NodeId); 
		let displayName = (referenceDescription.DisplayName) ? UaPayloadMapper.localizedTextFromWebApi(referenceDescription.DisplayName) : undefined;
		let referenceTypeId = (referenceDescription.ReferenceTypeId) ? parseUaNodeIdOrNull(referenceDescription.ReferenceTypeId) : undefined;
		let typeDefinition = (referenceDescription.TypeDefinition) ? parseUaExpandedNodeIdOrNull(referenceDescription.TypeDefinition) : undefined;

        if (null == nodeId || null == displayName || null == referenceTypeId || null == typeDefinition) return null;

		return new UaReferenceDescription(
            nodeId, 
            referenceDescription.NodeClass, 
            referenceDescription.BrowseName, 
            displayName, 
            referenceTypeId, 
            referenceDescription.IsForward, 
            typeDefinition);
	}
}

import { BrowseDescription, BrowseDirection } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { parseUaNodeIdOrNull, UaNodeId } from "../types";

export class UaBrowseDescription
{
	static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.BrowseDescription);

	private _nodeId: UaNodeId;
	private _browseDirection: number;
	private _referenceTypeId: UaNodeId;
	private _includeSubtypes: boolean;
	private _nodeClassMask: number;
	private _resultMask: number;

	constructor(
		nodeId: UaNodeId,
		browseDirection: number,
		referenceTypeId: UaNodeId,
		includeSubtypes: boolean,
		nodeClassMask: number,
		resultMask: number)
	{   
		this._nodeId = nodeId;
		this._browseDirection = browseDirection;
		this._referenceTypeId = referenceTypeId;
		this._includeSubtypes = includeSubtypes;
		this._nodeClassMask = nodeClassMask;
		this._resultMask = resultMask;
	}

	get nodeId() : UaNodeId
	{
		return this._nodeId;
	}

	get browseDirection() : number
	{
		return this._browseDirection;
	}

	get referenceTypeId() : UaNodeId
	{
		return this._referenceTypeId;
	}

	get includeSubtypes() : boolean
	{
		return this._includeSubtypes;
	}

	get nodeClassMask() : number
	{
		return this._nodeClassMask;
	}

	get resultMask() : number
	{
		return this._resultMask;
	}

	toStruct() : BrowseDescription
	{
		let browseDescription : BrowseDescription = {
			NodeId: this._nodeId.toString(),
			BrowseDirection: this._browseDirection,
			ReferenceTypeId: this._referenceTypeId.toString(),
			IncludeSubtypes: this._includeSubtypes,
			NodeClassMask: this._nodeClassMask,
			ResultMask: this._resultMask
		};

		return browseDescription;
	}

	static fromStruct(browseDescription : BrowseDescription) : UaBrowseDescription | null
	{
		let nodeId = parseUaNodeIdOrNull(browseDescription.NodeId);
        let referenceTypeId = parseUaNodeIdOrNull(browseDescription.ReferenceTypeId);
		if (null == nodeId || null == referenceTypeId) return null;

		let browseDirection = (typeof browseDescription.BrowseDirection === "number") ? browseDescription.BrowseDirection : BrowseDirection.Invalid;
	    let includeSubtypes = (browseDescription.IncludeSubtypes) ? true : false;
		let nodeClassMask = (browseDescription.NodeClassMask) ? browseDescription.NodeClassMask : 0;
		let resultMask = (browseDescription.ResultMask) ? browseDescription.ResultMask : 0;

		return new UaBrowseDescription(nodeId, browseDirection, referenceTypeId, includeSubtypes, nodeClassMask, resultMask);
	}
}

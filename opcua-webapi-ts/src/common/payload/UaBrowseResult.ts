import { BrowseResult, ReferenceDescription, StatusCodes } from "opcua-webapi";
import { DataTypeIds } from "../nodes";
import { makeUaStatusCode, UaNodeId, UaStatusCode } from "../types";
import { UaPayloadMapper,UaReferenceDescription } from ".";

export class UaBrowseResult
{
	static dataTypeId : UaNodeId = new UaNodeId(DataTypeIds.BrowseResult);

	private _references: Array<UaReferenceDescription>;
	private _continuationPoint: string | null;
	private _statusCode: UaStatusCode;

	constructor(
		references: Array<UaReferenceDescription>,
		continuationPoint: string | null,
		statusCode: UaStatusCode)
	{
		this._references = references;
		this._continuationPoint = continuationPoint;
		this._statusCode = statusCode;
	}

	get references() : Array<UaReferenceDescription>
	{
		return this._references;
	}

	get results() : Array<UaReferenceDescription>
	{
		return this._references;
	}

	get continuationPoint() : string | null
	{
		return this._continuationPoint;
	}

	get statusCode() : UaStatusCode
	{
		return this._statusCode;
	}

	toStruct() : BrowseResult
	{
		let references: Array<ReferenceDescription> = [];

		for (let item of this._references)
		{
			references.push(item.toStruct());
		}

		let browseResult : BrowseResult = {
			StatusCode: UaPayloadMapper.statusCodeToWebApi(this._statusCode),
			ContinuationPoint: (this._continuationPoint) ? this._continuationPoint : undefined,
			References: references
		};

		return browseResult;
	}

	static fromStruct(browseResult : BrowseResult) : UaBrowseResult | null
	{
		let references: Array<UaReferenceDescription> = [];

		if (browseResult.References)
		{
			for (let item of browseResult.References)
			{
				let reference = UaReferenceDescription.fromStruct(item);
				if (null == reference) return null;
				references.push(reference);
			}
		}

		return new UaBrowseResult(
			references,
			(browseResult.ContinuationPoint) ? browseResult.ContinuationPoint : null,
			(browseResult.StatusCode) ? makeUaStatusCode(browseResult.StatusCode.Code) : makeUaStatusCode(StatusCodes.Good)
		);
	}
}

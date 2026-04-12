import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, ReferenceTypeIds, UaBrowseDescription, UaBrowseResult, UaError, UaExpandedNodeId, UaLocalizedText, UaNodeId, UaReferenceDescription, UaStatusCode } from "opcua-webapi-ts";
import { UaTransaction } from "./UaTransaction";
import { ServiceContext } from "../../..";

import { UaBrowseAdditionalInfo } from "../../../types";

export class UaBrowseTransaction extends UaTransaction<UaBrowseDescription, UaBrowseResult> {
    protected readonly _nodeToBrowse: UaBrowseDescription;
    protected _additionalInfo: UaBrowseAdditionalInfo;

    protected _statusCode: UaStatusCode;
    protected _continuationPoint: string | null;
    protected _references: Array<UaReferenceDescription>;

    constructor(
        serviceContext: ServiceContext,
        nodeToBrowse: UaBrowseDescription,
        additionalInfo: UaBrowseAdditionalInfo,
        handleId: number,
    ) {
        super(serviceContext, handleId);
        this._nodeToBrowse = nodeToBrowse;
        this._additionalInfo = additionalInfo;
        this._statusCode = makeUaStatusCode(StatusCodes.Good);
        this._continuationPoint = null;
        this._references = [];
    }

    setStatusCode(statusCode: UaStatusCode): void {
        this._statusCode = statusCode;
    }

    addTypeDefinitionReference(typeId: UaNodeId, browseName: string, displayName: UaLocalizedText): void {
        this._references.push(
            new UaReferenceDescription(
                UaExpandedNodeId.from(typeId),
                NodeClass.ObjectType,
                browseName,
                displayName,                
                UaNodeId.from(ReferenceTypeIds.HasTypeDefinition),
                true,
                undefined,
            ),
        );
    }

    getItem(): UaBrowseDescription {
        return this._nodeToBrowse;
    }

    getResult(): UaBrowseResult {
        let result = new UaBrowseResult(
            this._references,
            (this._continuationPoint && this._continuationPoint.length > 0) ? this._continuationPoint : null,            
            this._statusCode,
        );
        
        return result;
    }

    async execute(): Promise<void> {
        return;
    }

    protected buildErrorResults(error: unknown): void {
        this._references = [];
        this._continuationPoint = null;

        this._statusCode = makeUaStatusCode(StatusCodes.BadUnexpectedError);
        if (error instanceof UaError) {
            this._statusCode = error.statusCode;
        }
    }
}
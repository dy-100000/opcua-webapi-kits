import { makeUaStatusCode, UaError, UaNodeId } from "opcua-webapi-ts";
import { AddReferencesContext, DeleteReferencesContext, NodeManagerReactiveObject, UaReferenceType } from "../../..";
import { UaInstanceIdentifier, UaObjectId } from "../../../types";
import { AddReferenceRequest, DeleteReferenceRequest } from "../../message";
import { UaDeleteReferenceTransaction } from "../base";
import { NodeClass, StatusCodes } from "opcua-webapi";

export class UaDeleteObjectReferenceTransaction extends UaDeleteReferenceTransaction {
    private readonly sourceIdentifier: UaInstanceIdentifier;
    private readonly targetIdentifier: UaInstanceIdentifier;
    private readonly referenceTypeId: UaNodeId;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        deleteReferencesContext: DeleteReferencesContext,
        handleId: number,
        sourceIdentifier: UaInstanceIdentifier,
        targetIdentifier: UaInstanceIdentifier,
        referenceTypeId: UaNodeId,
        nodeManager: NodeManagerReactiveObject)
    {        
        super(deleteReferencesContext, handleId);
        this.sourceIdentifier = sourceIdentifier;
        this.targetIdentifier = targetIdentifier;
        this.referenceTypeId = referenceTypeId;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const sourceObjectType = this.nodeManager.findObjectType(this.sourceIdentifier.objectId);
            const sourceInstanceDeclaration = this.nodeManager.findInstanceDeclaration(this.sourceIdentifier.objectId);            
            const targetObjectType = this.nodeManager.findObjectType(this.targetIdentifier.objectId);
            const targetInstanceDeclaration = this.nodeManager.findInstanceDeclaration(this.targetIdentifier.objectId);
            const referenceType = this.nodeManager.getNode(this.referenceTypeId);

            if (sourceObjectType === null || targetObjectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            if (referenceType === null || referenceType.nodeClass !== NodeClass.ReferenceType) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadReferenceTypeIdInvalid));
            }

            const request = new DeleteReferenceRequest(
                new UaObjectId(this.sourceIdentifier.objectId.id, sourceInstanceDeclaration),
                new UaObjectId(this.targetIdentifier.objectId.id, targetInstanceDeclaration),
                targetObjectType,
                referenceType as UaReferenceType);

            let response =await sourceObjectType.onDeleteReference(request);
            this._statusCode = response.statusCode;
        } catch (err) {
            if (err instanceof UaError) {
                this._statusCode = err.statusCode;
            } else {
                this._statusCode = makeUaStatusCode(StatusCodes.BadInternalError);
            }
        }
    }
}
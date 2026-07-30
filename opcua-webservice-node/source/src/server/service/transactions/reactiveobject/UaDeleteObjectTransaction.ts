import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError } from "opcua-webapi-ts";
import { DeleteNodesContext, UaObjectId, UaReactiveObjectType } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaInstanceIdentifier } from "../../../types";
import { DeleteObjectRequest } from "../../message";
import { UaDeleteNodeTransaction } from "../base";

export class UaDeleteObjectTransaction extends UaDeleteNodeTransaction {
    private readonly objectIdentifier: UaInstanceIdentifier;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        deleteNodesContext: DeleteNodesContext,
        handleId: number,
        objectIdentifier: UaInstanceIdentifier, 
        nodeManager: NodeManagerReactiveObject) {
        super(deleteNodesContext, handleId);
        this.objectIdentifier = objectIdentifier;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectIdentifier.objectId);
            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectIdentifier.objectId);
            if (objectType === null ||
                objectType.nodeClass !== NodeClass.ObjectType) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            let objectId = new UaObjectId(this.objectIdentifier.objectId.id,instanceDeclaration);
            const request = new DeleteObjectRequest(objectId);
            const response = await (objectType as UaReactiveObjectType).onDeleteObject(request);
            this._statusCode = response.statusCode;            
        } catch (error) {
            this._statusCode = makeUaStatusCode(StatusCodes.BadUnexpectedError);

            if (error instanceof UaError) {
                this._statusCode = error.statusCode;
            }
        }
    }
    
}
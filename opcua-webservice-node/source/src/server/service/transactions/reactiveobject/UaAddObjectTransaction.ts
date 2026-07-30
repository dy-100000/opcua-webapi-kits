import { NodeClass, StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError, UaLocalizedText, UaNodeId, UaNodeIdType } from "opcua-webapi-ts";
import { AddNodesContext, UaReactiveObjectType } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaInstanceIdentifier, UaObjectId, UaObjectIdentifier } from "../../../types";
import { AddObjectRequest } from "../../message";
import { UaAddNodeTransaction } from "../base";

export class UaAddObjectTransaction extends UaAddNodeTransaction {
    private readonly parentIdentifier: UaInstanceIdentifier;
    private readonly objectTypeId: UaNodeId;
    private readonly browseName: string | null;
    private readonly displayName: UaLocalizedText | null;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        addNodesContext: AddNodesContext,
        handleId: number,
        parentIdentifier: UaInstanceIdentifier,
        objectTypeId: UaNodeId, 
        browseName: string | null,
        displayName: UaLocalizedText | null,
        nodeManager: NodeManagerReactiveObject)
    {
        super(addNodesContext, handleId);
        this.parentIdentifier = parentIdentifier;
        this.objectTypeId = objectTypeId;
        this.browseName = browseName;
        this.displayName = displayName;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const parentObjectType = this.nodeManager.findObjectType(this.parentIdentifier.objectId);
            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.parentIdentifier.objectId);
            const objectTypeToAdd = this.nodeManager.getNode(this.objectTypeId);

            if (parentObjectType === null || objectTypeToAdd === null ||
                objectTypeToAdd.nodeClass !== NodeClass.ObjectType) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            const request = new AddObjectRequest(
                new UaObjectId(this.parentIdentifier.objectId.id, instanceDeclaration),
                this.browseName,
                this.displayName);

            const response = await (objectTypeToAdd as UaReactiveObjectType).onAddObject(request);
            
            let objectIdentifier = new UaObjectIdentifier(
                this.objectTypeId.toString(), 
                response.id, 
                null);

            let instanceIdentifier = new UaInstanceIdentifier(objectIdentifier, null);
            this._addedNodeId = new UaNodeId(
                instanceIdentifier.toByteString(),
                this.nodeManager.nsIndex(),
                UaNodeIdType.BYTESTRING);   
        } catch (error) {
            this._statusCode = makeUaStatusCode(StatusCodes.BadUnexpectedError);

            if (error instanceof UaError) {
                this._statusCode = error.statusCode;
            }
        }
    }
}
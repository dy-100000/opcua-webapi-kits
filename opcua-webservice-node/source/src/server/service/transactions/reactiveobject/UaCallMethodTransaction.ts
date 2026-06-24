import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError } from "opcua-webapi-ts";
import { CallContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaInstanceIdentifier, UaObjectId } from "../../../types";
import { MethodCallRequest } from "../../message";
import { UaMethodCallTransaction } from "../base/UaMethodCallTransaction";

export class UaCallMethodTransaction extends UaMethodCallTransaction {
    private readonly nodeManager: NodeManagerReactiveObject;
    private readonly objectIdentifier: UaInstanceIdentifier;
    private readonly methodIdentifier: UaInstanceIdentifier;

    constructor(
        callContext: CallContext,
        index: number,
        objectIdentifier: UaInstanceIdentifier,
        methodIdentifier: UaInstanceIdentifier,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(callContext, index);
        this.objectIdentifier = objectIdentifier;
        this.methodIdentifier = methodIdentifier;
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectIdentifier.objectId);
            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectIdentifier.objectId);
            const methodIdentifier = this.methodIdentifier.childId;

            if (objectType === null || methodIdentifier === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            const callRequest = this.getItem();
            const inputArguments = callRequest.inputArguments ?? [];
            const request = new MethodCallRequest(
                new UaObjectId(this.objectIdentifier.objectId.id, instanceDeclaration),
                methodIdentifier.path,
                inputArguments,
            );

            const response = await objectType.onMethodCall(request);
            this._outputArguments.push(...response.outputArguments);
        } catch (error) {
            this._outputArguments.length = 0;
            this._statusCode = makeUaStatusCode(StatusCodes.BadUnexpectedError);

            if (error instanceof UaError) {
                this._statusCode = error.statusCode;
            }
        }
    }
}
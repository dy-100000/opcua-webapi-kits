import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaError, UaVariant } from "opcua-webapi-ts";
import { WriteContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaObjectId, UaObjectIdentifier, WriteVariableValue } from "../../../types";
import { WriteVariableValueRequest, WriteVariableValueResponse } from "../../message";
import { UaWriteTransaction } from "../base/UaWriteTransaction";

export class UaWriteVariableValueTransaction extends UaWriteTransaction {
    private readonly objectId: UaObjectIdentifier;
    private readonly variableValues: Array<WriteVariableValue>;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        context: WriteContext,
        objectId: UaObjectIdentifier,
        handleIdsAndVariableValues: Map<number, WriteVariableValue>,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, [...handleIdsAndVariableValues.keys()]);
        this.objectId = objectId;
        this.variableValues = [...handleIdsAndVariableValues.values()];
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectId);
            if (objectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectId);
            const valuesToWrite = new Map<string, UaVariant>();

            for (const item of this.variableValues) {
                if (item.value.statusCode.isNotGood()) continue;
                valuesToWrite.set(item.variableId.toString(), item.value.value);
            }

            const request = new WriteVariableValueRequest(
                new UaObjectId(this.objectId.id, instanceDeclaration),
                valuesToWrite,
            );

            const response = await objectType.onWriteVariablesValue(request);
            this.setResults(response);
        } catch (error) {
            this._results.length = 0;

            const statusCode = error instanceof UaError
                ? error.statusCode
                : makeUaStatusCode(StatusCodes.BadUnexpectedError);

            for (const _handleId of this.handleIds) {
                this._results.push(statusCode);
            }
        }
    }

    private setResults(response: WriteVariableValueResponse): void {
        this._results.length = 0;

        for (const item of this.variableValues) {
            const result = response.results.get(item.variableId.toString());
            this._results.push(result ?? makeUaStatusCode(StatusCodes.BadNotWritable));
        }
    }
}
import { StatusCodes } from "opcua-webapi";
import { UaError, UaStatusCode, UaVariant } from "opcua-webapi-ts";
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
                throw new UaError(UaStatusCode.from(StatusCodes.BadNodeIdUnknown));
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
            this.buildErrorResults(error);
        }
    }

    private setResults(response: WriteVariableValueResponse): void {
        for (const item of this.variableValues) {
            const result = response.results.get(item.variableId.toString());
            this._results.push(result ?? UaStatusCode.from(StatusCodes.Good));
        }
    }
}
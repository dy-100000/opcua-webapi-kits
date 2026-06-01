import { StatusCodes, TimestampsToReturn } from "opcua-webapi";
import { makeUaStatusCode, UaDataValue, UaError, UaVariant } from "opcua-webapi-ts";
import { ReadContext } from "../../..";
import { NodeManagerReactiveObject } from "../../../addressspace/nodemanager/NodeManagerReactiveObject";
import { UaChildId, UaObjectId, UaObjectIdentifier } from "../../../types";
import { ReadVariableValueRequest, ReadVariableValueResponse } from "../../message";
import { UaReadTransaction } from "../base/UaReadTransaction";

export class UaReadVariableValueTransaction extends UaReadTransaction {
    private readonly objectId: UaObjectIdentifier;
    private readonly childIds: Array<UaChildId>;
    private readonly nodeManager: NodeManagerReactiveObject;

    constructor(
        context: ReadContext,
        objectId: UaObjectIdentifier,
        handleIdsAndChildIds: Map<number, UaChildId>,
        nodeManager: NodeManagerReactiveObject,
    ) {
        super(context, [...handleIdsAndChildIds.keys()]);
        this.objectId = objectId;
        this.childIds = [...handleIdsAndChildIds.values()];
        this.nodeManager = nodeManager;
    }

    override async execute(): Promise<void> {
        try {
            const objectType = this.nodeManager.findObjectType(this.objectId);
            if (objectType === null) {
                throw new UaError(makeUaStatusCode(StatusCodes.BadNodeIdUnknown));
            }

            const variableIds = new Set(this.childIds.map((childId) => childId.toString()));
            const instanceDeclaration = this.nodeManager.findInstanceDeclaration(this.objectId);
            const request = new ReadVariableValueRequest(
                new UaObjectId(this.objectId.id, instanceDeclaration),
                variableIds,
            );

            const response = await objectType.onReadVariablesValue(request);
            this.setResults(response);
        } catch (error) {
            this.buildErrorResults(error);
        }
    }

    private setResults(response: ReadVariableValueResponse): void {
        let sourceTimestamp: Date | undefined;
        let serverTimestamp: Date | undefined;
        const now = new Date();

        if (this.timestampsToReturn === TimestampsToReturn.Source) {
            sourceTimestamp = now;
        } else if (this.timestampsToReturn === TimestampsToReturn.Server) {
            serverTimestamp = now;
        } else if (this.timestampsToReturn === TimestampsToReturn.Both) {
            sourceTimestamp = now;
            serverTimestamp = now;
        }

        this._results = [];

        for (const item of this.childIds) {
            const result = response.results.get(item.toString());

            if (result !== undefined) {
                const hasBadStatus = result.statusCode.isBad();
                this._results.push(new UaDataValue(
                    result.value,
                    result.statusCode,
                    hasBadStatus ? undefined : sourceTimestamp,
                    hasBadStatus ? undefined : serverTimestamp,
                ));
            } else {
                this._results.push(
                    new UaDataValue(
                        UaVariant.null(),
                        makeUaStatusCode(StatusCodes.BadNotReadable),
                    ),
                );
            }
        }
    }
}
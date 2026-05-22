import { StatusCodes } from "opcua-webapi";
import { makeUaStatusCode, UaError, UaNodeId } from "opcua-webapi-ts";
import { UaTransaction2 } from "./UaTransaction2";

export class UaTransactionManager2<TRequest, TResponse> {
    private readonly transactions: Array<UaTransaction2<TRequest, TResponse>>;

    constructor() {
        this.transactions = [];
    }

    addTransaction(transaction: UaTransaction2<TRequest, TResponse>): void {
        this.transactions.push(transaction);
    }

    getMergedResults(): Array<TResponse> {
        const results = new Map<number, TResponse>();
        const mergedResults: Array<TResponse> = [];

        for (const item of this.transactions) {
            if (item.getResults().length !== item.handleIds.length) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));

            for (let index = 0; index < item.handleIds.length; index++) {
                results.set(item.handleIds[index], item.getResults()[index]);
            }
        }

        for (let index = 0; index < results.size; index++) {
            if (!results.has(index)) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));
            mergedResults.push(results.get(index) as TResponse);
        }

        return mergedResults;
    }

    async execute(): Promise<void> {
        for (let transaction of this.transactions) {
            await transaction.execute();
        }
    }

    static getNsIndexes(nodeIds: Array<UaNodeId>): Array<number> {
        const nsIndexes = new Set<number>();

        for (const item of nodeIds) {
            nsIndexes.add(item.nsIndex);
        }

        return Array.from(nsIndexes);
    }

    static getHandleIds(nodeIds: Array<UaNodeId>, nsIndex: number): Array<number> {
        const ret: Array<number> = [];

        for (let index = 0; index < nodeIds.length; index++) {
            if (nodeIds[index].nsIndex === nsIndex) ret.push(index);
        }

        return ret;
    }
}
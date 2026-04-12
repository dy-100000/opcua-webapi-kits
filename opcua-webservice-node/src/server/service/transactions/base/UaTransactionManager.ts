import { makeUaStatusCode, UaError } from "opcua-webapi-ts";
import { UaTransaction } from "./UaTransaction";
import { StatusCodes } from "opcua-webapi";

export class UaTransactionManager<TRequest, TResponse> {
    private readonly transactions: Array<UaTransaction<TRequest, TResponse>>;
    private currentTransactionIndex: number;

    constructor() {
        this.transactions = [];
        this.currentTransactionIndex = 0;
    }

    addTransaction(transaction: UaTransaction<TRequest, TResponse>): void {
        this.transactions.push(transaction);
    }

    getMergedResults(): Array<TResponse> {
        const results = new Map<number, TResponse>();
        let ret : Array<TResponse> = [];

        for (const item of this.transactions) {
            results.set(item.handleId, item.getResult());
        }

        for (let i = 0; i < results.size; i++) {            
            if (!results.has(i)) throw new UaError(makeUaStatusCode(StatusCodes.BadUnexpectedError));
            ret.push(results.get(i) as TResponse);
        }

        return ret;
    }

    async execute(): Promise<void> {
        if (this.currentTransactionIndex >= this.transactions.length) return;
        await this.transactions[this.currentTransactionIndex].execute();
        this.currentTransactionIndex += 1;
        await this.execute();
    }
}
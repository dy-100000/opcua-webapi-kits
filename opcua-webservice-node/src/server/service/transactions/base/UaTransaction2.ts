import { ServiceContext } from "../../../types/contexts";

export abstract class UaTransaction2<TRequest, TResponse> {
    protected readonly _serviceContext: ServiceContext;
    private readonly _handleIds: Array<number>;

    constructor(serviceContext: ServiceContext, handleIds: Array<number>) {
        this._serviceContext = serviceContext;
        this._handleIds = handleIds;
    }

    get serviceContext(): ServiceContext {
        return this._serviceContext;
    }

    get handleIds(): Array<number> {
        return this._handleIds;
    }

    getRequestedItems(): Array<TRequest> {
        const allItems = this.getItems();
        let requestedItems: Array<TRequest> = [];

        for (const handleId of this._handleIds) {
            if (handleId < 0 || handleId >= allItems.length) continue;
            requestedItems.push(allItems[handleId]);
        }

        return requestedItems;
    }

    abstract getItems(): Array<TRequest>;

    abstract getResults(): TResponse[];

    abstract execute(): Promise<void>;
}
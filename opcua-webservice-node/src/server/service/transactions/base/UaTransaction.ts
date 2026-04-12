import { ServiceContext } from "../../..";

export abstract class UaTransaction<TRequest, TResponse> {
    protected readonly _serviceContext: ServiceContext;
    private readonly _handleId: number;

    constructor(serviceContext: ServiceContext, handleId: number) {
        this._serviceContext = serviceContext;
        this._handleId = handleId;
    }

    get serviceContext(): ServiceContext {
        return this.serviceContext;
    }

    get handleId(): number {
        return this._handleId;
    }

    abstract getItem(): TRequest;

    abstract getResult(): TResponse;

    abstract execute(): Promise<void>;
}
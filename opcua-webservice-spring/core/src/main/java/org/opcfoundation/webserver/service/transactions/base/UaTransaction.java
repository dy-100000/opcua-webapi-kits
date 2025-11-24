package org.opcfoundation.webserver.service.transactions.base;

import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.concurrent.CompletableFuture;

public abstract class UaTransaction<T_Request, T_Response> {
    protected final ServiceContext serviceContext;
    private final int handleId;

    public UaTransaction(
            ServiceContext serviceContext,
            int handleId)
    {
        this.serviceContext = serviceContext;
        this.handleId = handleId;
    }

    public ServiceContext getServiceContext() {
        return serviceContext;
    }

    public Integer getHandleId() {
        return handleId;
    }

    public abstract T_Request getItem();

    public abstract T_Response getResult();

    public abstract CompletableFuture<Void> execute();
}

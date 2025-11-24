package org.opcfoundation.webserver.service.transactions.base;

import org.opcfoundation.webapi.service.types.ServiceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class UaTransaction2<T_Request, T_Response> {
    protected final ServiceContext serviceContext;
    private final List<Integer> handleIds;

    public UaTransaction2(
            ServiceContext serviceContext,
            List<Integer> handleIds)
    {
        this.serviceContext = serviceContext;
        this.handleIds = handleIds;
    }

    public ServiceContext getServiceContext() {
        return serviceContext;
    }

    public List<Integer> getHandleIds() {
        return handleIds;
    }

    public List<T_Request> getRequestedItems()
    {
        List<T_Request> items = new ArrayList<>();
        List<T_Request> allItems = getItems();

        for (Integer item : handleIds)
        {
            items.add(allItems.get(item));
        }

        return items;
    }

    public abstract List<T_Request> getItems();

    public abstract List<T_Response> getResults();

    public abstract CompletableFuture<Void> execute();
}

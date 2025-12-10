package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetDigitalTwinListResponse;

import java.util.concurrent.CompletableFuture;

public interface DigitalTwinDirectoryCallback {
    CompletableFuture<GetDigitalTwinListResponse> onGetDigitalTwinList(GetDigitalTwinListRequest request);
}

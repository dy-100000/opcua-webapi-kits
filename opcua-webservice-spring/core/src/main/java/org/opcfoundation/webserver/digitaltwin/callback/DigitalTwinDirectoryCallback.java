package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.types.message.digitaltwin.GetDigitalTwinListRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetDigitalTwinListResponse;

import java.util.concurrent.CompletableFuture;

public interface DigitalTwinDirectoryCallback {
    CompletableFuture<GetDigitalTwinListResponse> onGetDigitalTwinList(GetDigitalTwinListRequest request);
}

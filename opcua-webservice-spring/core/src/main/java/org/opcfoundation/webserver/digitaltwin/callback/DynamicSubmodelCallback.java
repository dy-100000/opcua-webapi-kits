package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.service.message.digitaltwin.GetObjectElementListResponse;

import java.util.concurrent.CompletableFuture;

public interface DynamicSubmodelCallback {
    CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request);
}

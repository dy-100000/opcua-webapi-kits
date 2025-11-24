package org.opcfoundation.webserver.digitaltwin.callback;

import org.opcfoundation.webserver.types.message.digitaltwin.GetObjectElementListRequest;
import org.opcfoundation.webserver.types.message.digitaltwin.GetObjectElementListResponse;

import java.util.concurrent.CompletableFuture;

public interface ElementListSubmodelCallback {
    CompletableFuture<GetObjectElementListResponse> onGetObjectElementList(GetObjectElementListRequest request);
}

package org.opcfoundation.webapi.serverapi;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
@Controller
@RequestMapping("${openapi.oPCUAWeb.base-path:}")
public class CreatemonitoreditemsApiController implements CreatemonitoreditemsApi {

    private final NativeWebRequest request;

    @Autowired
    public CreatemonitoreditemsApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

/*    public CompletableFuture<ResponseEntity<CreateMonitoredItemsResponse>> createMonitoredItems(
            String serverUri,
            CreateMonitoredItemsRequest createMonitoredItemsRequest)*/
}

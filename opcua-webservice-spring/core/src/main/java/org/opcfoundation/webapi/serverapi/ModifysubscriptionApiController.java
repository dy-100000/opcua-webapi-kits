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
public class ModifysubscriptionApiController implements ModifysubscriptionApi {

    private final NativeWebRequest request;

    @Autowired
    public ModifysubscriptionApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

/*    @Override
    public CompletableFuture<ResponseEntity<ModifySubscriptionResponse>> modifySubscription(
            String serverUri,
            ModifySubscriptionRequest modifySubscriptionRequest)*/
}

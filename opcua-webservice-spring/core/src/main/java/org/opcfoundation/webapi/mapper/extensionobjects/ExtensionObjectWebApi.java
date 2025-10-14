package org.opcfoundation.webapi.mapper.extensionobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public interface ExtensionObjectWebApi {
    @Schema(name = "UaTypeId", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("UaTypeId")
    public String getUaTypeId();
}

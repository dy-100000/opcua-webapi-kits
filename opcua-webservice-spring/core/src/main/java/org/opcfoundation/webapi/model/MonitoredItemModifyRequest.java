package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.3/#5.13.3.2).
 */

@Schema(name = "MonitoredItemModifyRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.3/#5.13.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class MonitoredItemModifyRequest {

  private Long monitoredItemId = 0l;

  private @Nullable MonitoringParameters requestedParameters;

  public MonitoredItemModifyRequest monitoredItemId(Long monitoredItemId) {
    this.monitoredItemId = monitoredItemId;
    return this;
  }

  /**
   * Get monitoredItemId
   * minimum: 0
   * maximum: 4294967295
   * @return monitoredItemId
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MonitoredItemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MonitoredItemId")
  public Long getMonitoredItemId() {
    return monitoredItemId;
  }

  public void setMonitoredItemId(Long monitoredItemId) {
    this.monitoredItemId = monitoredItemId;
  }

  public MonitoredItemModifyRequest requestedParameters(MonitoringParameters requestedParameters) {
    this.requestedParameters = requestedParameters;
    return this;
  }

  /**
   * Get requestedParameters
   * @return requestedParameters
   */
  @Valid 
  @Schema(name = "RequestedParameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedParameters")
  public MonitoringParameters getRequestedParameters() {
    return requestedParameters;
  }

  public void setRequestedParameters(MonitoringParameters requestedParameters) {
    this.requestedParameters = requestedParameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonitoredItemModifyRequest monitoredItemModifyRequest = (MonitoredItemModifyRequest) o;
    return Objects.equals(this.monitoredItemId, monitoredItemModifyRequest.monitoredItemId) &&
        Objects.equals(this.requestedParameters, monitoredItemModifyRequest.requestedParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monitoredItemId, requestedParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoredItemModifyRequest {\n");
    sb.append("    monitoredItemId: ").append(toIndentedString(monitoredItemId)).append("\n");
    sb.append("    requestedParameters: ").append(toIndentedString(requestedParameters)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


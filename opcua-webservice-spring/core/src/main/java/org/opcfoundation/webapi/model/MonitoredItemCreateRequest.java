package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2).
 */

@Schema(name = "MonitoredItemCreateRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class MonitoredItemCreateRequest {

  private @Nullable ReadValueId itemToMonitor;

  private @Nullable Integer monitoringMode;

  private @Nullable MonitoringParameters requestedParameters;

  public MonitoredItemCreateRequest itemToMonitor(ReadValueId itemToMonitor) {
    this.itemToMonitor = itemToMonitor;
    return this;
  }

  /**
   * Get itemToMonitor
   * @return itemToMonitor
   */
  @Valid 
  @Schema(name = "ItemToMonitor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ItemToMonitor")
  public ReadValueId getItemToMonitor() {
    return itemToMonitor;
  }

  public void setItemToMonitor(ReadValueId itemToMonitor) {
    this.itemToMonitor = itemToMonitor;
  }

  public MonitoredItemCreateRequest monitoringMode(Integer monitoringMode) {
    this.monitoringMode = monitoringMode;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.23).
   * @return monitoringMode
   */
  
  @Schema(name = "MonitoringMode", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.23).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MonitoringMode")
  public Integer getMonitoringMode() {
    return monitoringMode;
  }

  public void setMonitoringMode(Integer monitoringMode) {
    this.monitoringMode = monitoringMode;
  }

  public MonitoredItemCreateRequest requestedParameters(MonitoringParameters requestedParameters) {
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
    MonitoredItemCreateRequest monitoredItemCreateRequest = (MonitoredItemCreateRequest) o;
    return Objects.equals(this.itemToMonitor, monitoredItemCreateRequest.itemToMonitor) &&
        Objects.equals(this.monitoringMode, monitoredItemCreateRequest.monitoringMode) &&
        Objects.equals(this.requestedParameters, monitoredItemCreateRequest.requestedParameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemToMonitor, monitoringMode, requestedParameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoredItemCreateRequest {\n");
    sb.append("    itemToMonitor: ").append(toIndentedString(itemToMonitor)).append("\n");
    sb.append("    monitoringMode: ").append(toIndentedString(monitoringMode)).append("\n");
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


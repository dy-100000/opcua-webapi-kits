package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.4/#5.13.4.2).
 */

@Schema(name = "SetMonitoringModeRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.4/#5.13.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SetMonitoringModeRequest {

  private @Nullable RequestHeader requestHeader;

  private Long subscriptionId = 0l;

  private @Nullable Integer monitoringMode;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> monitoredItemIds = new ArrayList<>();

  public SetMonitoringModeRequest requestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
    return this;
  }

  /**
   * Get requestHeader
   * @return requestHeader
   */
  @Valid 
  @Schema(name = "RequestHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHeader")
  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public void setRequestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
  }

  public SetMonitoringModeRequest subscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * Get subscriptionId
   * minimum: 0
   * maximum: 4294967295
   * @return subscriptionId
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "SubscriptionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscriptionId")
  public Long getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public SetMonitoringModeRequest monitoringMode(Integer monitoringMode) {
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

  public SetMonitoringModeRequest monitoredItemIds(List<@Min(0L) @Max(4294967295L)Long> monitoredItemIds) {
    this.monitoredItemIds = monitoredItemIds;
    return this;
  }

  public SetMonitoringModeRequest addMonitoredItemIdsItem(Long monitoredItemIdsItem) {
    if (this.monitoredItemIds == null) {
      this.monitoredItemIds = new ArrayList<>();
    }
    this.monitoredItemIds.add(monitoredItemIdsItem);
    return this;
  }

  /**
   * Get monitoredItemIds
   * @return monitoredItemIds
   */
  
  @Schema(name = "MonitoredItemIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MonitoredItemIds")
  public List<@Min(0L) @Max(4294967295L)Long> getMonitoredItemIds() {
    return monitoredItemIds;
  }

  public void setMonitoredItemIds(List<@Min(0L) @Max(4294967295L)Long> monitoredItemIds) {
    this.monitoredItemIds = monitoredItemIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetMonitoringModeRequest setMonitoringModeRequest = (SetMonitoringModeRequest) o;
    return Objects.equals(this.requestHeader, setMonitoringModeRequest.requestHeader) &&
        Objects.equals(this.subscriptionId, setMonitoringModeRequest.subscriptionId) &&
        Objects.equals(this.monitoringMode, setMonitoringModeRequest.monitoringMode) &&
        Objects.equals(this.monitoredItemIds, setMonitoringModeRequest.monitoredItemIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionId, monitoringMode, monitoredItemIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetMonitoringModeRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    monitoringMode: ").append(toIndentedString(monitoringMode)).append("\n");
    sb.append("    monitoredItemIds: ").append(toIndentedString(monitoredItemIds)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2).
 */

@Schema(name = "MonitoredItemCreateResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class MonitoredItemCreateResult {

  private @Nullable StatusCode statusCode;

  private Long monitoredItemId = 0l;

  private Double revisedSamplingInterval = 0d;

  private Long revisedQueueSize = 0l;

  private @Nullable ExtensionObject filterResult;

  public MonitoredItemCreateResult statusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Get statusCode
   * @return statusCode
   */
  @Valid 
  @Schema(name = "StatusCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StatusCode")
  public StatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public MonitoredItemCreateResult monitoredItemId(Long monitoredItemId) {
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

  public MonitoredItemCreateResult revisedSamplingInterval(Double revisedSamplingInterval) {
    this.revisedSamplingInterval = revisedSamplingInterval;
    return this;
  }

  /**
   * Get revisedSamplingInterval
   * @return revisedSamplingInterval
   */
  
  @Schema(name = "RevisedSamplingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedSamplingInterval")
  public Double getRevisedSamplingInterval() {
    return revisedSamplingInterval;
  }

  public void setRevisedSamplingInterval(Double revisedSamplingInterval) {
    this.revisedSamplingInterval = revisedSamplingInterval;
  }

  public MonitoredItemCreateResult revisedQueueSize(Long revisedQueueSize) {
    this.revisedQueueSize = revisedQueueSize;
    return this;
  }

  /**
   * Get revisedQueueSize
   * minimum: 0
   * maximum: 4294967295
   * @return revisedQueueSize
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RevisedQueueSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedQueueSize")
  public Long getRevisedQueueSize() {
    return revisedQueueSize;
  }

  public void setRevisedQueueSize(Long revisedQueueSize) {
    this.revisedQueueSize = revisedQueueSize;
  }

  public MonitoredItemCreateResult filterResult(ExtensionObject filterResult) {
    this.filterResult = filterResult;
    return this;
  }

  /**
   * Get filterResult
   * @return filterResult
   */
  @Valid 
  @Schema(name = "FilterResult", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("FilterResult")
  public ExtensionObject getFilterResult() {
    return filterResult;
  }

  public void setFilterResult(ExtensionObject filterResult) {
    this.filterResult = filterResult;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonitoredItemCreateResult monitoredItemCreateResult = (MonitoredItemCreateResult) o;
    return Objects.equals(this.statusCode, monitoredItemCreateResult.statusCode) &&
        Objects.equals(this.monitoredItemId, monitoredItemCreateResult.monitoredItemId) &&
        Objects.equals(this.revisedSamplingInterval, monitoredItemCreateResult.revisedSamplingInterval) &&
        Objects.equals(this.revisedQueueSize, monitoredItemCreateResult.revisedQueueSize) &&
        Objects.equals(this.filterResult, monitoredItemCreateResult.filterResult);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, monitoredItemId, revisedSamplingInterval, revisedQueueSize, filterResult);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoredItemCreateResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    monitoredItemId: ").append(toIndentedString(monitoredItemId)).append("\n");
    sb.append("    revisedSamplingInterval: ").append(toIndentedString(revisedSamplingInterval)).append("\n");
    sb.append("    revisedQueueSize: ").append(toIndentedString(revisedQueueSize)).append("\n");
    sb.append("    filterResult: ").append(toIndentedString(filterResult)).append("\n");
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


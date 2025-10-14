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

@Schema(name = "MonitoredItemModifyResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.3/#5.13.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class MonitoredItemModifyResult {

  private @Nullable StatusCode statusCode;

  private Double revisedSamplingInterval = 0d;

  private Long revisedQueueSize = 0l;

  private @Nullable ExtensionObject filterResult;

  public MonitoredItemModifyResult statusCode(StatusCode statusCode) {
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

  public MonitoredItemModifyResult revisedSamplingInterval(Double revisedSamplingInterval) {
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

  public MonitoredItemModifyResult revisedQueueSize(Long revisedQueueSize) {
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

  public MonitoredItemModifyResult filterResult(ExtensionObject filterResult) {
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
    MonitoredItemModifyResult monitoredItemModifyResult = (MonitoredItemModifyResult) o;
    return Objects.equals(this.statusCode, monitoredItemModifyResult.statusCode) &&
        Objects.equals(this.revisedSamplingInterval, monitoredItemModifyResult.revisedSamplingInterval) &&
        Objects.equals(this.revisedQueueSize, monitoredItemModifyResult.revisedQueueSize) &&
        Objects.equals(this.filterResult, monitoredItemModifyResult.filterResult);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, revisedSamplingInterval, revisedQueueSize, filterResult);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoredItemModifyResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
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


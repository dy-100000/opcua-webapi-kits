package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.21).
 */

@Schema(name = "MonitoringParameters", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.21).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class MonitoringParameters {

  private Long clientHandle = 0l;

  private Double samplingInterval = 0d;

  private @Nullable ExtensionObject filter;

  private Long queueSize = 0l;

  private Boolean discardOldest = false;

  public MonitoringParameters clientHandle(Long clientHandle) {
    this.clientHandle = clientHandle;
    return this;
  }

  /**
   * Get clientHandle
   * minimum: 0
   * maximum: 4294967295
   * @return clientHandle
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ClientHandle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientHandle")
  public Long getClientHandle() {
    return clientHandle;
  }

  public void setClientHandle(Long clientHandle) {
    this.clientHandle = clientHandle;
  }

  public MonitoringParameters samplingInterval(Double samplingInterval) {
    this.samplingInterval = samplingInterval;
    return this;
  }

  /**
   * Get samplingInterval
   * @return samplingInterval
   */
  
  @Schema(name = "SamplingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SamplingInterval")
  public Double getSamplingInterval() {
    return samplingInterval;
  }

  public void setSamplingInterval(Double samplingInterval) {
    this.samplingInterval = samplingInterval;
  }

  public MonitoringParameters filter(ExtensionObject filter) {
    this.filter = filter;
    return this;
  }

  /**
   * Get filter
   * @return filter
   */
  @Valid 
  @Schema(name = "Filter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Filter")
  public ExtensionObject getFilter() {
    return filter;
  }

  public void setFilter(ExtensionObject filter) {
    this.filter = filter;
  }

  public MonitoringParameters queueSize(Long queueSize) {
    this.queueSize = queueSize;
    return this;
  }

  /**
   * Get queueSize
   * minimum: 0
   * maximum: 4294967295
   * @return queueSize
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "QueueSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("QueueSize")
  public Long getQueueSize() {
    return queueSize;
  }

  public void setQueueSize(Long queueSize) {
    this.queueSize = queueSize;
  }

  public MonitoringParameters discardOldest(Boolean discardOldest) {
    this.discardOldest = discardOldest;
    return this;
  }

  /**
   * Get discardOldest
   * @return discardOldest
   */
  
  @Schema(name = "DiscardOldest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiscardOldest")
  public Boolean getDiscardOldest() {
    return discardOldest;
  }

  public void setDiscardOldest(Boolean discardOldest) {
    this.discardOldest = discardOldest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonitoringParameters monitoringParameters = (MonitoringParameters) o;
    return Objects.equals(this.clientHandle, monitoringParameters.clientHandle) &&
        Objects.equals(this.samplingInterval, monitoringParameters.samplingInterval) &&
        Objects.equals(this.filter, monitoringParameters.filter) &&
        Objects.equals(this.queueSize, monitoringParameters.queueSize) &&
        Objects.equals(this.discardOldest, monitoringParameters.discardOldest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientHandle, samplingInterval, filter, queueSize, discardOldest);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoringParameters {\n");
    sb.append("    clientHandle: ").append(toIndentedString(clientHandle)).append("\n");
    sb.append("    samplingInterval: ").append(toIndentedString(samplingInterval)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
    sb.append("    queueSize: ").append(toIndentedString(queueSize)).append("\n");
    sb.append("    discardOldest: ").append(toIndentedString(discardOldest)).append("\n");
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


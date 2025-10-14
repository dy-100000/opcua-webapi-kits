package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.4).
 */

@Schema(name = "AggregateFilterResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class AggregateFilterResult {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime revisedStartTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Double revisedProcessingInterval = 0d;

  private @Nullable AggregateConfiguration revisedAggregateConfiguration;

  public AggregateFilterResult revisedStartTime(OffsetDateTime revisedStartTime) {
    this.revisedStartTime = revisedStartTime;
    return this;
  }

  /**
   * Get revisedStartTime
   * @return revisedStartTime
   */
  @Valid 
  @Schema(name = "RevisedStartTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedStartTime")
  public OffsetDateTime getRevisedStartTime() {
    return revisedStartTime;
  }

  public void setRevisedStartTime(OffsetDateTime revisedStartTime) {
    this.revisedStartTime = revisedStartTime;
  }

  public AggregateFilterResult revisedProcessingInterval(Double revisedProcessingInterval) {
    this.revisedProcessingInterval = revisedProcessingInterval;
    return this;
  }

  /**
   * Get revisedProcessingInterval
   * @return revisedProcessingInterval
   */
  
  @Schema(name = "RevisedProcessingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedProcessingInterval")
  public Double getRevisedProcessingInterval() {
    return revisedProcessingInterval;
  }

  public void setRevisedProcessingInterval(Double revisedProcessingInterval) {
    this.revisedProcessingInterval = revisedProcessingInterval;
  }

  public AggregateFilterResult revisedAggregateConfiguration(AggregateConfiguration revisedAggregateConfiguration) {
    this.revisedAggregateConfiguration = revisedAggregateConfiguration;
    return this;
  }

  /**
   * Get revisedAggregateConfiguration
   * @return revisedAggregateConfiguration
   */
  @Valid 
  @Schema(name = "RevisedAggregateConfiguration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedAggregateConfiguration")
  public AggregateConfiguration getRevisedAggregateConfiguration() {
    return revisedAggregateConfiguration;
  }

  public void setRevisedAggregateConfiguration(AggregateConfiguration revisedAggregateConfiguration) {
    this.revisedAggregateConfiguration = revisedAggregateConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggregateFilterResult aggregateFilterResult = (AggregateFilterResult) o;
    return Objects.equals(this.revisedStartTime, aggregateFilterResult.revisedStartTime) &&
        Objects.equals(this.revisedProcessingInterval, aggregateFilterResult.revisedProcessingInterval) &&
        Objects.equals(this.revisedAggregateConfiguration, aggregateFilterResult.revisedAggregateConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(revisedStartTime, revisedProcessingInterval, revisedAggregateConfiguration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggregateFilterResult {\n");
    sb.append("    revisedStartTime: ").append(toIndentedString(revisedStartTime)).append("\n");
    sb.append("    revisedProcessingInterval: ").append(toIndentedString(revisedProcessingInterval)).append("\n");
    sb.append("    revisedAggregateConfiguration: ").append(toIndentedString(revisedAggregateConfiguration)).append("\n");
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


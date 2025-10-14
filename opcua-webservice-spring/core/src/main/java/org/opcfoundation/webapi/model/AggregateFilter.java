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

@Schema(name = "AggregateFilter", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class AggregateFilter {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable String aggregateType;

  private Double processingInterval = 0d;

  private @Nullable AggregateConfiguration aggregateConfiguration;

  public AggregateFilter startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  /**
   * Get startTime
   * @return startTime
   */
  @Valid 
  @Schema(name = "StartTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StartTime")
  public OffsetDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  public AggregateFilter aggregateType(String aggregateType) {
    this.aggregateType = aggregateType;
    return this;
  }

  /**
   * Get aggregateType
   * @return aggregateType
   */
  
  @Schema(name = "AggregateType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AggregateType")
  public String getAggregateType() {
    return aggregateType;
  }

  public void setAggregateType(String aggregateType) {
    this.aggregateType = aggregateType;
  }

  public AggregateFilter processingInterval(Double processingInterval) {
    this.processingInterval = processingInterval;
    return this;
  }

  /**
   * Get processingInterval
   * @return processingInterval
   */
  
  @Schema(name = "ProcessingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ProcessingInterval")
  public Double getProcessingInterval() {
    return processingInterval;
  }

  public void setProcessingInterval(Double processingInterval) {
    this.processingInterval = processingInterval;
  }

  public AggregateFilter aggregateConfiguration(AggregateConfiguration aggregateConfiguration) {
    this.aggregateConfiguration = aggregateConfiguration;
    return this;
  }

  /**
   * Get aggregateConfiguration
   * @return aggregateConfiguration
   */
  @Valid 
  @Schema(name = "AggregateConfiguration", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AggregateConfiguration")
  public AggregateConfiguration getAggregateConfiguration() {
    return aggregateConfiguration;
  }

  public void setAggregateConfiguration(AggregateConfiguration aggregateConfiguration) {
    this.aggregateConfiguration = aggregateConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggregateFilter aggregateFilter = (AggregateFilter) o;
    return Objects.equals(this.startTime, aggregateFilter.startTime) &&
        Objects.equals(this.aggregateType, aggregateFilter.aggregateType) &&
        Objects.equals(this.processingInterval, aggregateFilter.processingInterval) &&
        Objects.equals(this.aggregateConfiguration, aggregateFilter.aggregateConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTime, aggregateType, processingInterval, aggregateConfiguration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggregateFilter {\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    aggregateType: ").append(toIndentedString(aggregateType)).append("\n");
    sb.append("    processingInterval: ").append(toIndentedString(processingInterval)).append("\n");
    sb.append("    aggregateConfiguration: ").append(toIndentedString(aggregateConfiguration)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.4/#6.5.4.1).
 */

@Schema(name = "ReadProcessedDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.4/#6.5.4.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadProcessedDetails {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Double processingInterval = 0d;

  @Valid
  private List<String> aggregateType = new ArrayList<>();

  private @Nullable AggregateConfiguration aggregateConfiguration;

  public ReadProcessedDetails startTime(OffsetDateTime startTime) {
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

  public ReadProcessedDetails endTime(OffsetDateTime endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * Get endTime
   * @return endTime
   */
  @Valid 
  @Schema(name = "EndTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("EndTime")
  public OffsetDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(OffsetDateTime endTime) {
    this.endTime = endTime;
  }

  public ReadProcessedDetails processingInterval(Double processingInterval) {
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

  public ReadProcessedDetails aggregateType(List<String> aggregateType) {
    this.aggregateType = aggregateType;
    return this;
  }

  public ReadProcessedDetails addAggregateTypeItem(String aggregateTypeItem) {
    if (this.aggregateType == null) {
      this.aggregateType = new ArrayList<>();
    }
    this.aggregateType.add(aggregateTypeItem);
    return this;
  }

  /**
   * Get aggregateType
   * @return aggregateType
   */
  
  @Schema(name = "AggregateType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AggregateType")
  public List<String> getAggregateType() {
    return aggregateType;
  }

  public void setAggregateType(List<String> aggregateType) {
    this.aggregateType = aggregateType;
  }

  public ReadProcessedDetails aggregateConfiguration(AggregateConfiguration aggregateConfiguration) {
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
    ReadProcessedDetails readProcessedDetails = (ReadProcessedDetails) o;
    return Objects.equals(this.startTime, readProcessedDetails.startTime) &&
        Objects.equals(this.endTime, readProcessedDetails.endTime) &&
        Objects.equals(this.processingInterval, readProcessedDetails.processingInterval) &&
        Objects.equals(this.aggregateType, readProcessedDetails.aggregateType) &&
        Objects.equals(this.aggregateConfiguration, readProcessedDetails.aggregateConfiguration);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startTime, endTime, processingInterval, aggregateType, aggregateConfiguration);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadProcessedDetails {\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    processingInterval: ").append(toIndentedString(processingInterval)).append("\n");
    sb.append("    aggregateType: ").append(toIndentedString(aggregateType)).append("\n");
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


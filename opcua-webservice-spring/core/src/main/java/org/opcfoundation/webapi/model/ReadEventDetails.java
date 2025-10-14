package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.1).
 */

@Schema(name = "ReadEventDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadEventDetails {

  private Long numValuesPerNode = 0l;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable EventFilter filter;

  public ReadEventDetails numValuesPerNode(Long numValuesPerNode) {
    this.numValuesPerNode = numValuesPerNode;
    return this;
  }

  /**
   * Get numValuesPerNode
   * minimum: 0
   * maximum: 4294967295
   * @return numValuesPerNode
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "NumValuesPerNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NumValuesPerNode")
  public Long getNumValuesPerNode() {
    return numValuesPerNode;
  }

  public void setNumValuesPerNode(Long numValuesPerNode) {
    this.numValuesPerNode = numValuesPerNode;
  }

  public ReadEventDetails startTime(OffsetDateTime startTime) {
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

  public ReadEventDetails endTime(OffsetDateTime endTime) {
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

  public ReadEventDetails filter(EventFilter filter) {
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
  public EventFilter getFilter() {
    return filter;
  }

  public void setFilter(EventFilter filter) {
    this.filter = filter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadEventDetails readEventDetails = (ReadEventDetails) o;
    return Objects.equals(this.numValuesPerNode, readEventDetails.numValuesPerNode) &&
        Objects.equals(this.startTime, readEventDetails.startTime) &&
        Objects.equals(this.endTime, readEventDetails.endTime) &&
        Objects.equals(this.filter, readEventDetails.filter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numValuesPerNode, startTime, endTime, filter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadEventDetails {\n");
    sb.append("    numValuesPerNode: ").append(toIndentedString(numValuesPerNode)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
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


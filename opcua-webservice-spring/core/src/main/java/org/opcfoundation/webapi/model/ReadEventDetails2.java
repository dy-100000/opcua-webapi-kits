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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.3).
 */

@Schema(name = "ReadEventDetails2", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.2/#6.5.2.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadEventDetails2 {

  private Boolean readModified = false;

  private Long numValuesPerNode = 0l;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable EventFilter filter;

  public ReadEventDetails2 readModified(Boolean readModified) {
    this.readModified = readModified;
    return this;
  }

  /**
   * Get readModified
   * @return readModified
   */
  
  @Schema(name = "ReadModified", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReadModified")
  public Boolean getReadModified() {
    return readModified;
  }

  public void setReadModified(Boolean readModified) {
    this.readModified = readModified;
  }

  public ReadEventDetails2 numValuesPerNode(Long numValuesPerNode) {
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

  public ReadEventDetails2 startTime(OffsetDateTime startTime) {
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

  public ReadEventDetails2 endTime(OffsetDateTime endTime) {
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

  public ReadEventDetails2 filter(EventFilter filter) {
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
    ReadEventDetails2 readEventDetails2 = (ReadEventDetails2) o;
    return Objects.equals(this.readModified, readEventDetails2.readModified) &&
        Objects.equals(this.numValuesPerNode, readEventDetails2.numValuesPerNode) &&
        Objects.equals(this.startTime, readEventDetails2.startTime) &&
        Objects.equals(this.endTime, readEventDetails2.endTime) &&
        Objects.equals(this.filter, readEventDetails2.filter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(readModified, numValuesPerNode, startTime, endTime, filter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadEventDetails2 {\n");
    sb.append("    readModified: ").append(toIndentedString(readModified)).append("\n");
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


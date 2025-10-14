package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.3/#6.5.3.1).
 */

@Schema(name = "ReadRawModifiedDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.3/#6.5.3.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadRawModifiedDetails {

  private Boolean isReadModified = false;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Long numValuesPerNode = 0l;

  private Boolean returnBounds = false;

  public ReadRawModifiedDetails isReadModified(Boolean isReadModified) {
    this.isReadModified = isReadModified;
    return this;
  }

  /**
   * Get isReadModified
   * @return isReadModified
   */
  
  @Schema(name = "IsReadModified", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IsReadModified")
  public Boolean getIsReadModified() {
    return isReadModified;
  }

  public void setIsReadModified(Boolean isReadModified) {
    this.isReadModified = isReadModified;
  }

  public ReadRawModifiedDetails startTime(OffsetDateTime startTime) {
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

  public ReadRawModifiedDetails endTime(OffsetDateTime endTime) {
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

  public ReadRawModifiedDetails numValuesPerNode(Long numValuesPerNode) {
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

  public ReadRawModifiedDetails returnBounds(Boolean returnBounds) {
    this.returnBounds = returnBounds;
    return this;
  }

  /**
   * Get returnBounds
   * @return returnBounds
   */
  
  @Schema(name = "ReturnBounds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReturnBounds")
  public Boolean getReturnBounds() {
    return returnBounds;
  }

  public void setReturnBounds(Boolean returnBounds) {
    this.returnBounds = returnBounds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadRawModifiedDetails readRawModifiedDetails = (ReadRawModifiedDetails) o;
    return Objects.equals(this.isReadModified, readRawModifiedDetails.isReadModified) &&
        Objects.equals(this.startTime, readRawModifiedDetails.startTime) &&
        Objects.equals(this.endTime, readRawModifiedDetails.endTime) &&
        Objects.equals(this.numValuesPerNode, readRawModifiedDetails.numValuesPerNode) &&
        Objects.equals(this.returnBounds, readRawModifiedDetails.returnBounds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isReadModified, startTime, endTime, numValuesPerNode, returnBounds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadRawModifiedDetails {\n");
    sb.append("    isReadModified: ").append(toIndentedString(isReadModified)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    numValuesPerNode: ").append(toIndentedString(numValuesPerNode)).append("\n");
    sb.append("    returnBounds: ").append(toIndentedString(returnBounds)).append("\n");
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


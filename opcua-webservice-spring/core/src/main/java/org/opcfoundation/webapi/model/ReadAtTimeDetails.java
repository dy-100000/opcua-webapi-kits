package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.5/#6.5.5.1).
 */

@Schema(name = "ReadAtTimeDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.5/#6.5.5.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadAtTimeDetails {

  @Valid
  private List<OffsetDateTime> reqTimes = new ArrayList<>();

  private Boolean useSimpleBounds = false;

  public ReadAtTimeDetails reqTimes(List<OffsetDateTime> reqTimes) {
    this.reqTimes = reqTimes;
    return this;
  }

  public ReadAtTimeDetails addReqTimesItem(OffsetDateTime reqTimesItem) {
    if (this.reqTimes == null) {
      this.reqTimes = new ArrayList<>();
    }
    this.reqTimes.add(reqTimesItem);
    return this;
  }

  /**
   * Get reqTimes
   * @return reqTimes
   */
  @Valid 
  @Schema(name = "ReqTimes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReqTimes")
  public List<OffsetDateTime> getReqTimes() {
    return reqTimes;
  }

  public void setReqTimes(List<OffsetDateTime> reqTimes) {
    this.reqTimes = reqTimes;
  }

  public ReadAtTimeDetails useSimpleBounds(Boolean useSimpleBounds) {
    this.useSimpleBounds = useSimpleBounds;
    return this;
  }

  /**
   * Get useSimpleBounds
   * @return useSimpleBounds
   */
  
  @Schema(name = "UseSimpleBounds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UseSimpleBounds")
  public Boolean getUseSimpleBounds() {
    return useSimpleBounds;
  }

  public void setUseSimpleBounds(Boolean useSimpleBounds) {
    this.useSimpleBounds = useSimpleBounds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadAtTimeDetails readAtTimeDetails = (ReadAtTimeDetails) o;
    return Objects.equals(this.reqTimes, readAtTimeDetails.reqTimes) &&
        Objects.equals(this.useSimpleBounds, readAtTimeDetails.useSimpleBounds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reqTimes, useSimpleBounds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadAtTimeDetails {\n");
    sb.append("    reqTimes: ").append(toIndentedString(reqTimes)).append("\n");
    sb.append("    useSimpleBounds: ").append(toIndentedString(useSimpleBounds)).append("\n");
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


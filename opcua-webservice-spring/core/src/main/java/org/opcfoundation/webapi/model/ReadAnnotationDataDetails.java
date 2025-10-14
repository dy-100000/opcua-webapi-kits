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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.6/#6.5.6.1).
 */

@Schema(name = "ReadAnnotationDataDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.6/#6.5.6.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadAnnotationDataDetails {

  @Valid
  private List<OffsetDateTime> reqTimes = new ArrayList<>();

  public ReadAnnotationDataDetails reqTimes(List<OffsetDateTime> reqTimes) {
    this.reqTimes = reqTimes;
    return this;
  }

  public ReadAnnotationDataDetails addReqTimesItem(OffsetDateTime reqTimesItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadAnnotationDataDetails readAnnotationDataDetails = (ReadAnnotationDataDetails) o;
    return Objects.equals(this.reqTimes, readAnnotationDataDetails.reqTimes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reqTimes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadAnnotationDataDetails {\n");
    sb.append("    reqTimes: ").append(toIndentedString(reqTimes)).append("\n");
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


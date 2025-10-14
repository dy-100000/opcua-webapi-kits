package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2).
 */

@Schema(name = "ModifySubscriptionResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ModifySubscriptionResponse {

  private @Nullable ResponseHeader responseHeader;

  private Double revisedPublishingInterval = 0d;

  private Long revisedLifetimeCount = 0l;

  private Long revisedMaxKeepAliveCount = 0l;

  public ModifySubscriptionResponse responseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
    return this;
  }

  /**
   * Get responseHeader
   * @return responseHeader
   */
  @Valid 
  @Schema(name = "ResponseHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResponseHeader")
  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public void setResponseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
  }

  public ModifySubscriptionResponse revisedPublishingInterval(Double revisedPublishingInterval) {
    this.revisedPublishingInterval = revisedPublishingInterval;
    return this;
  }

  /**
   * Get revisedPublishingInterval
   * @return revisedPublishingInterval
   */
  
  @Schema(name = "RevisedPublishingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedPublishingInterval")
  public Double getRevisedPublishingInterval() {
    return revisedPublishingInterval;
  }

  public void setRevisedPublishingInterval(Double revisedPublishingInterval) {
    this.revisedPublishingInterval = revisedPublishingInterval;
  }

  public ModifySubscriptionResponse revisedLifetimeCount(Long revisedLifetimeCount) {
    this.revisedLifetimeCount = revisedLifetimeCount;
    return this;
  }

  /**
   * Get revisedLifetimeCount
   * minimum: 0
   * maximum: 4294967295
   * @return revisedLifetimeCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RevisedLifetimeCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedLifetimeCount")
  public Long getRevisedLifetimeCount() {
    return revisedLifetimeCount;
  }

  public void setRevisedLifetimeCount(Long revisedLifetimeCount) {
    this.revisedLifetimeCount = revisedLifetimeCount;
  }

  public ModifySubscriptionResponse revisedMaxKeepAliveCount(Long revisedMaxKeepAliveCount) {
    this.revisedMaxKeepAliveCount = revisedMaxKeepAliveCount;
    return this;
  }

  /**
   * Get revisedMaxKeepAliveCount
   * minimum: 0
   * maximum: 4294967295
   * @return revisedMaxKeepAliveCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RevisedMaxKeepAliveCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedMaxKeepAliveCount")
  public Long getRevisedMaxKeepAliveCount() {
    return revisedMaxKeepAliveCount;
  }

  public void setRevisedMaxKeepAliveCount(Long revisedMaxKeepAliveCount) {
    this.revisedMaxKeepAliveCount = revisedMaxKeepAliveCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModifySubscriptionResponse modifySubscriptionResponse = (ModifySubscriptionResponse) o;
    return Objects.equals(this.responseHeader, modifySubscriptionResponse.responseHeader) &&
        Objects.equals(this.revisedPublishingInterval, modifySubscriptionResponse.revisedPublishingInterval) &&
        Objects.equals(this.revisedLifetimeCount, modifySubscriptionResponse.revisedLifetimeCount) &&
        Objects.equals(this.revisedMaxKeepAliveCount, modifySubscriptionResponse.revisedMaxKeepAliveCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, revisedPublishingInterval, revisedLifetimeCount, revisedMaxKeepAliveCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModifySubscriptionResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    revisedPublishingInterval: ").append(toIndentedString(revisedPublishingInterval)).append("\n");
    sb.append("    revisedLifetimeCount: ").append(toIndentedString(revisedLifetimeCount)).append("\n");
    sb.append("    revisedMaxKeepAliveCount: ").append(toIndentedString(revisedMaxKeepAliveCount)).append("\n");
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


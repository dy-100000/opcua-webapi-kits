package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2).
 */

@Schema(name = "RepublishRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RepublishRequest {

  private @Nullable RequestHeader requestHeader;

  private Long subscriptionId = 0l;

  private Long retransmitSequenceNumber = 0l;

  public RepublishRequest requestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
    return this;
  }

  /**
   * Get requestHeader
   * @return requestHeader
   */
  @Valid 
  @Schema(name = "RequestHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHeader")
  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public void setRequestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
  }

  public RepublishRequest subscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * Get subscriptionId
   * minimum: 0
   * maximum: 4294967295
   * @return subscriptionId
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "SubscriptionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscriptionId")
  public Long getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public RepublishRequest retransmitSequenceNumber(Long retransmitSequenceNumber) {
    this.retransmitSequenceNumber = retransmitSequenceNumber;
    return this;
  }

  /**
   * Get retransmitSequenceNumber
   * minimum: 0
   * maximum: 4294967295
   * @return retransmitSequenceNumber
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RetransmitSequenceNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RetransmitSequenceNumber")
  public Long getRetransmitSequenceNumber() {
    return retransmitSequenceNumber;
  }

  public void setRetransmitSequenceNumber(Long retransmitSequenceNumber) {
    this.retransmitSequenceNumber = retransmitSequenceNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RepublishRequest republishRequest = (RepublishRequest) o;
    return Objects.equals(this.requestHeader, republishRequest.requestHeader) &&
        Objects.equals(this.subscriptionId, republishRequest.subscriptionId) &&
        Objects.equals(this.retransmitSequenceNumber, republishRequest.retransmitSequenceNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionId, retransmitSequenceNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RepublishRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    retransmitSequenceNumber: ").append(toIndentedString(retransmitSequenceNumber)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2).
 */

@Schema(name = "SubscriptionAcknowledgement", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SubscriptionAcknowledgement {

  private Long subscriptionId = 0l;

  private Long sequenceNumber = 0l;

  public SubscriptionAcknowledgement subscriptionId(Long subscriptionId) {
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

  public SubscriptionAcknowledgement sequenceNumber(Long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  /**
   * Get sequenceNumber
   * minimum: 0
   * maximum: 4294967295
   * @return sequenceNumber
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "SequenceNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SequenceNumber")
  public Long getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubscriptionAcknowledgement subscriptionAcknowledgement = (SubscriptionAcknowledgement) o;
    return Objects.equals(this.subscriptionId, subscriptionAcknowledgement.subscriptionId) &&
        Objects.equals(this.sequenceNumber, subscriptionAcknowledgement.sequenceNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, sequenceNumber);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubscriptionAcknowledgement {\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
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


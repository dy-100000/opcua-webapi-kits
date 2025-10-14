package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2).
 */

@Schema(name = "PublishRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PublishRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<@Valid SubscriptionAcknowledgement> subscriptionAcknowledgements = new ArrayList<>();

  public PublishRequest requestHeader(RequestHeader requestHeader) {
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

  public PublishRequest subscriptionAcknowledgements(List<@Valid SubscriptionAcknowledgement> subscriptionAcknowledgements) {
    this.subscriptionAcknowledgements = subscriptionAcknowledgements;
    return this;
  }

  public PublishRequest addSubscriptionAcknowledgementsItem(SubscriptionAcknowledgement subscriptionAcknowledgementsItem) {
    if (this.subscriptionAcknowledgements == null) {
      this.subscriptionAcknowledgements = new ArrayList<>();
    }
    this.subscriptionAcknowledgements.add(subscriptionAcknowledgementsItem);
    return this;
  }

  /**
   * Get subscriptionAcknowledgements
   * @return subscriptionAcknowledgements
   */
  @Valid 
  @Schema(name = "SubscriptionAcknowledgements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscriptionAcknowledgements")
  public List<@Valid SubscriptionAcknowledgement> getSubscriptionAcknowledgements() {
    return subscriptionAcknowledgements;
  }

  public void setSubscriptionAcknowledgements(List<@Valid SubscriptionAcknowledgement> subscriptionAcknowledgements) {
    this.subscriptionAcknowledgements = subscriptionAcknowledgements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PublishRequest publishRequest = (PublishRequest) o;
    return Objects.equals(this.requestHeader, publishRequest.requestHeader) &&
        Objects.equals(this.subscriptionAcknowledgements, publishRequest.subscriptionAcknowledgements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionAcknowledgements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PublishRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionAcknowledgements: ").append(toIndentedString(subscriptionAcknowledgements)).append("\n");
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


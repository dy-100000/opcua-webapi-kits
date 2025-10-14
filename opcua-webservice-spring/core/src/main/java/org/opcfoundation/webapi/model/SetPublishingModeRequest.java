package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.4/#5.14.4.2).
 */

@Schema(name = "SetPublishingModeRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.4/#5.14.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SetPublishingModeRequest {

  private @Nullable RequestHeader requestHeader;

  private Boolean publishingEnabled = false;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> subscriptionIds = new ArrayList<>();

  public SetPublishingModeRequest requestHeader(RequestHeader requestHeader) {
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

  public SetPublishingModeRequest publishingEnabled(Boolean publishingEnabled) {
    this.publishingEnabled = publishingEnabled;
    return this;
  }

  /**
   * Get publishingEnabled
   * @return publishingEnabled
   */
  
  @Schema(name = "PublishingEnabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PublishingEnabled")
  public Boolean getPublishingEnabled() {
    return publishingEnabled;
  }

  public void setPublishingEnabled(Boolean publishingEnabled) {
    this.publishingEnabled = publishingEnabled;
  }

  public SetPublishingModeRequest subscriptionIds(List<@Min(0L) @Max(4294967295L)Long> subscriptionIds) {
    this.subscriptionIds = subscriptionIds;
    return this;
  }

  public SetPublishingModeRequest addSubscriptionIdsItem(Long subscriptionIdsItem) {
    if (this.subscriptionIds == null) {
      this.subscriptionIds = new ArrayList<>();
    }
    this.subscriptionIds.add(subscriptionIdsItem);
    return this;
  }

  /**
   * Get subscriptionIds
   * @return subscriptionIds
   */
  
  @Schema(name = "SubscriptionIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscriptionIds")
  public List<@Min(0L) @Max(4294967295L)Long> getSubscriptionIds() {
    return subscriptionIds;
  }

  public void setSubscriptionIds(List<@Min(0L) @Max(4294967295L)Long> subscriptionIds) {
    this.subscriptionIds = subscriptionIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetPublishingModeRequest setPublishingModeRequest = (SetPublishingModeRequest) o;
    return Objects.equals(this.requestHeader, setPublishingModeRequest.requestHeader) &&
        Objects.equals(this.publishingEnabled, setPublishingModeRequest.publishingEnabled) &&
        Objects.equals(this.subscriptionIds, setPublishingModeRequest.subscriptionIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, publishingEnabled, subscriptionIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetPublishingModeRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    publishingEnabled: ").append(toIndentedString(publishingEnabled)).append("\n");
    sb.append("    subscriptionIds: ").append(toIndentedString(subscriptionIds)).append("\n");
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


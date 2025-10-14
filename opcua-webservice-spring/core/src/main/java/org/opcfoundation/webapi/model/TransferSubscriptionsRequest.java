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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2).
 */

@Schema(name = "TransferSubscriptionsRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class TransferSubscriptionsRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> subscriptionIds = new ArrayList<>();

  private Boolean sendInitialValues = false;

  public TransferSubscriptionsRequest requestHeader(RequestHeader requestHeader) {
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

  public TransferSubscriptionsRequest subscriptionIds(List<@Min(0L) @Max(4294967295L)Long> subscriptionIds) {
    this.subscriptionIds = subscriptionIds;
    return this;
  }

  public TransferSubscriptionsRequest addSubscriptionIdsItem(Long subscriptionIdsItem) {
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

  public TransferSubscriptionsRequest sendInitialValues(Boolean sendInitialValues) {
    this.sendInitialValues = sendInitialValues;
    return this;
  }

  /**
   * Get sendInitialValues
   * @return sendInitialValues
   */
  
  @Schema(name = "SendInitialValues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SendInitialValues")
  public Boolean getSendInitialValues() {
    return sendInitialValues;
  }

  public void setSendInitialValues(Boolean sendInitialValues) {
    this.sendInitialValues = sendInitialValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferSubscriptionsRequest transferSubscriptionsRequest = (TransferSubscriptionsRequest) o;
    return Objects.equals(this.requestHeader, transferSubscriptionsRequest.requestHeader) &&
        Objects.equals(this.subscriptionIds, transferSubscriptionsRequest.subscriptionIds) &&
        Objects.equals(this.sendInitialValues, transferSubscriptionsRequest.sendInitialValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionIds, sendInitialValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferSubscriptionsRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionIds: ").append(toIndentedString(subscriptionIds)).append("\n");
    sb.append("    sendInitialValues: ").append(toIndentedString(sendInitialValues)).append("\n");
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


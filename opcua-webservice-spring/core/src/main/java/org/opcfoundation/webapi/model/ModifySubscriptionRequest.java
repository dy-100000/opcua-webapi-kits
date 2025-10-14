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

@Schema(name = "ModifySubscriptionRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.3/#5.14.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ModifySubscriptionRequest {

  private @Nullable RequestHeader requestHeader;

  private Long subscriptionId = 0l;

  private Double requestedPublishingInterval = 0d;

  private Long requestedLifetimeCount = 0l;

  private Long requestedMaxKeepAliveCount = 0l;

  private Long maxNotificationsPerPublish = 0l;

  private Integer priority = 0;

  public ModifySubscriptionRequest requestHeader(RequestHeader requestHeader) {
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

  public ModifySubscriptionRequest subscriptionId(Long subscriptionId) {
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

  public ModifySubscriptionRequest requestedPublishingInterval(Double requestedPublishingInterval) {
    this.requestedPublishingInterval = requestedPublishingInterval;
    return this;
  }

  /**
   * Get requestedPublishingInterval
   * @return requestedPublishingInterval
   */
  
  @Schema(name = "RequestedPublishingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedPublishingInterval")
  public Double getRequestedPublishingInterval() {
    return requestedPublishingInterval;
  }

  public void setRequestedPublishingInterval(Double requestedPublishingInterval) {
    this.requestedPublishingInterval = requestedPublishingInterval;
  }

  public ModifySubscriptionRequest requestedLifetimeCount(Long requestedLifetimeCount) {
    this.requestedLifetimeCount = requestedLifetimeCount;
    return this;
  }

  /**
   * Get requestedLifetimeCount
   * minimum: 0
   * maximum: 4294967295
   * @return requestedLifetimeCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RequestedLifetimeCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedLifetimeCount")
  public Long getRequestedLifetimeCount() {
    return requestedLifetimeCount;
  }

  public void setRequestedLifetimeCount(Long requestedLifetimeCount) {
    this.requestedLifetimeCount = requestedLifetimeCount;
  }

  public ModifySubscriptionRequest requestedMaxKeepAliveCount(Long requestedMaxKeepAliveCount) {
    this.requestedMaxKeepAliveCount = requestedMaxKeepAliveCount;
    return this;
  }

  /**
   * Get requestedMaxKeepAliveCount
   * minimum: 0
   * maximum: 4294967295
   * @return requestedMaxKeepAliveCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RequestedMaxKeepAliveCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedMaxKeepAliveCount")
  public Long getRequestedMaxKeepAliveCount() {
    return requestedMaxKeepAliveCount;
  }

  public void setRequestedMaxKeepAliveCount(Long requestedMaxKeepAliveCount) {
    this.requestedMaxKeepAliveCount = requestedMaxKeepAliveCount;
  }

  public ModifySubscriptionRequest maxNotificationsPerPublish(Long maxNotificationsPerPublish) {
    this.maxNotificationsPerPublish = maxNotificationsPerPublish;
    return this;
  }

  /**
   * Get maxNotificationsPerPublish
   * minimum: 0
   * maximum: 4294967295
   * @return maxNotificationsPerPublish
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxNotificationsPerPublish", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxNotificationsPerPublish")
  public Long getMaxNotificationsPerPublish() {
    return maxNotificationsPerPublish;
  }

  public void setMaxNotificationsPerPublish(Long maxNotificationsPerPublish) {
    this.maxNotificationsPerPublish = maxNotificationsPerPublish;
  }

  public ModifySubscriptionRequest priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * minimum: 0
   * maximum: 255
   * @return priority
   */
  @Min(0) @Max(255) 
  @Schema(name = "Priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Priority")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModifySubscriptionRequest modifySubscriptionRequest = (ModifySubscriptionRequest) o;
    return Objects.equals(this.requestHeader, modifySubscriptionRequest.requestHeader) &&
        Objects.equals(this.subscriptionId, modifySubscriptionRequest.subscriptionId) &&
        Objects.equals(this.requestedPublishingInterval, modifySubscriptionRequest.requestedPublishingInterval) &&
        Objects.equals(this.requestedLifetimeCount, modifySubscriptionRequest.requestedLifetimeCount) &&
        Objects.equals(this.requestedMaxKeepAliveCount, modifySubscriptionRequest.requestedMaxKeepAliveCount) &&
        Objects.equals(this.maxNotificationsPerPublish, modifySubscriptionRequest.maxNotificationsPerPublish) &&
        Objects.equals(this.priority, modifySubscriptionRequest.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionId, requestedPublishingInterval, requestedLifetimeCount, requestedMaxKeepAliveCount, maxNotificationsPerPublish, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModifySubscriptionRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    requestedPublishingInterval: ").append(toIndentedString(requestedPublishingInterval)).append("\n");
    sb.append("    requestedLifetimeCount: ").append(toIndentedString(requestedLifetimeCount)).append("\n");
    sb.append("    requestedMaxKeepAliveCount: ").append(toIndentedString(requestedMaxKeepAliveCount)).append("\n");
    sb.append("    maxNotificationsPerPublish: ").append(toIndentedString(maxNotificationsPerPublish)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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


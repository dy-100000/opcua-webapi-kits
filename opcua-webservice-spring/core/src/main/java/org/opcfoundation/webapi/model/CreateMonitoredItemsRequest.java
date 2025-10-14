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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2).
 */

@Schema(name = "CreateMonitoredItemsRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.2/#5.13.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateMonitoredItemsRequest {

  private @Nullable RequestHeader requestHeader;

  private Long subscriptionId = 0l;

  private @Nullable Integer timestampsToReturn;

  @Valid
  private List<@Valid MonitoredItemCreateRequest> itemsToCreate = new ArrayList<>();

  public CreateMonitoredItemsRequest requestHeader(RequestHeader requestHeader) {
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

  public CreateMonitoredItemsRequest subscriptionId(Long subscriptionId) {
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

  public CreateMonitoredItemsRequest timestampsToReturn(Integer timestampsToReturn) {
    this.timestampsToReturn = timestampsToReturn;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.40).
   * @return timestampsToReturn
   */
  
  @Schema(name = "TimestampsToReturn", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.40).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TimestampsToReturn")
  public Integer getTimestampsToReturn() {
    return timestampsToReturn;
  }

  public void setTimestampsToReturn(Integer timestampsToReturn) {
    this.timestampsToReturn = timestampsToReturn;
  }

  public CreateMonitoredItemsRequest itemsToCreate(List<@Valid MonitoredItemCreateRequest> itemsToCreate) {
    this.itemsToCreate = itemsToCreate;
    return this;
  }

  public CreateMonitoredItemsRequest addItemsToCreateItem(MonitoredItemCreateRequest itemsToCreateItem) {
    if (this.itemsToCreate == null) {
      this.itemsToCreate = new ArrayList<>();
    }
    this.itemsToCreate.add(itemsToCreateItem);
    return this;
  }

  /**
   * Get itemsToCreate
   * @return itemsToCreate
   */
  @Valid 
  @Schema(name = "ItemsToCreate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ItemsToCreate")
  public List<@Valid MonitoredItemCreateRequest> getItemsToCreate() {
    return itemsToCreate;
  }

  public void setItemsToCreate(List<@Valid MonitoredItemCreateRequest> itemsToCreate) {
    this.itemsToCreate = itemsToCreate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateMonitoredItemsRequest createMonitoredItemsRequest = (CreateMonitoredItemsRequest) o;
    return Objects.equals(this.requestHeader, createMonitoredItemsRequest.requestHeader) &&
        Objects.equals(this.subscriptionId, createMonitoredItemsRequest.subscriptionId) &&
        Objects.equals(this.timestampsToReturn, createMonitoredItemsRequest.timestampsToReturn) &&
        Objects.equals(this.itemsToCreate, createMonitoredItemsRequest.itemsToCreate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionId, timestampsToReturn, itemsToCreate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateMonitoredItemsRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    timestampsToReturn: ").append(toIndentedString(timestampsToReturn)).append("\n");
    sb.append("    itemsToCreate: ").append(toIndentedString(itemsToCreate)).append("\n");
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


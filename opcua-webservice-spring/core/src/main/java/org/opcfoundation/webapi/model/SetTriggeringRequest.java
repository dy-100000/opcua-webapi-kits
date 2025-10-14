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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.5/#5.13.5.2).
 */

@Schema(name = "SetTriggeringRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.5/#5.13.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SetTriggeringRequest {

  private @Nullable RequestHeader requestHeader;

  private Long subscriptionId = 0l;

  private Long triggeringItemId = 0l;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> linksToAdd = new ArrayList<>();

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> linksToRemove = new ArrayList<>();

  public SetTriggeringRequest requestHeader(RequestHeader requestHeader) {
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

  public SetTriggeringRequest subscriptionId(Long subscriptionId) {
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

  public SetTriggeringRequest triggeringItemId(Long triggeringItemId) {
    this.triggeringItemId = triggeringItemId;
    return this;
  }

  /**
   * Get triggeringItemId
   * minimum: 0
   * maximum: 4294967295
   * @return triggeringItemId
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "TriggeringItemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TriggeringItemId")
  public Long getTriggeringItemId() {
    return triggeringItemId;
  }

  public void setTriggeringItemId(Long triggeringItemId) {
    this.triggeringItemId = triggeringItemId;
  }

  public SetTriggeringRequest linksToAdd(List<@Min(0L) @Max(4294967295L)Long> linksToAdd) {
    this.linksToAdd = linksToAdd;
    return this;
  }

  public SetTriggeringRequest addLinksToAddItem(Long linksToAddItem) {
    if (this.linksToAdd == null) {
      this.linksToAdd = new ArrayList<>();
    }
    this.linksToAdd.add(linksToAddItem);
    return this;
  }

  /**
   * Get linksToAdd
   * @return linksToAdd
   */
  
  @Schema(name = "LinksToAdd", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("LinksToAdd")
  public List<@Min(0L) @Max(4294967295L)Long> getLinksToAdd() {
    return linksToAdd;
  }

  public void setLinksToAdd(List<@Min(0L) @Max(4294967295L)Long> linksToAdd) {
    this.linksToAdd = linksToAdd;
  }

  public SetTriggeringRequest linksToRemove(List<@Min(0L) @Max(4294967295L)Long> linksToRemove) {
    this.linksToRemove = linksToRemove;
    return this;
  }

  public SetTriggeringRequest addLinksToRemoveItem(Long linksToRemoveItem) {
    if (this.linksToRemove == null) {
      this.linksToRemove = new ArrayList<>();
    }
    this.linksToRemove.add(linksToRemoveItem);
    return this;
  }

  /**
   * Get linksToRemove
   * @return linksToRemove
   */
  
  @Schema(name = "LinksToRemove", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("LinksToRemove")
  public List<@Min(0L) @Max(4294967295L)Long> getLinksToRemove() {
    return linksToRemove;
  }

  public void setLinksToRemove(List<@Min(0L) @Max(4294967295L)Long> linksToRemove) {
    this.linksToRemove = linksToRemove;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetTriggeringRequest setTriggeringRequest = (SetTriggeringRequest) o;
    return Objects.equals(this.requestHeader, setTriggeringRequest.requestHeader) &&
        Objects.equals(this.subscriptionId, setTriggeringRequest.subscriptionId) &&
        Objects.equals(this.triggeringItemId, setTriggeringRequest.triggeringItemId) &&
        Objects.equals(this.linksToAdd, setTriggeringRequest.linksToAdd) &&
        Objects.equals(this.linksToRemove, setTriggeringRequest.linksToRemove);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, subscriptionId, triggeringItemId, linksToAdd, linksToRemove);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetTriggeringRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    triggeringItemId: ").append(toIndentedString(triggeringItemId)).append("\n");
    sb.append("    linksToAdd: ").append(toIndentedString(linksToAdd)).append("\n");
    sb.append("    linksToRemove: ").append(toIndentedString(linksToRemove)).append("\n");
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


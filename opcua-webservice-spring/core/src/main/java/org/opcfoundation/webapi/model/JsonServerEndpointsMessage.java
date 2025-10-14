package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification]().
 */

@Schema(name = "JsonServerEndpointsMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonServerEndpointsMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable ApplicationDescription description;

  @Valid
  private List<@Valid EndpointDescription> endpoints = new ArrayList<>();

  public JsonServerEndpointsMessage messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }

  /**
   * Get messageId
   * @return messageId
   */
  
  @Schema(name = "MessageId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MessageId")
  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public JsonServerEndpointsMessage messageType(String messageType) {
    this.messageType = messageType;
    return this;
  }

  /**
   * Get messageType
   * @return messageType
   */
  
  @Schema(name = "MessageType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MessageType")
  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public JsonServerEndpointsMessage publisherId(String publisherId) {
    this.publisherId = publisherId;
    return this;
  }

  /**
   * Get publisherId
   * @return publisherId
   */
  
  @Schema(name = "PublisherId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PublisherId")
  public String getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(String publisherId) {
    this.publisherId = publisherId;
  }

  public JsonServerEndpointsMessage timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "Timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public JsonServerEndpointsMessage description(ApplicationDescription description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Valid 
  @Schema(name = "Description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Description")
  public ApplicationDescription getDescription() {
    return description;
  }

  public void setDescription(ApplicationDescription description) {
    this.description = description;
  }

  public JsonServerEndpointsMessage endpoints(List<@Valid EndpointDescription> endpoints) {
    this.endpoints = endpoints;
    return this;
  }

  public JsonServerEndpointsMessage addEndpointsItem(EndpointDescription endpointsItem) {
    if (this.endpoints == null) {
      this.endpoints = new ArrayList<>();
    }
    this.endpoints.add(endpointsItem);
    return this;
  }

  /**
   * Get endpoints
   * @return endpoints
   */
  @Valid 
  @Schema(name = "Endpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Endpoints")
  public List<@Valid EndpointDescription> getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(List<@Valid EndpointDescription> endpoints) {
    this.endpoints = endpoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonServerEndpointsMessage jsonServerEndpointsMessage = (JsonServerEndpointsMessage) o;
    return Objects.equals(this.messageId, jsonServerEndpointsMessage.messageId) &&
        Objects.equals(this.messageType, jsonServerEndpointsMessage.messageType) &&
        Objects.equals(this.publisherId, jsonServerEndpointsMessage.publisherId) &&
        Objects.equals(this.timestamp, jsonServerEndpointsMessage.timestamp) &&
        Objects.equals(this.description, jsonServerEndpointsMessage.description) &&
        Objects.equals(this.endpoints, jsonServerEndpointsMessage.endpoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, timestamp, description, endpoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonServerEndpointsMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
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


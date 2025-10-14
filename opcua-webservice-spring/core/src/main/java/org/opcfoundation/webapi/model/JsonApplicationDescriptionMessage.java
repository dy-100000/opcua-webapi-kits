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

@Schema(name = "JsonApplicationDescriptionMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonApplicationDescriptionMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable ApplicationDescription description;

  @Valid
  private List<String> serverCapabilities = new ArrayList<>();

  public JsonApplicationDescriptionMessage messageId(String messageId) {
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

  public JsonApplicationDescriptionMessage messageType(String messageType) {
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

  public JsonApplicationDescriptionMessage publisherId(String publisherId) {
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

  public JsonApplicationDescriptionMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonApplicationDescriptionMessage description(ApplicationDescription description) {
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

  public JsonApplicationDescriptionMessage serverCapabilities(List<String> serverCapabilities) {
    this.serverCapabilities = serverCapabilities;
    return this;
  }

  public JsonApplicationDescriptionMessage addServerCapabilitiesItem(String serverCapabilitiesItem) {
    if (this.serverCapabilities == null) {
      this.serverCapabilities = new ArrayList<>();
    }
    this.serverCapabilities.add(serverCapabilitiesItem);
    return this;
  }

  /**
   * Get serverCapabilities
   * @return serverCapabilities
   */
  
  @Schema(name = "ServerCapabilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerCapabilities")
  public List<String> getServerCapabilities() {
    return serverCapabilities;
  }

  public void setServerCapabilities(List<String> serverCapabilities) {
    this.serverCapabilities = serverCapabilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonApplicationDescriptionMessage jsonApplicationDescriptionMessage = (JsonApplicationDescriptionMessage) o;
    return Objects.equals(this.messageId, jsonApplicationDescriptionMessage.messageId) &&
        Objects.equals(this.messageType, jsonApplicationDescriptionMessage.messageType) &&
        Objects.equals(this.publisherId, jsonApplicationDescriptionMessage.publisherId) &&
        Objects.equals(this.timestamp, jsonApplicationDescriptionMessage.timestamp) &&
        Objects.equals(this.description, jsonApplicationDescriptionMessage.description) &&
        Objects.equals(this.serverCapabilities, jsonApplicationDescriptionMessage.serverCapabilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, timestamp, description, serverCapabilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonApplicationDescriptionMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    serverCapabilities: ").append(toIndentedString(serverCapabilities)).append("\n");
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


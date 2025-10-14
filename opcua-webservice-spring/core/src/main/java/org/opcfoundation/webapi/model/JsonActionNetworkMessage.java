package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification]().
 */

@Schema(name = "JsonActionNetworkMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonActionNetworkMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable String responseAddress;

  private @Nullable byte[] correlationData;

  private @Nullable String requestorId;

  private Double timeoutHint = 0d;

  @Valid
  private List<Object> messages = new ArrayList<>();

  public JsonActionNetworkMessage messageId(String messageId) {
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

  public JsonActionNetworkMessage messageType(String messageType) {
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

  public JsonActionNetworkMessage publisherId(String publisherId) {
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

  public JsonActionNetworkMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonActionNetworkMessage responseAddress(String responseAddress) {
    this.responseAddress = responseAddress;
    return this;
  }

  /**
   * Get responseAddress
   * @return responseAddress
   */
  
  @Schema(name = "ResponseAddress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResponseAddress")
  public String getResponseAddress() {
    return responseAddress;
  }

  public void setResponseAddress(String responseAddress) {
    this.responseAddress = responseAddress;
  }

  public JsonActionNetworkMessage correlationData(byte[] correlationData) {
    this.correlationData = correlationData;
    return this;
  }

  /**
   * Get correlationData
   * @return correlationData
   */
  
  @Schema(name = "CorrelationData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("CorrelationData")
  public byte[] getCorrelationData() {
    return correlationData;
  }

  public void setCorrelationData(byte[] correlationData) {
    this.correlationData = correlationData;
  }

  public JsonActionNetworkMessage requestorId(String requestorId) {
    this.requestorId = requestorId;
    return this;
  }

  /**
   * Get requestorId
   * @return requestorId
   */
  
  @Schema(name = "RequestorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestorId")
  public String getRequestorId() {
    return requestorId;
  }

  public void setRequestorId(String requestorId) {
    this.requestorId = requestorId;
  }

  public JsonActionNetworkMessage timeoutHint(Double timeoutHint) {
    this.timeoutHint = timeoutHint;
    return this;
  }

  /**
   * Get timeoutHint
   * @return timeoutHint
   */
  
  @Schema(name = "TimeoutHint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TimeoutHint")
  public Double getTimeoutHint() {
    return timeoutHint;
  }

  public void setTimeoutHint(Double timeoutHint) {
    this.timeoutHint = timeoutHint;
  }

  public JsonActionNetworkMessage messages(List<Object> messages) {
    this.messages = messages;
    return this;
  }

  public JsonActionNetworkMessage addMessagesItem(Object messagesItem) {
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    this.messages.add(messagesItem);
    return this;
  }

  /**
   * Get messages
   * @return messages
   */
  
  @Schema(name = "Messages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Messages")
  public List<Object> getMessages() {
    return messages;
  }

  public void setMessages(List<Object> messages) {
    this.messages = messages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonActionNetworkMessage jsonActionNetworkMessage = (JsonActionNetworkMessage) o;
    return Objects.equals(this.messageId, jsonActionNetworkMessage.messageId) &&
        Objects.equals(this.messageType, jsonActionNetworkMessage.messageType) &&
        Objects.equals(this.publisherId, jsonActionNetworkMessage.publisherId) &&
        Objects.equals(this.timestamp, jsonActionNetworkMessage.timestamp) &&
        Objects.equals(this.responseAddress, jsonActionNetworkMessage.responseAddress) &&
        Arrays.equals(this.correlationData, jsonActionNetworkMessage.correlationData) &&
        Objects.equals(this.requestorId, jsonActionNetworkMessage.requestorId) &&
        Objects.equals(this.timeoutHint, jsonActionNetworkMessage.timeoutHint) &&
        Objects.equals(this.messages, jsonActionNetworkMessage.messages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, timestamp, responseAddress, Arrays.hashCode(correlationData), requestorId, timeoutHint, messages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonActionNetworkMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    responseAddress: ").append(toIndentedString(responseAddress)).append("\n");
    sb.append("    correlationData: ").append(toIndentedString(correlationData)).append("\n");
    sb.append("    requestorId: ").append(toIndentedString(requestorId)).append("\n");
    sb.append("    timeoutHint: ").append(toIndentedString(timeoutHint)).append("\n");
    sb.append("    messages: ").append(toIndentedString(messages)).append("\n");
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


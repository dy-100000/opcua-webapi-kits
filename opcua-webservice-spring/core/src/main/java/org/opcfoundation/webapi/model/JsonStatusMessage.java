package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification]().
 */

@Schema(name = "JsonStatusMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonStatusMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Boolean isCyclic = false;

  private @Nullable Integer status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime nextReportTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  public JsonStatusMessage messageId(String messageId) {
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

  public JsonStatusMessage messageType(String messageType) {
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

  public JsonStatusMessage publisherId(String publisherId) {
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

  public JsonStatusMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonStatusMessage isCyclic(Boolean isCyclic) {
    this.isCyclic = isCyclic;
    return this;
  }

  /**
   * Get isCyclic
   * @return isCyclic
   */
  
  @Schema(name = "IsCyclic", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IsCyclic")
  public Boolean getIsCyclic() {
    return isCyclic;
  }

  public void setIsCyclic(Boolean isCyclic) {
    this.isCyclic = isCyclic;
  }

  public JsonStatusMessage status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.1).
   * @return status
   */
  
  @Schema(name = "Status", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.1).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public JsonStatusMessage nextReportTime(OffsetDateTime nextReportTime) {
    this.nextReportTime = nextReportTime;
    return this;
  }

  /**
   * Get nextReportTime
   * @return nextReportTime
   */
  @Valid 
  @Schema(name = "NextReportTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NextReportTime")
  public OffsetDateTime getNextReportTime() {
    return nextReportTime;
  }

  public void setNextReportTime(OffsetDateTime nextReportTime) {
    this.nextReportTime = nextReportTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonStatusMessage jsonStatusMessage = (JsonStatusMessage) o;
    return Objects.equals(this.messageId, jsonStatusMessage.messageId) &&
        Objects.equals(this.messageType, jsonStatusMessage.messageType) &&
        Objects.equals(this.publisherId, jsonStatusMessage.publisherId) &&
        Objects.equals(this.timestamp, jsonStatusMessage.timestamp) &&
        Objects.equals(this.isCyclic, jsonStatusMessage.isCyclic) &&
        Objects.equals(this.status, jsonStatusMessage.status) &&
        Objects.equals(this.nextReportTime, jsonStatusMessage.nextReportTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, timestamp, isCyclic, status, nextReportTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonStatusMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    isCyclic: ").append(toIndentedString(isCyclic)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    nextReportTime: ").append(toIndentedString(nextReportTime)).append("\n");
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


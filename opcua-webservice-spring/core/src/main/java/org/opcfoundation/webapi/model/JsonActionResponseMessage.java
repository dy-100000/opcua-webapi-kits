package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification]().
 */

@Schema(name = "JsonActionResponseMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonActionResponseMessage {

  private Integer dataSetWriterId = 0;

  private Integer actionTargetId = 0;

  private @Nullable String dataSetWriterName;

  private @Nullable String writerGroupName;

  private @Nullable ConfigurationVersionDataType metaDataVersion;

  private Long minorVersion = 0l;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable StatusCode status;

  private @Nullable String messageType;

  private Integer requestId = 0;

  private @Nullable Integer actionState;

  private @Nullable Object payload;

  public JsonActionResponseMessage dataSetWriterId(Integer dataSetWriterId) {
    this.dataSetWriterId = dataSetWriterId;
    return this;
  }

  /**
   * Get dataSetWriterId
   * minimum: 0
   * maximum: 65535
   * @return dataSetWriterId
   */
  @Min(0) @Max(65535) 
  @Schema(name = "DataSetWriterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetWriterId")
  public Integer getDataSetWriterId() {
    return dataSetWriterId;
  }

  public void setDataSetWriterId(Integer dataSetWriterId) {
    this.dataSetWriterId = dataSetWriterId;
  }

  public JsonActionResponseMessage actionTargetId(Integer actionTargetId) {
    this.actionTargetId = actionTargetId;
    return this;
  }

  /**
   * Get actionTargetId
   * minimum: 0
   * maximum: 65535
   * @return actionTargetId
   */
  @Min(0) @Max(65535) 
  @Schema(name = "ActionTargetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ActionTargetId")
  public Integer getActionTargetId() {
    return actionTargetId;
  }

  public void setActionTargetId(Integer actionTargetId) {
    this.actionTargetId = actionTargetId;
  }

  public JsonActionResponseMessage dataSetWriterName(String dataSetWriterName) {
    this.dataSetWriterName = dataSetWriterName;
    return this;
  }

  /**
   * Get dataSetWriterName
   * @return dataSetWriterName
   */
  
  @Schema(name = "DataSetWriterName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetWriterName")
  public String getDataSetWriterName() {
    return dataSetWriterName;
  }

  public void setDataSetWriterName(String dataSetWriterName) {
    this.dataSetWriterName = dataSetWriterName;
  }

  public JsonActionResponseMessage writerGroupName(String writerGroupName) {
    this.writerGroupName = writerGroupName;
    return this;
  }

  /**
   * Get writerGroupName
   * @return writerGroupName
   */
  
  @Schema(name = "WriterGroupName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("WriterGroupName")
  public String getWriterGroupName() {
    return writerGroupName;
  }

  public void setWriterGroupName(String writerGroupName) {
    this.writerGroupName = writerGroupName;
  }

  public JsonActionResponseMessage metaDataVersion(ConfigurationVersionDataType metaDataVersion) {
    this.metaDataVersion = metaDataVersion;
    return this;
  }

  /**
   * Get metaDataVersion
   * @return metaDataVersion
   */
  @Valid 
  @Schema(name = "MetaDataVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MetaDataVersion")
  public ConfigurationVersionDataType getMetaDataVersion() {
    return metaDataVersion;
  }

  public void setMetaDataVersion(ConfigurationVersionDataType metaDataVersion) {
    this.metaDataVersion = metaDataVersion;
  }

  public JsonActionResponseMessage minorVersion(Long minorVersion) {
    this.minorVersion = minorVersion;
    return this;
  }

  /**
   * Get minorVersion
   * minimum: 0
   * maximum: 4294967295
   * @return minorVersion
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MinorVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MinorVersion")
  public Long getMinorVersion() {
    return minorVersion;
  }

  public void setMinorVersion(Long minorVersion) {
    this.minorVersion = minorVersion;
  }

  public JsonActionResponseMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonActionResponseMessage status(StatusCode status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @Valid 
  @Schema(name = "Status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Status")
  public StatusCode getStatus() {
    return status;
  }

  public void setStatus(StatusCode status) {
    this.status = status;
  }

  public JsonActionResponseMessage messageType(String messageType) {
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

  public JsonActionResponseMessage requestId(Integer requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Get requestId
   * minimum: 0
   * maximum: 65535
   * @return requestId
   */
  @Min(0) @Max(65535) 
  @Schema(name = "RequestId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestId")
  public Integer getRequestId() {
    return requestId;
  }

  public void setRequestId(Integer requestId) {
    this.requestId = requestId;
  }

  public JsonActionResponseMessage actionState(Integer actionState) {
    this.actionState = actionState;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.11/#6.2.11.2.1).
   * @return actionState
   */
  
  @Schema(name = "ActionState", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.11/#6.2.11.2.1).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ActionState")
  public Integer getActionState() {
    return actionState;
  }

  public void setActionState(Integer actionState) {
    this.actionState = actionState;
  }

  public JsonActionResponseMessage payload(Object payload) {
    this.payload = payload;
    return this;
  }

  /**
   * Get payload
   * @return payload
   */
  
  @Schema(name = "Payload", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Payload")
  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonActionResponseMessage jsonActionResponseMessage = (JsonActionResponseMessage) o;
    return Objects.equals(this.dataSetWriterId, jsonActionResponseMessage.dataSetWriterId) &&
        Objects.equals(this.actionTargetId, jsonActionResponseMessage.actionTargetId) &&
        Objects.equals(this.dataSetWriterName, jsonActionResponseMessage.dataSetWriterName) &&
        Objects.equals(this.writerGroupName, jsonActionResponseMessage.writerGroupName) &&
        Objects.equals(this.metaDataVersion, jsonActionResponseMessage.metaDataVersion) &&
        Objects.equals(this.minorVersion, jsonActionResponseMessage.minorVersion) &&
        Objects.equals(this.timestamp, jsonActionResponseMessage.timestamp) &&
        Objects.equals(this.status, jsonActionResponseMessage.status) &&
        Objects.equals(this.messageType, jsonActionResponseMessage.messageType) &&
        Objects.equals(this.requestId, jsonActionResponseMessage.requestId) &&
        Objects.equals(this.actionState, jsonActionResponseMessage.actionState) &&
        Objects.equals(this.payload, jsonActionResponseMessage.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataSetWriterId, actionTargetId, dataSetWriterName, writerGroupName, metaDataVersion, minorVersion, timestamp, status, messageType, requestId, actionState, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonActionResponseMessage {\n");
    sb.append("    dataSetWriterId: ").append(toIndentedString(dataSetWriterId)).append("\n");
    sb.append("    actionTargetId: ").append(toIndentedString(actionTargetId)).append("\n");
    sb.append("    dataSetWriterName: ").append(toIndentedString(dataSetWriterName)).append("\n");
    sb.append("    writerGroupName: ").append(toIndentedString(writerGroupName)).append("\n");
    sb.append("    metaDataVersion: ").append(toIndentedString(metaDataVersion)).append("\n");
    sb.append("    minorVersion: ").append(toIndentedString(minorVersion)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    actionState: ").append(toIndentedString(actionState)).append("\n");
    sb.append("    payload: ").append(toIndentedString(payload)).append("\n");
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


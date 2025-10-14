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

@Schema(name = "JsonDataSetMetaDataMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonDataSetMetaDataMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  private Integer dataSetWriterId = 0;

  private @Nullable String writerGroupName;

  private @Nullable String dataSetWriterName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable DataSetMetaDataType metaData;

  public JsonDataSetMetaDataMessage messageId(String messageId) {
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

  public JsonDataSetMetaDataMessage messageType(String messageType) {
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

  public JsonDataSetMetaDataMessage publisherId(String publisherId) {
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

  public JsonDataSetMetaDataMessage dataSetWriterId(Integer dataSetWriterId) {
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

  public JsonDataSetMetaDataMessage writerGroupName(String writerGroupName) {
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

  public JsonDataSetMetaDataMessage dataSetWriterName(String dataSetWriterName) {
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

  public JsonDataSetMetaDataMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonDataSetMetaDataMessage metaData(DataSetMetaDataType metaData) {
    this.metaData = metaData;
    return this;
  }

  /**
   * Get metaData
   * @return metaData
   */
  @Valid 
  @Schema(name = "MetaData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MetaData")
  public DataSetMetaDataType getMetaData() {
    return metaData;
  }

  public void setMetaData(DataSetMetaDataType metaData) {
    this.metaData = metaData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonDataSetMetaDataMessage jsonDataSetMetaDataMessage = (JsonDataSetMetaDataMessage) o;
    return Objects.equals(this.messageId, jsonDataSetMetaDataMessage.messageId) &&
        Objects.equals(this.messageType, jsonDataSetMetaDataMessage.messageType) &&
        Objects.equals(this.publisherId, jsonDataSetMetaDataMessage.publisherId) &&
        Objects.equals(this.dataSetWriterId, jsonDataSetMetaDataMessage.dataSetWriterId) &&
        Objects.equals(this.writerGroupName, jsonDataSetMetaDataMessage.writerGroupName) &&
        Objects.equals(this.dataSetWriterName, jsonDataSetMetaDataMessage.dataSetWriterName) &&
        Objects.equals(this.timestamp, jsonDataSetMetaDataMessage.timestamp) &&
        Objects.equals(this.metaData, jsonDataSetMetaDataMessage.metaData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, dataSetWriterId, writerGroupName, dataSetWriterName, timestamp, metaData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonDataSetMetaDataMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    dataSetWriterId: ").append(toIndentedString(dataSetWriterId)).append("\n");
    sb.append("    writerGroupName: ").append(toIndentedString(writerGroupName)).append("\n");
    sb.append("    dataSetWriterName: ").append(toIndentedString(dataSetWriterName)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    metaData: ").append(toIndentedString(metaData)).append("\n");
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


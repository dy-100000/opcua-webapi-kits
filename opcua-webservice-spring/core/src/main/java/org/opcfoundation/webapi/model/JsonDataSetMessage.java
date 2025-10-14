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

@Schema(name = "JsonDataSetMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonDataSetMessage {

  private Integer dataSetWriterId = 0;

  private @Nullable String dataSetWriterName;

  private @Nullable String publisherId;

  private @Nullable String writerGroupName;

  private Long sequenceNumber = 0l;

  private @Nullable ConfigurationVersionDataType metaDataVersion;

  private Long minorVersion = 0l;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable StatusCode status;

  private @Nullable String messageType;

  private @Nullable Object payload;

  public JsonDataSetMessage dataSetWriterId(Integer dataSetWriterId) {
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

  public JsonDataSetMessage dataSetWriterName(String dataSetWriterName) {
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

  public JsonDataSetMessage publisherId(String publisherId) {
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

  public JsonDataSetMessage writerGroupName(String writerGroupName) {
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

  public JsonDataSetMessage sequenceNumber(Long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
    return this;
  }

  /**
   * Get sequenceNumber
   * minimum: 0
   * maximum: 4294967295
   * @return sequenceNumber
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "SequenceNumber", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SequenceNumber")
  public Long getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(Long sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public JsonDataSetMessage metaDataVersion(ConfigurationVersionDataType metaDataVersion) {
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

  public JsonDataSetMessage minorVersion(Long minorVersion) {
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

  public JsonDataSetMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonDataSetMessage status(StatusCode status) {
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

  public JsonDataSetMessage messageType(String messageType) {
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

  public JsonDataSetMessage payload(Object payload) {
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
    JsonDataSetMessage jsonDataSetMessage = (JsonDataSetMessage) o;
    return Objects.equals(this.dataSetWriterId, jsonDataSetMessage.dataSetWriterId) &&
        Objects.equals(this.dataSetWriterName, jsonDataSetMessage.dataSetWriterName) &&
        Objects.equals(this.publisherId, jsonDataSetMessage.publisherId) &&
        Objects.equals(this.writerGroupName, jsonDataSetMessage.writerGroupName) &&
        Objects.equals(this.sequenceNumber, jsonDataSetMessage.sequenceNumber) &&
        Objects.equals(this.metaDataVersion, jsonDataSetMessage.metaDataVersion) &&
        Objects.equals(this.minorVersion, jsonDataSetMessage.minorVersion) &&
        Objects.equals(this.timestamp, jsonDataSetMessage.timestamp) &&
        Objects.equals(this.status, jsonDataSetMessage.status) &&
        Objects.equals(this.messageType, jsonDataSetMessage.messageType) &&
        Objects.equals(this.payload, jsonDataSetMessage.payload);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataSetWriterId, dataSetWriterName, publisherId, writerGroupName, sequenceNumber, metaDataVersion, minorVersion, timestamp, status, messageType, payload);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonDataSetMessage {\n");
    sb.append("    dataSetWriterId: ").append(toIndentedString(dataSetWriterId)).append("\n");
    sb.append("    dataSetWriterName: ").append(toIndentedString(dataSetWriterName)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    writerGroupName: ").append(toIndentedString(writerGroupName)).append("\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    metaDataVersion: ").append(toIndentedString(metaDataVersion)).append("\n");
    sb.append("    minorVersion: ").append(toIndentedString(minorVersion)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
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


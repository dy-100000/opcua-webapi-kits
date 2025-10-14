package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification]().
 */

@Schema(name = "JsonActionMetaDataMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonActionMetaDataMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  private Integer dataSetWriterId = 0;

  private @Nullable String dataSetWriterName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  @Valid
  private List<@Valid ActionTargetDataType> actionTargets = new ArrayList<>();

  private @Nullable DataSetMetaDataType request;

  private @Nullable DataSetMetaDataType response;

  @Valid
  private List<@Valid ActionMethodDataType> actionMethods = new ArrayList<>();

  public JsonActionMetaDataMessage messageId(String messageId) {
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

  public JsonActionMetaDataMessage messageType(String messageType) {
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

  public JsonActionMetaDataMessage publisherId(String publisherId) {
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

  public JsonActionMetaDataMessage dataSetWriterId(Integer dataSetWriterId) {
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

  public JsonActionMetaDataMessage dataSetWriterName(String dataSetWriterName) {
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

  public JsonActionMetaDataMessage timestamp(OffsetDateTime timestamp) {
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

  public JsonActionMetaDataMessage actionTargets(List<@Valid ActionTargetDataType> actionTargets) {
    this.actionTargets = actionTargets;
    return this;
  }

  public JsonActionMetaDataMessage addActionTargetsItem(ActionTargetDataType actionTargetsItem) {
    if (this.actionTargets == null) {
      this.actionTargets = new ArrayList<>();
    }
    this.actionTargets.add(actionTargetsItem);
    return this;
  }

  /**
   * Get actionTargets
   * @return actionTargets
   */
  @Valid 
  @Schema(name = "ActionTargets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ActionTargets")
  public List<@Valid ActionTargetDataType> getActionTargets() {
    return actionTargets;
  }

  public void setActionTargets(List<@Valid ActionTargetDataType> actionTargets) {
    this.actionTargets = actionTargets;
  }

  public JsonActionMetaDataMessage request(DataSetMetaDataType request) {
    this.request = request;
    return this;
  }

  /**
   * Get request
   * @return request
   */
  @Valid 
  @Schema(name = "Request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Request")
  public DataSetMetaDataType getRequest() {
    return request;
  }

  public void setRequest(DataSetMetaDataType request) {
    this.request = request;
  }

  public JsonActionMetaDataMessage response(DataSetMetaDataType response) {
    this.response = response;
    return this;
  }

  /**
   * Get response
   * @return response
   */
  @Valid 
  @Schema(name = "Response", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Response")
  public DataSetMetaDataType getResponse() {
    return response;
  }

  public void setResponse(DataSetMetaDataType response) {
    this.response = response;
  }

  public JsonActionMetaDataMessage actionMethods(List<@Valid ActionMethodDataType> actionMethods) {
    this.actionMethods = actionMethods;
    return this;
  }

  public JsonActionMetaDataMessage addActionMethodsItem(ActionMethodDataType actionMethodsItem) {
    if (this.actionMethods == null) {
      this.actionMethods = new ArrayList<>();
    }
    this.actionMethods.add(actionMethodsItem);
    return this;
  }

  /**
   * Get actionMethods
   * @return actionMethods
   */
  @Valid 
  @Schema(name = "ActionMethods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ActionMethods")
  public List<@Valid ActionMethodDataType> getActionMethods() {
    return actionMethods;
  }

  public void setActionMethods(List<@Valid ActionMethodDataType> actionMethods) {
    this.actionMethods = actionMethods;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonActionMetaDataMessage jsonActionMetaDataMessage = (JsonActionMetaDataMessage) o;
    return Objects.equals(this.messageId, jsonActionMetaDataMessage.messageId) &&
        Objects.equals(this.messageType, jsonActionMetaDataMessage.messageType) &&
        Objects.equals(this.publisherId, jsonActionMetaDataMessage.publisherId) &&
        Objects.equals(this.dataSetWriterId, jsonActionMetaDataMessage.dataSetWriterId) &&
        Objects.equals(this.dataSetWriterName, jsonActionMetaDataMessage.dataSetWriterName) &&
        Objects.equals(this.timestamp, jsonActionMetaDataMessage.timestamp) &&
        Objects.equals(this.actionTargets, jsonActionMetaDataMessage.actionTargets) &&
        Objects.equals(this.request, jsonActionMetaDataMessage.request) &&
        Objects.equals(this.response, jsonActionMetaDataMessage.response) &&
        Objects.equals(this.actionMethods, jsonActionMetaDataMessage.actionMethods);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, dataSetWriterId, dataSetWriterName, timestamp, actionTargets, request, response, actionMethods);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonActionMetaDataMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    dataSetWriterId: ").append(toIndentedString(dataSetWriterId)).append("\n");
    sb.append("    dataSetWriterName: ").append(toIndentedString(dataSetWriterName)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    actionTargets: ").append(toIndentedString(actionTargets)).append("\n");
    sb.append("    request: ").append(toIndentedString(request)).append("\n");
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("    actionMethods: ").append(toIndentedString(actionMethods)).append("\n");
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


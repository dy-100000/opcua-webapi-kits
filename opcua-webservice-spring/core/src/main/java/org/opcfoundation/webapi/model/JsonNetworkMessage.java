package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification]().
 */

@Schema(name = "JsonNetworkMessage", description = "[Link to specification]().")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonNetworkMessage {

  private @Nullable String messageId;

  private @Nullable String messageType;

  private @Nullable String publisherId;

  private @Nullable String writerGroupName;

  private @Nullable String dataSetClassId;

  private @Nullable Object messages = null;

  public JsonNetworkMessage messageId(String messageId) {
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

  public JsonNetworkMessage messageType(String messageType) {
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

  public JsonNetworkMessage publisherId(String publisherId) {
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

  public JsonNetworkMessage writerGroupName(String writerGroupName) {
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

  public JsonNetworkMessage dataSetClassId(String dataSetClassId) {
    this.dataSetClassId = dataSetClassId;
    return this;
  }

  /**
   * Get dataSetClassId
   * @return dataSetClassId
   */
  
  @Schema(name = "DataSetClassId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetClassId")
  public String getDataSetClassId() {
    return dataSetClassId;
  }

  public void setDataSetClassId(String dataSetClassId) {
    this.dataSetClassId = dataSetClassId;
  }

  public JsonNetworkMessage messages(Object messages) {
    this.messages = messages;
    return this;
  }

  /**
   * Get messages
   * @return messages
   */
  
  @Schema(name = "Messages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Messages")
  public Object getMessages() {
    return messages;
  }

  public void setMessages(Object messages) {
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
    JsonNetworkMessage jsonNetworkMessage = (JsonNetworkMessage) o;
    return Objects.equals(this.messageId, jsonNetworkMessage.messageId) &&
        Objects.equals(this.messageType, jsonNetworkMessage.messageType) &&
        Objects.equals(this.publisherId, jsonNetworkMessage.publisherId) &&
        Objects.equals(this.writerGroupName, jsonNetworkMessage.writerGroupName) &&
        Objects.equals(this.dataSetClassId, jsonNetworkMessage.dataSetClassId) &&
        Objects.equals(this.messages, jsonNetworkMessage.messages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageId, messageType, publisherId, writerGroupName, dataSetClassId, messages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonNetworkMessage {\n");
    sb.append("    messageId: ").append(toIndentedString(messageId)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    writerGroupName: ").append(toIndentedString(writerGroupName)).append("\n");
    sb.append("    dataSetClassId: ").append(toIndentedString(dataSetClassId)).append("\n");
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


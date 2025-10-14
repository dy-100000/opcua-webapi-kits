package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.26).
 */

@Schema(name = "NotificationMessage", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.26).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class NotificationMessage {

  private Long sequenceNumber = 0l;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime publishTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  @Valid
  private List<@Valid ExtensionObject> notificationData = new ArrayList<>();

  public NotificationMessage sequenceNumber(Long sequenceNumber) {
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

  public NotificationMessage publishTime(OffsetDateTime publishTime) {
    this.publishTime = publishTime;
    return this;
  }

  /**
   * Get publishTime
   * @return publishTime
   */
  @Valid 
  @Schema(name = "PublishTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PublishTime")
  public OffsetDateTime getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(OffsetDateTime publishTime) {
    this.publishTime = publishTime;
  }

  public NotificationMessage notificationData(List<@Valid ExtensionObject> notificationData) {
    this.notificationData = notificationData;
    return this;
  }

  public NotificationMessage addNotificationDataItem(ExtensionObject notificationDataItem) {
    if (this.notificationData == null) {
      this.notificationData = new ArrayList<>();
    }
    this.notificationData.add(notificationDataItem);
    return this;
  }

  /**
   * Get notificationData
   * @return notificationData
   */
  @Valid 
  @Schema(name = "NotificationData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NotificationData")
  public List<@Valid ExtensionObject> getNotificationData() {
    return notificationData;
  }

  public void setNotificationData(List<@Valid ExtensionObject> notificationData) {
    this.notificationData = notificationData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationMessage notificationMessage = (NotificationMessage) o;
    return Objects.equals(this.sequenceNumber, notificationMessage.sequenceNumber) &&
        Objects.equals(this.publishTime, notificationMessage.publishTime) &&
        Objects.equals(this.notificationData, notificationMessage.notificationData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sequenceNumber, publishTime, notificationData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationMessage {\n");
    sb.append("    sequenceNumber: ").append(toIndentedString(sequenceNumber)).append("\n");
    sb.append("    publishTime: ").append(toIndentedString(publishTime)).append("\n");
    sb.append("    notificationData: ").append(toIndentedString(notificationData)).append("\n");
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


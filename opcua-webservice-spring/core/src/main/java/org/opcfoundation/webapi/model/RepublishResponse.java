package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2).
 */

@Schema(name = "RepublishResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RepublishResponse {

  private @Nullable ResponseHeader responseHeader;

  private @Nullable NotificationMessage notificationMessage;

  public RepublishResponse responseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
    return this;
  }

  /**
   * Get responseHeader
   * @return responseHeader
   */
  @Valid 
  @Schema(name = "ResponseHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResponseHeader")
  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public void setResponseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
  }

  public RepublishResponse notificationMessage(NotificationMessage notificationMessage) {
    this.notificationMessage = notificationMessage;
    return this;
  }

  /**
   * Get notificationMessage
   * @return notificationMessage
   */
  @Valid 
  @Schema(name = "NotificationMessage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NotificationMessage")
  public NotificationMessage getNotificationMessage() {
    return notificationMessage;
  }

  public void setNotificationMessage(NotificationMessage notificationMessage) {
    this.notificationMessage = notificationMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RepublishResponse republishResponse = (RepublishResponse) o;
    return Objects.equals(this.responseHeader, republishResponse.responseHeader) &&
        Objects.equals(this.notificationMessage, republishResponse.notificationMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, notificationMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RepublishResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    notificationMessage: ").append(toIndentedString(notificationMessage)).append("\n");
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


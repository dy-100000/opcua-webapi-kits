package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2).
 */

@Schema(name = "MonitoredItemNotification", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class MonitoredItemNotification {

  private Long clientHandle = 0l;

  private @Nullable DataValue value;

  public MonitoredItemNotification clientHandle(Long clientHandle) {
    this.clientHandle = clientHandle;
    return this;
  }

  /**
   * Get clientHandle
   * minimum: 0
   * maximum: 4294967295
   * @return clientHandle
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ClientHandle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientHandle")
  public Long getClientHandle() {
    return clientHandle;
  }

  public void setClientHandle(Long clientHandle) {
    this.clientHandle = clientHandle;
  }

  public MonitoredItemNotification value(DataValue value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "Value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Value")
  public DataValue getValue() {
    return value;
  }

  public void setValue(DataValue value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonitoredItemNotification monitoredItemNotification = (MonitoredItemNotification) o;
    return Objects.equals(this.clientHandle, monitoredItemNotification.clientHandle) &&
        Objects.equals(this.value, monitoredItemNotification.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientHandle, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoredItemNotification {\n");
    sb.append("    clientHandle: ").append(toIndentedString(clientHandle)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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


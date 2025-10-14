package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.2).
 */

@Schema(name = "JsonWriterGroupMessageDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonWriterGroupMessageDataType {

  private Long networkMessageContentMask = 0l;

  public JsonWriterGroupMessageDataType networkMessageContentMask(Long networkMessageContentMask) {
    this.networkMessageContentMask = networkMessageContentMask;
    return this;
  }

  /**
   * Get networkMessageContentMask
   * minimum: 0
   * maximum: 4294967295
   * @return networkMessageContentMask
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "NetworkMessageContentMask", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NetworkMessageContentMask")
  public Long getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  public void setNetworkMessageContentMask(Long networkMessageContentMask) {
    this.networkMessageContentMask = networkMessageContentMask;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonWriterGroupMessageDataType jsonWriterGroupMessageDataType = (JsonWriterGroupMessageDataType) o;
    return Objects.equals(this.networkMessageContentMask, jsonWriterGroupMessageDataType.networkMessageContentMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkMessageContentMask);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonWriterGroupMessageDataType {\n");
    sb.append("    networkMessageContentMask: ").append(toIndentedString(networkMessageContentMask)).append("\n");
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


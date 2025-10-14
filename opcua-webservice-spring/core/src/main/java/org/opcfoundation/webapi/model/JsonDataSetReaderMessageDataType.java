package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.4.3).
 */

@Schema(name = "JsonDataSetReaderMessageDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.4.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonDataSetReaderMessageDataType {

  private Long networkMessageContentMask = 0l;

  private Long dataSetMessageContentMask = 0l;

  public JsonDataSetReaderMessageDataType networkMessageContentMask(Long networkMessageContentMask) {
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

  public JsonDataSetReaderMessageDataType dataSetMessageContentMask(Long dataSetMessageContentMask) {
    this.dataSetMessageContentMask = dataSetMessageContentMask;
    return this;
  }

  /**
   * Get dataSetMessageContentMask
   * minimum: 0
   * maximum: 4294967295
   * @return dataSetMessageContentMask
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "DataSetMessageContentMask", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetMessageContentMask")
  public Long getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  public void setDataSetMessageContentMask(Long dataSetMessageContentMask) {
    this.dataSetMessageContentMask = dataSetMessageContentMask;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JsonDataSetReaderMessageDataType jsonDataSetReaderMessageDataType = (JsonDataSetReaderMessageDataType) o;
    return Objects.equals(this.networkMessageContentMask, jsonDataSetReaderMessageDataType.networkMessageContentMask) &&
        Objects.equals(this.dataSetMessageContentMask, jsonDataSetReaderMessageDataType.dataSetMessageContentMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkMessageContentMask, dataSetMessageContentMask);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonDataSetReaderMessageDataType {\n");
    sb.append("    networkMessageContentMask: ").append(toIndentedString(networkMessageContentMask)).append("\n");
    sb.append("    dataSetMessageContentMask: ").append(toIndentedString(dataSetMessageContentMask)).append("\n");
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


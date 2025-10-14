package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.2).
 */

@Schema(name = "JsonDataSetWriterMessageDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class JsonDataSetWriterMessageDataType {

  private Long dataSetMessageContentMask = 0l;

  public JsonDataSetWriterMessageDataType dataSetMessageContentMask(Long dataSetMessageContentMask) {
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
    JsonDataSetWriterMessageDataType jsonDataSetWriterMessageDataType = (JsonDataSetWriterMessageDataType) o;
    return Objects.equals(this.dataSetMessageContentMask, jsonDataSetWriterMessageDataType.dataSetMessageContentMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataSetMessageContentMask);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JsonDataSetWriterMessageDataType {\n");
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


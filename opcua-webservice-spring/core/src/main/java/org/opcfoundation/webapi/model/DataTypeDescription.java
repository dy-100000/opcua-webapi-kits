package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.32).
 */

@Schema(name = "DataTypeDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.32).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataTypeDescription {

  private @Nullable String dataTypeId;

  private @Nullable String name;

  public DataTypeDescription dataTypeId(String dataTypeId) {
    this.dataTypeId = dataTypeId;
    return this;
  }

  /**
   * Get dataTypeId
   * @return dataTypeId
   */
  
  @Schema(name = "DataTypeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataTypeId")
  public String getDataTypeId() {
    return dataTypeId;
  }

  public void setDataTypeId(String dataTypeId) {
    this.dataTypeId = dataTypeId;
  }

  public DataTypeDescription name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "Name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataTypeDescription dataTypeDescription = (DataTypeDescription) o;
    return Objects.equals(this.dataTypeId, dataTypeDescription.dataTypeId) &&
        Objects.equals(this.name, dataTypeDescription.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataTypeId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataTypeDescription {\n");
    sb.append("    dataTypeId: ").append(toIndentedString(dataTypeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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


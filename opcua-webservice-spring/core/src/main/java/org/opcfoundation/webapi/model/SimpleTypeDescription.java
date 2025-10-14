package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.35).
 */

@Schema(name = "SimpleTypeDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.35).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SimpleTypeDescription {

  private @Nullable String baseDataType;

  private Integer builtInType = 0;

  private @Nullable String dataTypeId;

  private @Nullable String name;

  public SimpleTypeDescription baseDataType(String baseDataType) {
    this.baseDataType = baseDataType;
    return this;
  }

  /**
   * Get baseDataType
   * @return baseDataType
   */
  
  @Schema(name = "BaseDataType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BaseDataType")
  public String getBaseDataType() {
    return baseDataType;
  }

  public void setBaseDataType(String baseDataType) {
    this.baseDataType = baseDataType;
  }

  public SimpleTypeDescription builtInType(Integer builtInType) {
    this.builtInType = builtInType;
    return this;
  }

  /**
   * Get builtInType
   * minimum: 0
   * maximum: 255
   * @return builtInType
   */
  @Min(0) @Max(255) 
  @Schema(name = "BuiltInType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BuiltInType")
  public Integer getBuiltInType() {
    return builtInType;
  }

  public void setBuiltInType(Integer builtInType) {
    this.builtInType = builtInType;
  }

  public SimpleTypeDescription dataTypeId(String dataTypeId) {
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

  public SimpleTypeDescription name(String name) {
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
    SimpleTypeDescription simpleTypeDescription = (SimpleTypeDescription) o;
    return Objects.equals(this.baseDataType, simpleTypeDescription.baseDataType) &&
        Objects.equals(this.builtInType, simpleTypeDescription.builtInType) &&
        Objects.equals(this.dataTypeId, simpleTypeDescription.dataTypeId) &&
        Objects.equals(this.name, simpleTypeDescription.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseDataType, builtInType, dataTypeId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimpleTypeDescription {\n");
    sb.append("    baseDataType: ").append(toIndentedString(baseDataType)).append("\n");
    sb.append("    builtInType: ").append(toIndentedString(builtInType)).append("\n");
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


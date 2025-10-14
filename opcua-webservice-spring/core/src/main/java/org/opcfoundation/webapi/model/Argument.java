package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.1).
 */

@Schema(name = "Argument", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Argument {

  private @Nullable String name;

  private @Nullable String dataType;

  private Integer valueRank = 0;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> arrayDimensions = new ArrayList<>();

  private @Nullable LocalizedText description;

  public Argument name(String name) {
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

  public Argument dataType(String dataType) {
    this.dataType = dataType;
    return this;
  }

  /**
   * Get dataType
   * @return dataType
   */
  
  @Schema(name = "DataType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataType")
  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public Argument valueRank(Integer valueRank) {
    this.valueRank = valueRank;
    return this;
  }

  /**
   * Get valueRank
   * @return valueRank
   */
  
  @Schema(name = "ValueRank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ValueRank")
  public Integer getValueRank() {
    return valueRank;
  }

  public void setValueRank(Integer valueRank) {
    this.valueRank = valueRank;
  }

  public Argument arrayDimensions(List<@Min(0L) @Max(4294967295L)Long> arrayDimensions) {
    this.arrayDimensions = arrayDimensions;
    return this;
  }

  public Argument addArrayDimensionsItem(Long arrayDimensionsItem) {
    if (this.arrayDimensions == null) {
      this.arrayDimensions = new ArrayList<>();
    }
    this.arrayDimensions.add(arrayDimensionsItem);
    return this;
  }

  /**
   * Get arrayDimensions
   * @return arrayDimensions
   */
  
  @Schema(name = "ArrayDimensions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ArrayDimensions")
  public List<@Min(0L) @Max(4294967295L)Long> getArrayDimensions() {
    return arrayDimensions;
  }

  public void setArrayDimensions(List<@Min(0L) @Max(4294967295L)Long> arrayDimensions) {
    this.arrayDimensions = arrayDimensions;
  }

  public Argument description(LocalizedText description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Valid 
  @Schema(name = "Description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Description")
  public LocalizedText getDescription() {
    return description;
  }

  public void setDescription(LocalizedText description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Argument argument = (Argument) o;
    return Objects.equals(this.name, argument.name) &&
        Objects.equals(this.dataType, argument.dataType) &&
        Objects.equals(this.valueRank, argument.valueRank) &&
        Objects.equals(this.arrayDimensions, argument.arrayDimensions) &&
        Objects.equals(this.description, argument.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dataType, valueRank, arrayDimensions, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Argument {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    valueRank: ").append(toIndentedString(valueRank)).append("\n");
    sb.append("    arrayDimensions: ").append(toIndentedString(arrayDimensions)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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


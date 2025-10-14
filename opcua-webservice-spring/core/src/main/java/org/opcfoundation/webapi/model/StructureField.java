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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.10).
 */

@Schema(name = "StructureField", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.10).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class StructureField {

  private @Nullable String name;

  private @Nullable LocalizedText description;

  private @Nullable String dataType;

  private Integer valueRank = 0;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> arrayDimensions = new ArrayList<>();

  private Long maxStringLength = 0l;

  private Boolean isOptional = false;

  public StructureField name(String name) {
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

  public StructureField description(LocalizedText description) {
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

  public StructureField dataType(String dataType) {
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

  public StructureField valueRank(Integer valueRank) {
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

  public StructureField arrayDimensions(List<@Min(0L) @Max(4294967295L)Long> arrayDimensions) {
    this.arrayDimensions = arrayDimensions;
    return this;
  }

  public StructureField addArrayDimensionsItem(Long arrayDimensionsItem) {
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

  public StructureField maxStringLength(Long maxStringLength) {
    this.maxStringLength = maxStringLength;
    return this;
  }

  /**
   * Get maxStringLength
   * minimum: 0
   * maximum: 4294967295
   * @return maxStringLength
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxStringLength", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxStringLength")
  public Long getMaxStringLength() {
    return maxStringLength;
  }

  public void setMaxStringLength(Long maxStringLength) {
    this.maxStringLength = maxStringLength;
  }

  public StructureField isOptional(Boolean isOptional) {
    this.isOptional = isOptional;
    return this;
  }

  /**
   * Get isOptional
   * @return isOptional
   */
  
  @Schema(name = "IsOptional", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IsOptional")
  public Boolean getIsOptional() {
    return isOptional;
  }

  public void setIsOptional(Boolean isOptional) {
    this.isOptional = isOptional;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StructureField structureField = (StructureField) o;
    return Objects.equals(this.name, structureField.name) &&
        Objects.equals(this.description, structureField.description) &&
        Objects.equals(this.dataType, structureField.dataType) &&
        Objects.equals(this.valueRank, structureField.valueRank) &&
        Objects.equals(this.arrayDimensions, structureField.arrayDimensions) &&
        Objects.equals(this.maxStringLength, structureField.maxStringLength) &&
        Objects.equals(this.isOptional, structureField.isOptional);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, dataType, valueRank, arrayDimensions, maxStringLength, isOptional);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StructureField {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    valueRank: ").append(toIndentedString(valueRank)).append("\n");
    sb.append("    arrayDimensions: ").append(toIndentedString(arrayDimensions)).append("\n");
    sb.append("    maxStringLength: ").append(toIndentedString(maxStringLength)).append("\n");
    sb.append("    isOptional: ").append(toIndentedString(isOptional)).append("\n");
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


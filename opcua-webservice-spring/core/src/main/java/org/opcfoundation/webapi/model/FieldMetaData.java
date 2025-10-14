package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.4).
 */

@Schema(name = "FieldMetaData", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class FieldMetaData {

  private @Nullable String name;

  private @Nullable LocalizedText description;

  private Integer fieldFlags = 0;

  private Integer builtInType = 0;

  private @Nullable String dataType;

  private Integer valueRank = 0;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> arrayDimensions = new ArrayList<>();

  private Long maxStringLength = 0l;

  private @Nullable UUID dataSetFieldId;

  @Valid
  private List<@Valid KeyValuePair> properties = new ArrayList<>();

  public FieldMetaData name(String name) {
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

  public FieldMetaData description(LocalizedText description) {
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

  public FieldMetaData fieldFlags(Integer fieldFlags) {
    this.fieldFlags = fieldFlags;
    return this;
  }

  /**
   * Get fieldFlags
   * minimum: 0
   * maximum: 65535
   * @return fieldFlags
   */
  @Min(0) @Max(65535) 
  @Schema(name = "FieldFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("FieldFlags")
  public Integer getFieldFlags() {
    return fieldFlags;
  }

  public void setFieldFlags(Integer fieldFlags) {
    this.fieldFlags = fieldFlags;
  }

  public FieldMetaData builtInType(Integer builtInType) {
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

  public FieldMetaData dataType(String dataType) {
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

  public FieldMetaData valueRank(Integer valueRank) {
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

  public FieldMetaData arrayDimensions(List<@Min(0L) @Max(4294967295L)Long> arrayDimensions) {
    this.arrayDimensions = arrayDimensions;
    return this;
  }

  public FieldMetaData addArrayDimensionsItem(Long arrayDimensionsItem) {
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

  public FieldMetaData maxStringLength(Long maxStringLength) {
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

  public FieldMetaData dataSetFieldId(UUID dataSetFieldId) {
    this.dataSetFieldId = dataSetFieldId;
    return this;
  }

  /**
   * Get dataSetFieldId
   * @return dataSetFieldId
   */
  @Valid 
  @Schema(name = "DataSetFieldId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetFieldId")
  public UUID getDataSetFieldId() {
    return dataSetFieldId;
  }

  public void setDataSetFieldId(UUID dataSetFieldId) {
    this.dataSetFieldId = dataSetFieldId;
  }

  public FieldMetaData properties(List<@Valid KeyValuePair> properties) {
    this.properties = properties;
    return this;
  }

  public FieldMetaData addPropertiesItem(KeyValuePair propertiesItem) {
    if (this.properties == null) {
      this.properties = new ArrayList<>();
    }
    this.properties.add(propertiesItem);
    return this;
  }

  /**
   * Get properties
   * @return properties
   */
  @Valid 
  @Schema(name = "Properties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Properties")
  public List<@Valid KeyValuePair> getProperties() {
    return properties;
  }

  public void setProperties(List<@Valid KeyValuePair> properties) {
    this.properties = properties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FieldMetaData fieldMetaData = (FieldMetaData) o;
    return Objects.equals(this.name, fieldMetaData.name) &&
        Objects.equals(this.description, fieldMetaData.description) &&
        Objects.equals(this.fieldFlags, fieldMetaData.fieldFlags) &&
        Objects.equals(this.builtInType, fieldMetaData.builtInType) &&
        Objects.equals(this.dataType, fieldMetaData.dataType) &&
        Objects.equals(this.valueRank, fieldMetaData.valueRank) &&
        Objects.equals(this.arrayDimensions, fieldMetaData.arrayDimensions) &&
        Objects.equals(this.maxStringLength, fieldMetaData.maxStringLength) &&
        Objects.equals(this.dataSetFieldId, fieldMetaData.dataSetFieldId) &&
        Objects.equals(this.properties, fieldMetaData.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, fieldFlags, builtInType, dataType, valueRank, arrayDimensions, maxStringLength, dataSetFieldId, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FieldMetaData {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    fieldFlags: ").append(toIndentedString(fieldFlags)).append("\n");
    sb.append("    builtInType: ").append(toIndentedString(builtInType)).append("\n");
    sb.append("    dataType: ").append(toIndentedString(dataType)).append("\n");
    sb.append("    valueRank: ").append(toIndentedString(valueRank)).append("\n");
    sb.append("    arrayDimensions: ").append(toIndentedString(arrayDimensions)).append("\n");
    sb.append("    maxStringLength: ").append(toIndentedString(maxStringLength)).append("\n");
    sb.append("    dataSetFieldId: ").append(toIndentedString(dataSetFieldId)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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


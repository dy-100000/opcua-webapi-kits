package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.3).
 */

@Schema(name = "DataSetMetaDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataSetMetaDataType {

  private @Nullable String name;

  private @Nullable LocalizedText description;

  @Valid
  private List<@Valid FieldMetaData> fields = new ArrayList<>();

  private @Nullable UUID dataSetClassId;

  private @Nullable ConfigurationVersionDataType configurationVersion;

  @Valid
  private List<String> namespaces = new ArrayList<>();

  @Valid
  private List<@Valid StructureDescription> structureDataTypes = new ArrayList<>();

  @Valid
  private List<@Valid EnumDescription> enumDataTypes = new ArrayList<>();

  @Valid
  private List<@Valid SimpleTypeDescription> simpleDataTypes = new ArrayList<>();

  public DataSetMetaDataType name(String name) {
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

  public DataSetMetaDataType description(LocalizedText description) {
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

  public DataSetMetaDataType fields(List<@Valid FieldMetaData> fields) {
    this.fields = fields;
    return this;
  }

  public DataSetMetaDataType addFieldsItem(FieldMetaData fieldsItem) {
    if (this.fields == null) {
      this.fields = new ArrayList<>();
    }
    this.fields.add(fieldsItem);
    return this;
  }

  /**
   * Get fields
   * @return fields
   */
  @Valid 
  @Schema(name = "Fields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Fields")
  public List<@Valid FieldMetaData> getFields() {
    return fields;
  }

  public void setFields(List<@Valid FieldMetaData> fields) {
    this.fields = fields;
  }

  public DataSetMetaDataType dataSetClassId(UUID dataSetClassId) {
    this.dataSetClassId = dataSetClassId;
    return this;
  }

  /**
   * Get dataSetClassId
   * @return dataSetClassId
   */
  @Valid 
  @Schema(name = "DataSetClassId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetClassId")
  public UUID getDataSetClassId() {
    return dataSetClassId;
  }

  public void setDataSetClassId(UUID dataSetClassId) {
    this.dataSetClassId = dataSetClassId;
  }

  public DataSetMetaDataType configurationVersion(ConfigurationVersionDataType configurationVersion) {
    this.configurationVersion = configurationVersion;
    return this;
  }

  /**
   * Get configurationVersion
   * @return configurationVersion
   */
  @Valid 
  @Schema(name = "ConfigurationVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ConfigurationVersion")
  public ConfigurationVersionDataType getConfigurationVersion() {
    return configurationVersion;
  }

  public void setConfigurationVersion(ConfigurationVersionDataType configurationVersion) {
    this.configurationVersion = configurationVersion;
  }

  public DataSetMetaDataType namespaces(List<String> namespaces) {
    this.namespaces = namespaces;
    return this;
  }

  public DataSetMetaDataType addNamespacesItem(String namespacesItem) {
    if (this.namespaces == null) {
      this.namespaces = new ArrayList<>();
    }
    this.namespaces.add(namespacesItem);
    return this;
  }

  /**
   * Get namespaces
   * @return namespaces
   */
  
  @Schema(name = "Namespaces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Namespaces")
  public List<String> getNamespaces() {
    return namespaces;
  }

  public void setNamespaces(List<String> namespaces) {
    this.namespaces = namespaces;
  }

  public DataSetMetaDataType structureDataTypes(List<@Valid StructureDescription> structureDataTypes) {
    this.structureDataTypes = structureDataTypes;
    return this;
  }

  public DataSetMetaDataType addStructureDataTypesItem(StructureDescription structureDataTypesItem) {
    if (this.structureDataTypes == null) {
      this.structureDataTypes = new ArrayList<>();
    }
    this.structureDataTypes.add(structureDataTypesItem);
    return this;
  }

  /**
   * Get structureDataTypes
   * @return structureDataTypes
   */
  @Valid 
  @Schema(name = "StructureDataTypes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StructureDataTypes")
  public List<@Valid StructureDescription> getStructureDataTypes() {
    return structureDataTypes;
  }

  public void setStructureDataTypes(List<@Valid StructureDescription> structureDataTypes) {
    this.structureDataTypes = structureDataTypes;
  }

  public DataSetMetaDataType enumDataTypes(List<@Valid EnumDescription> enumDataTypes) {
    this.enumDataTypes = enumDataTypes;
    return this;
  }

  public DataSetMetaDataType addEnumDataTypesItem(EnumDescription enumDataTypesItem) {
    if (this.enumDataTypes == null) {
      this.enumDataTypes = new ArrayList<>();
    }
    this.enumDataTypes.add(enumDataTypesItem);
    return this;
  }

  /**
   * Get enumDataTypes
   * @return enumDataTypes
   */
  @Valid 
  @Schema(name = "EnumDataTypes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("EnumDataTypes")
  public List<@Valid EnumDescription> getEnumDataTypes() {
    return enumDataTypes;
  }

  public void setEnumDataTypes(List<@Valid EnumDescription> enumDataTypes) {
    this.enumDataTypes = enumDataTypes;
  }

  public DataSetMetaDataType simpleDataTypes(List<@Valid SimpleTypeDescription> simpleDataTypes) {
    this.simpleDataTypes = simpleDataTypes;
    return this;
  }

  public DataSetMetaDataType addSimpleDataTypesItem(SimpleTypeDescription simpleDataTypesItem) {
    if (this.simpleDataTypes == null) {
      this.simpleDataTypes = new ArrayList<>();
    }
    this.simpleDataTypes.add(simpleDataTypesItem);
    return this;
  }

  /**
   * Get simpleDataTypes
   * @return simpleDataTypes
   */
  @Valid 
  @Schema(name = "SimpleDataTypes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SimpleDataTypes")
  public List<@Valid SimpleTypeDescription> getSimpleDataTypes() {
    return simpleDataTypes;
  }

  public void setSimpleDataTypes(List<@Valid SimpleTypeDescription> simpleDataTypes) {
    this.simpleDataTypes = simpleDataTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataSetMetaDataType dataSetMetaDataType = (DataSetMetaDataType) o;
    return Objects.equals(this.name, dataSetMetaDataType.name) &&
        Objects.equals(this.description, dataSetMetaDataType.description) &&
        Objects.equals(this.fields, dataSetMetaDataType.fields) &&
        Objects.equals(this.dataSetClassId, dataSetMetaDataType.dataSetClassId) &&
        Objects.equals(this.configurationVersion, dataSetMetaDataType.configurationVersion) &&
        Objects.equals(this.namespaces, dataSetMetaDataType.namespaces) &&
        Objects.equals(this.structureDataTypes, dataSetMetaDataType.structureDataTypes) &&
        Objects.equals(this.enumDataTypes, dataSetMetaDataType.enumDataTypes) &&
        Objects.equals(this.simpleDataTypes, dataSetMetaDataType.simpleDataTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, fields, dataSetClassId, configurationVersion, namespaces, structureDataTypes, enumDataTypes, simpleDataTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataSetMetaDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
    sb.append("    dataSetClassId: ").append(toIndentedString(dataSetClassId)).append("\n");
    sb.append("    configurationVersion: ").append(toIndentedString(configurationVersion)).append("\n");
    sb.append("    namespaces: ").append(toIndentedString(namespaces)).append("\n");
    sb.append("    structureDataTypes: ").append(toIndentedString(structureDataTypes)).append("\n");
    sb.append("    enumDataTypes: ").append(toIndentedString(enumDataTypes)).append("\n");
    sb.append("    simpleDataTypes: ").append(toIndentedString(simpleDataTypes)).append("\n");
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


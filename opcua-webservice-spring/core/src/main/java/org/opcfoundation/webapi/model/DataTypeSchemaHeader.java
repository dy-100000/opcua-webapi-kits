package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.2).
 */

@Schema(name = "DataTypeSchemaHeader", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataTypeSchemaHeader {

  @Valid
  private List<String> namespaces = new ArrayList<>();

  @Valid
  private List<@Valid StructureDescription> structureDataTypes = new ArrayList<>();

  @Valid
  private List<@Valid EnumDescription> enumDataTypes = new ArrayList<>();

  @Valid
  private List<@Valid SimpleTypeDescription> simpleDataTypes = new ArrayList<>();

  public DataTypeSchemaHeader namespaces(List<String> namespaces) {
    this.namespaces = namespaces;
    return this;
  }

  public DataTypeSchemaHeader addNamespacesItem(String namespacesItem) {
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

  public DataTypeSchemaHeader structureDataTypes(List<@Valid StructureDescription> structureDataTypes) {
    this.structureDataTypes = structureDataTypes;
    return this;
  }

  public DataTypeSchemaHeader addStructureDataTypesItem(StructureDescription structureDataTypesItem) {
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

  public DataTypeSchemaHeader enumDataTypes(List<@Valid EnumDescription> enumDataTypes) {
    this.enumDataTypes = enumDataTypes;
    return this;
  }

  public DataTypeSchemaHeader addEnumDataTypesItem(EnumDescription enumDataTypesItem) {
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

  public DataTypeSchemaHeader simpleDataTypes(List<@Valid SimpleTypeDescription> simpleDataTypes) {
    this.simpleDataTypes = simpleDataTypes;
    return this;
  }

  public DataTypeSchemaHeader addSimpleDataTypesItem(SimpleTypeDescription simpleDataTypesItem) {
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
    DataTypeSchemaHeader dataTypeSchemaHeader = (DataTypeSchemaHeader) o;
    return Objects.equals(this.namespaces, dataTypeSchemaHeader.namespaces) &&
        Objects.equals(this.structureDataTypes, dataTypeSchemaHeader.structureDataTypes) &&
        Objects.equals(this.enumDataTypes, dataTypeSchemaHeader.enumDataTypes) &&
        Objects.equals(this.simpleDataTypes, dataTypeSchemaHeader.simpleDataTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespaces, structureDataTypes, enumDataTypes, simpleDataTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataTypeSchemaHeader {\n");
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


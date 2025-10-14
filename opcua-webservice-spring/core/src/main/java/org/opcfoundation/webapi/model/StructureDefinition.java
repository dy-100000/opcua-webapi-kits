package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.5).
 */

@Schema(name = "StructureDefinition", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class StructureDefinition {

  private @Nullable String defaultEncodingId;

  private @Nullable String baseDataType;

  private @Nullable Integer structureType;

  @Valid
  private List<@Valid StructureField> fields = new ArrayList<>();

  public StructureDefinition defaultEncodingId(String defaultEncodingId) {
    this.defaultEncodingId = defaultEncodingId;
    return this;
  }

  /**
   * Get defaultEncodingId
   * @return defaultEncodingId
   */
  
  @Schema(name = "DefaultEncodingId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DefaultEncodingId")
  public String getDefaultEncodingId() {
    return defaultEncodingId;
  }

  public void setDefaultEncodingId(String defaultEncodingId) {
    this.defaultEncodingId = defaultEncodingId;
  }

  public StructureDefinition baseDataType(String baseDataType) {
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

  public StructureDefinition structureType(Integer structureType) {
    this.structureType = structureType;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.3).
   * @return structureType
   */
  
  @Schema(name = "StructureType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.3).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StructureType")
  public Integer getStructureType() {
    return structureType;
  }

  public void setStructureType(Integer structureType) {
    this.structureType = structureType;
  }

  public StructureDefinition fields(List<@Valid StructureField> fields) {
    this.fields = fields;
    return this;
  }

  public StructureDefinition addFieldsItem(StructureField fieldsItem) {
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
  public List<@Valid StructureField> getFields() {
    return fields;
  }

  public void setFields(List<@Valid StructureField> fields) {
    this.fields = fields;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StructureDefinition structureDefinition = (StructureDefinition) o;
    return Objects.equals(this.defaultEncodingId, structureDefinition.defaultEncodingId) &&
        Objects.equals(this.baseDataType, structureDefinition.baseDataType) &&
        Objects.equals(this.structureType, structureDefinition.structureType) &&
        Objects.equals(this.fields, structureDefinition.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defaultEncodingId, baseDataType, structureType, fields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StructureDefinition {\n");
    sb.append("    defaultEncodingId: ").append(toIndentedString(defaultEncodingId)).append("\n");
    sb.append("    baseDataType: ").append(toIndentedString(baseDataType)).append("\n");
    sb.append("    structureType: ").append(toIndentedString(structureType)).append("\n");
    sb.append("    fields: ").append(toIndentedString(fields)).append("\n");
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


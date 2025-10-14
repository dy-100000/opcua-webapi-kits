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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.5).
 */

@Schema(name = "SimpleAttributeOperand", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SimpleAttributeOperand {

  private @Nullable String typeDefinitionId;

  @Valid
  private List<String> browsePath = new ArrayList<>();

  private Long attributeId = 0l;

  private @Nullable String indexRange;

  public SimpleAttributeOperand typeDefinitionId(String typeDefinitionId) {
    this.typeDefinitionId = typeDefinitionId;
    return this;
  }

  /**
   * Get typeDefinitionId
   * @return typeDefinitionId
   */
  
  @Schema(name = "TypeDefinitionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TypeDefinitionId")
  public String getTypeDefinitionId() {
    return typeDefinitionId;
  }

  public void setTypeDefinitionId(String typeDefinitionId) {
    this.typeDefinitionId = typeDefinitionId;
  }

  public SimpleAttributeOperand browsePath(List<String> browsePath) {
    this.browsePath = browsePath;
    return this;
  }

  public SimpleAttributeOperand addBrowsePathItem(String browsePathItem) {
    if (this.browsePath == null) {
      this.browsePath = new ArrayList<>();
    }
    this.browsePath.add(browsePathItem);
    return this;
  }

  /**
   * Get browsePath
   * @return browsePath
   */
  
  @Schema(name = "BrowsePath", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BrowsePath")
  public List<String> getBrowsePath() {
    return browsePath;
  }

  public void setBrowsePath(List<String> browsePath) {
    this.browsePath = browsePath;
  }

  public SimpleAttributeOperand attributeId(Long attributeId) {
    this.attributeId = attributeId;
    return this;
  }

  /**
   * Get attributeId
   * minimum: 0
   * maximum: 4294967295
   * @return attributeId
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "AttributeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AttributeId")
  public Long getAttributeId() {
    return attributeId;
  }

  public void setAttributeId(Long attributeId) {
    this.attributeId = attributeId;
  }

  public SimpleAttributeOperand indexRange(String indexRange) {
    this.indexRange = indexRange;
    return this;
  }

  /**
   * Get indexRange
   * @return indexRange
   */
  
  @Schema(name = "IndexRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IndexRange")
  public String getIndexRange() {
    return indexRange;
  }

  public void setIndexRange(String indexRange) {
    this.indexRange = indexRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleAttributeOperand simpleAttributeOperand = (SimpleAttributeOperand) o;
    return Objects.equals(this.typeDefinitionId, simpleAttributeOperand.typeDefinitionId) &&
        Objects.equals(this.browsePath, simpleAttributeOperand.browsePath) &&
        Objects.equals(this.attributeId, simpleAttributeOperand.attributeId) &&
        Objects.equals(this.indexRange, simpleAttributeOperand.indexRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeDefinitionId, browsePath, attributeId, indexRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimpleAttributeOperand {\n");
    sb.append("    typeDefinitionId: ").append(toIndentedString(typeDefinitionId)).append("\n");
    sb.append("    browsePath: ").append(toIndentedString(browsePath)).append("\n");
    sb.append("    attributeId: ").append(toIndentedString(attributeId)).append("\n");
    sb.append("    indexRange: ").append(toIndentedString(indexRange)).append("\n");
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


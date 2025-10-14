package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.4).
 */

@Schema(name = "AttributeOperand", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class AttributeOperand {

  private @Nullable String nodeId;

  private @Nullable String alias;

  private @Nullable RelativePath browsePath;

  private Long attributeId = 0l;

  private @Nullable String indexRange;

  public AttributeOperand nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  
  @Schema(name = "NodeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodeId")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public AttributeOperand alias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Get alias
   * @return alias
   */
  
  @Schema(name = "Alias", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Alias")
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public AttributeOperand browsePath(RelativePath browsePath) {
    this.browsePath = browsePath;
    return this;
  }

  /**
   * Get browsePath
   * @return browsePath
   */
  @Valid 
  @Schema(name = "BrowsePath", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BrowsePath")
  public RelativePath getBrowsePath() {
    return browsePath;
  }

  public void setBrowsePath(RelativePath browsePath) {
    this.browsePath = browsePath;
  }

  public AttributeOperand attributeId(Long attributeId) {
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

  public AttributeOperand indexRange(String indexRange) {
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
    AttributeOperand attributeOperand = (AttributeOperand) o;
    return Objects.equals(this.nodeId, attributeOperand.nodeId) &&
        Objects.equals(this.alias, attributeOperand.alias) &&
        Objects.equals(this.browsePath, attributeOperand.browsePath) &&
        Objects.equals(this.attributeId, attributeOperand.attributeId) &&
        Objects.equals(this.indexRange, attributeOperand.indexRange);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, alias, browsePath, attributeId, indexRange);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeOperand {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
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


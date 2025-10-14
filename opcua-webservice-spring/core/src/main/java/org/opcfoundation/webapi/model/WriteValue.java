package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2).
 */

@Schema(name = "WriteValue", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class WriteValue {

  private @Nullable String nodeId;

  private Long attributeId = 0l;

  private @Nullable String indexRange;

  private @Nullable DataValue value;

  public WriteValue nodeId(String nodeId) {
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

  public WriteValue attributeId(Long attributeId) {
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

  public WriteValue indexRange(String indexRange) {
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

  public WriteValue value(DataValue value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "Value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Value")
  public DataValue getValue() {
    return value;
  }

  public void setValue(DataValue value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WriteValue writeValue = (WriteValue) o;
    return Objects.equals(this.nodeId, writeValue.nodeId) &&
        Objects.equals(this.attributeId, writeValue.attributeId) &&
        Objects.equals(this.indexRange, writeValue.indexRange) &&
        Objects.equals(this.value, writeValue.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, attributeId, indexRange, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WriteValue {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    attributeId: ").append(toIndentedString(attributeId)).append("\n");
    sb.append("    indexRange: ").append(toIndentedString(indexRange)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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


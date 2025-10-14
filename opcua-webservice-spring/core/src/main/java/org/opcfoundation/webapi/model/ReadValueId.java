package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.29).
 */

@Schema(name = "ReadValueId", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.29).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadValueId {

  private @Nullable String nodeId;

  private Long attributeId = 0l;

  private @Nullable String indexRange;

  private @Nullable String dataEncoding;

  public ReadValueId nodeId(String nodeId) {
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

  public ReadValueId attributeId(Long attributeId) {
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

  public ReadValueId indexRange(String indexRange) {
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

  public ReadValueId dataEncoding(String dataEncoding) {
    this.dataEncoding = dataEncoding;
    return this;
  }

  /**
   * Get dataEncoding
   * @return dataEncoding
   */
  
  @Schema(name = "DataEncoding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataEncoding")
  public String getDataEncoding() {
    return dataEncoding;
  }

  public void setDataEncoding(String dataEncoding) {
    this.dataEncoding = dataEncoding;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadValueId readValueId = (ReadValueId) o;
    return Objects.equals(this.nodeId, readValueId.nodeId) &&
        Objects.equals(this.attributeId, readValueId.attributeId) &&
        Objects.equals(this.indexRange, readValueId.indexRange) &&
        Objects.equals(this.dataEncoding, readValueId.dataEncoding);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, attributeId, indexRange, dataEncoding);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadValueId {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    attributeId: ").append(toIndentedString(attributeId)).append("\n");
    sb.append("    indexRange: ").append(toIndentedString(indexRange)).append("\n");
    sb.append("    dataEncoding: ").append(toIndentedString(dataEncoding)).append("\n");
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


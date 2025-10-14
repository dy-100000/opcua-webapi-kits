package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2).
 */

@Schema(name = "HistoryReadValueId", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryReadValueId {

  private @Nullable String nodeId;

  private @Nullable String indexRange;

  private @Nullable String dataEncoding;

  private @Nullable byte[] continuationPoint;

  public HistoryReadValueId nodeId(String nodeId) {
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

  public HistoryReadValueId indexRange(String indexRange) {
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

  public HistoryReadValueId dataEncoding(String dataEncoding) {
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

  public HistoryReadValueId continuationPoint(byte[] continuationPoint) {
    this.continuationPoint = continuationPoint;
    return this;
  }

  /**
   * Get continuationPoint
   * @return continuationPoint
   */
  
  @Schema(name = "ContinuationPoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ContinuationPoint")
  public byte[] getContinuationPoint() {
    return continuationPoint;
  }

  public void setContinuationPoint(byte[] continuationPoint) {
    this.continuationPoint = continuationPoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryReadValueId historyReadValueId = (HistoryReadValueId) o;
    return Objects.equals(this.nodeId, historyReadValueId.nodeId) &&
        Objects.equals(this.indexRange, historyReadValueId.indexRange) &&
        Objects.equals(this.dataEncoding, historyReadValueId.dataEncoding) &&
        Arrays.equals(this.continuationPoint, historyReadValueId.continuationPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, indexRange, dataEncoding, Arrays.hashCode(continuationPoint));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryReadValueId {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    indexRange: ").append(toIndentedString(indexRange)).append("\n");
    sb.append("    dataEncoding: ").append(toIndentedString(dataEncoding)).append("\n");
    sb.append("    continuationPoint: ").append(toIndentedString(continuationPoint)).append("\n");
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


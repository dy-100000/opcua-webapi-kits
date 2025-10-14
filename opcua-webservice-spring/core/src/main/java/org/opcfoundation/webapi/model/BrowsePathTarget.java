package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).
 */

@Schema(name = "BrowsePathTarget", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowsePathTarget {

  private @Nullable String targetId;

  private Long remainingPathIndex = 0l;

  public BrowsePathTarget targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "TargetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TargetId")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public BrowsePathTarget remainingPathIndex(Long remainingPathIndex) {
    this.remainingPathIndex = remainingPathIndex;
    return this;
  }

  /**
   * Get remainingPathIndex
   * minimum: 0
   * maximum: 4294967295
   * @return remainingPathIndex
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RemainingPathIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RemainingPathIndex")
  public Long getRemainingPathIndex() {
    return remainingPathIndex;
  }

  public void setRemainingPathIndex(Long remainingPathIndex) {
    this.remainingPathIndex = remainingPathIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowsePathTarget browsePathTarget = (BrowsePathTarget) o;
    return Objects.equals(this.targetId, browsePathTarget.targetId) &&
        Objects.equals(this.remainingPathIndex, browsePathTarget.remainingPathIndex);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetId, remainingPathIndex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowsePathTarget {\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    remainingPathIndex: ").append(toIndentedString(remainingPathIndex)).append("\n");
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


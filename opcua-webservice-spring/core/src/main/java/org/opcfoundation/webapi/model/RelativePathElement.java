package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31).
 */

@Schema(name = "RelativePathElement", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RelativePathElement {

  private @Nullable String referenceTypeId;

  private Boolean isInverse = false;

  private Boolean includeSubtypes = false;

  private @Nullable String targetName;

  public RelativePathElement referenceTypeId(String referenceTypeId) {
    this.referenceTypeId = referenceTypeId;
    return this;
  }

  /**
   * Get referenceTypeId
   * @return referenceTypeId
   */
  
  @Schema(name = "ReferenceTypeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReferenceTypeId")
  public String getReferenceTypeId() {
    return referenceTypeId;
  }

  public void setReferenceTypeId(String referenceTypeId) {
    this.referenceTypeId = referenceTypeId;
  }

  public RelativePathElement isInverse(Boolean isInverse) {
    this.isInverse = isInverse;
    return this;
  }

  /**
   * Get isInverse
   * @return isInverse
   */
  
  @Schema(name = "IsInverse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IsInverse")
  public Boolean getIsInverse() {
    return isInverse;
  }

  public void setIsInverse(Boolean isInverse) {
    this.isInverse = isInverse;
  }

  public RelativePathElement includeSubtypes(Boolean includeSubtypes) {
    this.includeSubtypes = includeSubtypes;
    return this;
  }

  /**
   * Get includeSubtypes
   * @return includeSubtypes
   */
  
  @Schema(name = "IncludeSubtypes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IncludeSubtypes")
  public Boolean getIncludeSubtypes() {
    return includeSubtypes;
  }

  public void setIncludeSubtypes(Boolean includeSubtypes) {
    this.includeSubtypes = includeSubtypes;
  }

  public RelativePathElement targetName(String targetName) {
    this.targetName = targetName;
    return this;
  }

  /**
   * Get targetName
   * @return targetName
   */
  
  @Schema(name = "TargetName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TargetName")
  public String getTargetName() {
    return targetName;
  }

  public void setTargetName(String targetName) {
    this.targetName = targetName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelativePathElement relativePathElement = (RelativePathElement) o;
    return Objects.equals(this.referenceTypeId, relativePathElement.referenceTypeId) &&
        Objects.equals(this.isInverse, relativePathElement.isInverse) &&
        Objects.equals(this.includeSubtypes, relativePathElement.includeSubtypes) &&
        Objects.equals(this.targetName, relativePathElement.targetName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referenceTypeId, isInverse, includeSubtypes, targetName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelativePathElement {\n");
    sb.append("    referenceTypeId: ").append(toIndentedString(referenceTypeId)).append("\n");
    sb.append("    isInverse: ").append(toIndentedString(isInverse)).append("\n");
    sb.append("    includeSubtypes: ").append(toIndentedString(includeSubtypes)).append("\n");
    sb.append("    targetName: ").append(toIndentedString(targetName)).append("\n");
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


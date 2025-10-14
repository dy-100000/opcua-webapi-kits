package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2).
 */

@Schema(name = "BrowseDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowseDescription {

  private @Nullable String nodeId;

  private @Nullable Integer browseDirection;

  private @Nullable String referenceTypeId;

  private Boolean includeSubtypes = false;

  private Long nodeClassMask = 0l;

  private Long resultMask = 0l;

  public BrowseDescription nodeId(String nodeId) {
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

  public BrowseDescription browseDirection(Integer browseDirection) {
    this.browseDirection = browseDirection;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.5).
   * @return browseDirection
   */
  
  @Schema(name = "BrowseDirection", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.5).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BrowseDirection")
  public Integer getBrowseDirection() {
    return browseDirection;
  }

  public void setBrowseDirection(Integer browseDirection) {
    this.browseDirection = browseDirection;
  }

  public BrowseDescription referenceTypeId(String referenceTypeId) {
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

  public BrowseDescription includeSubtypes(Boolean includeSubtypes) {
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

  public BrowseDescription nodeClassMask(Long nodeClassMask) {
    this.nodeClassMask = nodeClassMask;
    return this;
  }

  /**
   * Get nodeClassMask
   * minimum: 0
   * maximum: 4294967295
   * @return nodeClassMask
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "NodeClassMask", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodeClassMask")
  public Long getNodeClassMask() {
    return nodeClassMask;
  }

  public void setNodeClassMask(Long nodeClassMask) {
    this.nodeClassMask = nodeClassMask;
  }

  public BrowseDescription resultMask(Long resultMask) {
    this.resultMask = resultMask;
    return this;
  }

  /**
   * Get resultMask
   * minimum: 0
   * maximum: 4294967295
   * @return resultMask
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ResultMask", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResultMask")
  public Long getResultMask() {
    return resultMask;
  }

  public void setResultMask(Long resultMask) {
    this.resultMask = resultMask;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowseDescription browseDescription = (BrowseDescription) o;
    return Objects.equals(this.nodeId, browseDescription.nodeId) &&
        Objects.equals(this.browseDirection, browseDescription.browseDirection) &&
        Objects.equals(this.referenceTypeId, browseDescription.referenceTypeId) &&
        Objects.equals(this.includeSubtypes, browseDescription.includeSubtypes) &&
        Objects.equals(this.nodeClassMask, browseDescription.nodeClassMask) &&
        Objects.equals(this.resultMask, browseDescription.resultMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, browseDirection, referenceTypeId, includeSubtypes, nodeClassMask, resultMask);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowseDescription {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    browseDirection: ").append(toIndentedString(browseDirection)).append("\n");
    sb.append("    referenceTypeId: ").append(toIndentedString(referenceTypeId)).append("\n");
    sb.append("    includeSubtypes: ").append(toIndentedString(includeSubtypes)).append("\n");
    sb.append("    nodeClassMask: ").append(toIndentedString(nodeClassMask)).append("\n");
    sb.append("    resultMask: ").append(toIndentedString(resultMask)).append("\n");
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


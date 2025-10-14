package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.30).
 */

@Schema(name = "ReferenceDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.30).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReferenceDescription {

  private @Nullable String referenceTypeId;

  private Boolean isForward = false;

  private @Nullable String nodeId;

  private @Nullable String browseName;

  private @Nullable LocalizedText displayName;

  private @Nullable Integer nodeClass;

  private @Nullable String typeDefinition;

  public ReferenceDescription referenceTypeId(String referenceTypeId) {
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

  public ReferenceDescription isForward(Boolean isForward) {
    this.isForward = isForward;
    return this;
  }

  /**
   * Get isForward
   * @return isForward
   */
  
  @Schema(name = "IsForward", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IsForward")
  public Boolean getIsForward() {
    return isForward;
  }

  public void setIsForward(Boolean isForward) {
    this.isForward = isForward;
  }

  public ReferenceDescription nodeId(String nodeId) {
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

  public ReferenceDescription browseName(String browseName) {
    this.browseName = browseName;
    return this;
  }

  /**
   * Get browseName
   * @return browseName
   */
  
  @Schema(name = "BrowseName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BrowseName")
  public String getBrowseName() {
    return browseName;
  }

  public void setBrowseName(String browseName) {
    this.browseName = browseName;
  }

  public ReferenceDescription displayName(LocalizedText displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  @Valid 
  @Schema(name = "DisplayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DisplayName")
  public LocalizedText getDisplayName() {
    return displayName;
  }

  public void setDisplayName(LocalizedText displayName) {
    this.displayName = displayName;
  }

  public ReferenceDescription nodeClass(Integer nodeClass) {
    this.nodeClass = nodeClass;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2).
   * @return nodeClass
   */
  
  @Schema(name = "NodeClass", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodeClass")
  public Integer getNodeClass() {
    return nodeClass;
  }

  public void setNodeClass(Integer nodeClass) {
    this.nodeClass = nodeClass;
  }

  public ReferenceDescription typeDefinition(String typeDefinition) {
    this.typeDefinition = typeDefinition;
    return this;
  }

  /**
   * Get typeDefinition
   * @return typeDefinition
   */
  
  @Schema(name = "TypeDefinition", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TypeDefinition")
  public String getTypeDefinition() {
    return typeDefinition;
  }

  public void setTypeDefinition(String typeDefinition) {
    this.typeDefinition = typeDefinition;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferenceDescription referenceDescription = (ReferenceDescription) o;
    return Objects.equals(this.referenceTypeId, referenceDescription.referenceTypeId) &&
        Objects.equals(this.isForward, referenceDescription.isForward) &&
        Objects.equals(this.nodeId, referenceDescription.nodeId) &&
        Objects.equals(this.browseName, referenceDescription.browseName) &&
        Objects.equals(this.displayName, referenceDescription.displayName) &&
        Objects.equals(this.nodeClass, referenceDescription.nodeClass) &&
        Objects.equals(this.typeDefinition, referenceDescription.typeDefinition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referenceTypeId, isForward, nodeId, browseName, displayName, nodeClass, typeDefinition);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferenceDescription {\n");
    sb.append("    referenceTypeId: ").append(toIndentedString(referenceTypeId)).append("\n");
    sb.append("    isForward: ").append(toIndentedString(isForward)).append("\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    browseName: ").append(toIndentedString(browseName)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    nodeClass: ").append(toIndentedString(nodeClass)).append("\n");
    sb.append("    typeDefinition: ").append(toIndentedString(typeDefinition)).append("\n");
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


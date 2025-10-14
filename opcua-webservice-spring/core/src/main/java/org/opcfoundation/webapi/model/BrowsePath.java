package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).
 */

@Schema(name = "BrowsePath", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowsePath {

  private @Nullable String startingNode;

  private @Nullable RelativePath relativePath;

  public BrowsePath startingNode(String startingNode) {
    this.startingNode = startingNode;
    return this;
  }

  /**
   * Get startingNode
   * @return startingNode
   */
  
  @Schema(name = "StartingNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StartingNode")
  public String getStartingNode() {
    return startingNode;
  }

  public void setStartingNode(String startingNode) {
    this.startingNode = startingNode;
  }

  public BrowsePath relativePath(RelativePath relativePath) {
    this.relativePath = relativePath;
    return this;
  }

  /**
   * Get relativePath
   * @return relativePath
   */
  @Valid 
  @Schema(name = "RelativePath", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RelativePath")
  public RelativePath getRelativePath() {
    return relativePath;
  }

  public void setRelativePath(RelativePath relativePath) {
    this.relativePath = relativePath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowsePath browsePath = (BrowsePath) o;
    return Objects.equals(this.startingNode, browsePath.startingNode) &&
        Objects.equals(this.relativePath, browsePath.relativePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startingNode, relativePath);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowsePath {\n");
    sb.append("    startingNode: ").append(toIndentedString(startingNode)).append("\n");
    sb.append("    relativePath: ").append(toIndentedString(relativePath)).append("\n");
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


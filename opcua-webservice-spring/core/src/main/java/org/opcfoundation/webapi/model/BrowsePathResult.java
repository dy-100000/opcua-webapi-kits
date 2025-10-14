package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).
 */

@Schema(name = "BrowsePathResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowsePathResult {

  private @Nullable StatusCode statusCode;

  @Valid
  private List<@Valid BrowsePathTarget> targets = new ArrayList<>();

  public BrowsePathResult statusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Get statusCode
   * @return statusCode
   */
  @Valid 
  @Schema(name = "StatusCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StatusCode")
  public StatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public BrowsePathResult targets(List<@Valid BrowsePathTarget> targets) {
    this.targets = targets;
    return this;
  }

  public BrowsePathResult addTargetsItem(BrowsePathTarget targetsItem) {
    if (this.targets == null) {
      this.targets = new ArrayList<>();
    }
    this.targets.add(targetsItem);
    return this;
  }

  /**
   * Get targets
   * @return targets
   */
  @Valid 
  @Schema(name = "Targets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Targets")
  public List<@Valid BrowsePathTarget> getTargets() {
    return targets;
  }

  public void setTargets(List<@Valid BrowsePathTarget> targets) {
    this.targets = targets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowsePathResult browsePathResult = (BrowsePathResult) o;
    return Objects.equals(this.statusCode, browsePathResult.statusCode) &&
        Objects.equals(this.targets, browsePathResult.targets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, targets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowsePathResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    targets: ").append(toIndentedString(targets)).append("\n");
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


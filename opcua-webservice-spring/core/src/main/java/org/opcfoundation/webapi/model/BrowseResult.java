package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.6).
 */

@Schema(name = "BrowseResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.6).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowseResult {

  private @Nullable StatusCode statusCode;

  private @Nullable byte[] continuationPoint;

  @Valid
  private List<@Valid ReferenceDescription> references = new ArrayList<>();

  public BrowseResult statusCode(StatusCode statusCode) {
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

  public BrowseResult continuationPoint(byte[] continuationPoint) {
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

  public BrowseResult references(List<@Valid ReferenceDescription> references) {
    this.references = references;
    return this;
  }

  public BrowseResult addReferencesItem(ReferenceDescription referencesItem) {
    if (this.references == null) {
      this.references = new ArrayList<>();
    }
    this.references.add(referencesItem);
    return this;
  }

  /**
   * Get references
   * @return references
   */
  @Valid 
  @Schema(name = "References", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("References")
  public List<@Valid ReferenceDescription> getReferences() {
    return references;
  }

  public void setReferences(List<@Valid ReferenceDescription> references) {
    this.references = references;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowseResult browseResult = (BrowseResult) o;
    return Objects.equals(this.statusCode, browseResult.statusCode) &&
        Arrays.equals(this.continuationPoint, browseResult.continuationPoint) &&
        Objects.equals(this.references, browseResult.references);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, Arrays.hashCode(continuationPoint), references);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowseResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    continuationPoint: ").append(toIndentedString(continuationPoint)).append("\n");
    sb.append("    references: ").append(toIndentedString(references)).append("\n");
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


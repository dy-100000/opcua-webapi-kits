package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.2).
 */

@Schema(name = "ContentFilterResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ContentFilterResult {

  @Valid
  private List<@Valid ContentFilterElementResult> elementResults = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> elementDiagnosticInfos = new ArrayList<>();

  public ContentFilterResult elementResults(List<@Valid ContentFilterElementResult> elementResults) {
    this.elementResults = elementResults;
    return this;
  }

  public ContentFilterResult addElementResultsItem(ContentFilterElementResult elementResultsItem) {
    if (this.elementResults == null) {
      this.elementResults = new ArrayList<>();
    }
    this.elementResults.add(elementResultsItem);
    return this;
  }

  /**
   * Get elementResults
   * @return elementResults
   */
  @Valid 
  @Schema(name = "ElementResults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ElementResults")
  public List<@Valid ContentFilterElementResult> getElementResults() {
    return elementResults;
  }

  public void setElementResults(List<@Valid ContentFilterElementResult> elementResults) {
    this.elementResults = elementResults;
  }

  public ContentFilterResult elementDiagnosticInfos(List<@Valid DiagnosticInfo> elementDiagnosticInfos) {
    this.elementDiagnosticInfos = elementDiagnosticInfos;
    return this;
  }

  public ContentFilterResult addElementDiagnosticInfosItem(DiagnosticInfo elementDiagnosticInfosItem) {
    if (this.elementDiagnosticInfos == null) {
      this.elementDiagnosticInfos = new ArrayList<>();
    }
    this.elementDiagnosticInfos.add(elementDiagnosticInfosItem);
    return this;
  }

  /**
   * Get elementDiagnosticInfos
   * @return elementDiagnosticInfos
   */
  @Valid 
  @Schema(name = "ElementDiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ElementDiagnosticInfos")
  public List<@Valid DiagnosticInfo> getElementDiagnosticInfos() {
    return elementDiagnosticInfos;
  }

  public void setElementDiagnosticInfos(List<@Valid DiagnosticInfo> elementDiagnosticInfos) {
    this.elementDiagnosticInfos = elementDiagnosticInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentFilterResult contentFilterResult = (ContentFilterResult) o;
    return Objects.equals(this.elementResults, contentFilterResult.elementResults) &&
        Objects.equals(this.elementDiagnosticInfos, contentFilterResult.elementDiagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elementResults, elementDiagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentFilterResult {\n");
    sb.append("    elementResults: ").append(toIndentedString(elementResults)).append("\n");
    sb.append("    elementDiagnosticInfos: ").append(toIndentedString(elementDiagnosticInfos)).append("\n");
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


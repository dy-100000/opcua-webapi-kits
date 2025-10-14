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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.5/#5.13.5.2).
 */

@Schema(name = "SetTriggeringResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.13.5/#5.13.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SetTriggeringResponse {

  private @Nullable ResponseHeader responseHeader;

  @Valid
  private List<@Valid StatusCode> addResults = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> addDiagnosticInfos = new ArrayList<>();

  @Valid
  private List<@Valid StatusCode> removeResults = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> removeDiagnosticInfos = new ArrayList<>();

  public SetTriggeringResponse responseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
    return this;
  }

  /**
   * Get responseHeader
   * @return responseHeader
   */
  @Valid 
  @Schema(name = "ResponseHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResponseHeader")
  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public void setResponseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
  }

  public SetTriggeringResponse addResults(List<@Valid StatusCode> addResults) {
    this.addResults = addResults;
    return this;
  }

  public SetTriggeringResponse addAddResultsItem(StatusCode addResultsItem) {
    if (this.addResults == null) {
      this.addResults = new ArrayList<>();
    }
    this.addResults.add(addResultsItem);
    return this;
  }

  /**
   * Get addResults
   * @return addResults
   */
  @Valid 
  @Schema(name = "AddResults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AddResults")
  public List<@Valid StatusCode> getAddResults() {
    return addResults;
  }

  public void setAddResults(List<@Valid StatusCode> addResults) {
    this.addResults = addResults;
  }

  public SetTriggeringResponse addDiagnosticInfos(List<@Valid DiagnosticInfo> addDiagnosticInfos) {
    this.addDiagnosticInfos = addDiagnosticInfos;
    return this;
  }

  public SetTriggeringResponse addAddDiagnosticInfosItem(DiagnosticInfo addDiagnosticInfosItem) {
    if (this.addDiagnosticInfos == null) {
      this.addDiagnosticInfos = new ArrayList<>();
    }
    this.addDiagnosticInfos.add(addDiagnosticInfosItem);
    return this;
  }

  /**
   * Get addDiagnosticInfos
   * @return addDiagnosticInfos
   */
  @Valid 
  @Schema(name = "AddDiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AddDiagnosticInfos")
  public List<@Valid DiagnosticInfo> getAddDiagnosticInfos() {
    return addDiagnosticInfos;
  }

  public void setAddDiagnosticInfos(List<@Valid DiagnosticInfo> addDiagnosticInfos) {
    this.addDiagnosticInfos = addDiagnosticInfos;
  }

  public SetTriggeringResponse removeResults(List<@Valid StatusCode> removeResults) {
    this.removeResults = removeResults;
    return this;
  }

  public SetTriggeringResponse addRemoveResultsItem(StatusCode removeResultsItem) {
    if (this.removeResults == null) {
      this.removeResults = new ArrayList<>();
    }
    this.removeResults.add(removeResultsItem);
    return this;
  }

  /**
   * Get removeResults
   * @return removeResults
   */
  @Valid 
  @Schema(name = "RemoveResults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RemoveResults")
  public List<@Valid StatusCode> getRemoveResults() {
    return removeResults;
  }

  public void setRemoveResults(List<@Valid StatusCode> removeResults) {
    this.removeResults = removeResults;
  }

  public SetTriggeringResponse removeDiagnosticInfos(List<@Valid DiagnosticInfo> removeDiagnosticInfos) {
    this.removeDiagnosticInfos = removeDiagnosticInfos;
    return this;
  }

  public SetTriggeringResponse addRemoveDiagnosticInfosItem(DiagnosticInfo removeDiagnosticInfosItem) {
    if (this.removeDiagnosticInfos == null) {
      this.removeDiagnosticInfos = new ArrayList<>();
    }
    this.removeDiagnosticInfos.add(removeDiagnosticInfosItem);
    return this;
  }

  /**
   * Get removeDiagnosticInfos
   * @return removeDiagnosticInfos
   */
  @Valid 
  @Schema(name = "RemoveDiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RemoveDiagnosticInfos")
  public List<@Valid DiagnosticInfo> getRemoveDiagnosticInfos() {
    return removeDiagnosticInfos;
  }

  public void setRemoveDiagnosticInfos(List<@Valid DiagnosticInfo> removeDiagnosticInfos) {
    this.removeDiagnosticInfos = removeDiagnosticInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetTriggeringResponse setTriggeringResponse = (SetTriggeringResponse) o;
    return Objects.equals(this.responseHeader, setTriggeringResponse.responseHeader) &&
        Objects.equals(this.addResults, setTriggeringResponse.addResults) &&
        Objects.equals(this.addDiagnosticInfos, setTriggeringResponse.addDiagnosticInfos) &&
        Objects.equals(this.removeResults, setTriggeringResponse.removeResults) &&
        Objects.equals(this.removeDiagnosticInfos, setTriggeringResponse.removeDiagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, addResults, addDiagnosticInfos, removeResults, removeDiagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetTriggeringResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    addResults: ").append(toIndentedString(addResults)).append("\n");
    sb.append("    addDiagnosticInfos: ").append(toIndentedString(addDiagnosticInfos)).append("\n");
    sb.append("    removeResults: ").append(toIndentedString(removeResults)).append("\n");
    sb.append("    removeDiagnosticInfos: ").append(toIndentedString(removeDiagnosticInfos)).append("\n");
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


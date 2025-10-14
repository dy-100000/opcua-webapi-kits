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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.8/#5.14.8.2).
 */

@Schema(name = "DeleteSubscriptionsResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.8/#5.14.8.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DeleteSubscriptionsResponse {

  private @Nullable ResponseHeader responseHeader;

  @Valid
  private List<@Valid StatusCode> results = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> diagnosticInfos = new ArrayList<>();

  public DeleteSubscriptionsResponse responseHeader(ResponseHeader responseHeader) {
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

  public DeleteSubscriptionsResponse results(List<@Valid StatusCode> results) {
    this.results = results;
    return this;
  }

  public DeleteSubscriptionsResponse addResultsItem(StatusCode resultsItem) {
    if (this.results == null) {
      this.results = new ArrayList<>();
    }
    this.results.add(resultsItem);
    return this;
  }

  /**
   * Get results
   * @return results
   */
  @Valid 
  @Schema(name = "Results", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Results")
  public List<@Valid StatusCode> getResults() {
    return results;
  }

  public void setResults(List<@Valid StatusCode> results) {
    this.results = results;
  }

  public DeleteSubscriptionsResponse diagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
    return this;
  }

  public DeleteSubscriptionsResponse addDiagnosticInfosItem(DiagnosticInfo diagnosticInfosItem) {
    if (this.diagnosticInfos == null) {
      this.diagnosticInfos = new ArrayList<>();
    }
    this.diagnosticInfos.add(diagnosticInfosItem);
    return this;
  }

  /**
   * Get diagnosticInfos
   * @return diagnosticInfos
   */
  @Valid 
  @Schema(name = "DiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiagnosticInfos")
  public List<@Valid DiagnosticInfo> getDiagnosticInfos() {
    return diagnosticInfos;
  }

  public void setDiagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeleteSubscriptionsResponse deleteSubscriptionsResponse = (DeleteSubscriptionsResponse) o;
    return Objects.equals(this.responseHeader, deleteSubscriptionsResponse.responseHeader) &&
        Objects.equals(this.results, deleteSubscriptionsResponse.results) &&
        Objects.equals(this.diagnosticInfos, deleteSubscriptionsResponse.diagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, results, diagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeleteSubscriptionsResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
    sb.append("    diagnosticInfos: ").append(toIndentedString(diagnosticInfos)).append("\n");
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


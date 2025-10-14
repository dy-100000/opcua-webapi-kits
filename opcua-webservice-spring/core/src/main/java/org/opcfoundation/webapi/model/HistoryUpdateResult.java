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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2).
 */

@Schema(name = "HistoryUpdateResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryUpdateResult {

  private @Nullable StatusCode statusCode;

  @Valid
  private List<@Valid StatusCode> operationResults = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> diagnosticInfos = new ArrayList<>();

  public HistoryUpdateResult statusCode(StatusCode statusCode) {
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

  public HistoryUpdateResult operationResults(List<@Valid StatusCode> operationResults) {
    this.operationResults = operationResults;
    return this;
  }

  public HistoryUpdateResult addOperationResultsItem(StatusCode operationResultsItem) {
    if (this.operationResults == null) {
      this.operationResults = new ArrayList<>();
    }
    this.operationResults.add(operationResultsItem);
    return this;
  }

  /**
   * Get operationResults
   * @return operationResults
   */
  @Valid 
  @Schema(name = "OperationResults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("OperationResults")
  public List<@Valid StatusCode> getOperationResults() {
    return operationResults;
  }

  public void setOperationResults(List<@Valid StatusCode> operationResults) {
    this.operationResults = operationResults;
  }

  public HistoryUpdateResult diagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
    return this;
  }

  public HistoryUpdateResult addDiagnosticInfosItem(DiagnosticInfo diagnosticInfosItem) {
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
    HistoryUpdateResult historyUpdateResult = (HistoryUpdateResult) o;
    return Objects.equals(this.statusCode, historyUpdateResult.statusCode) &&
        Objects.equals(this.operationResults, historyUpdateResult.operationResults) &&
        Objects.equals(this.diagnosticInfos, historyUpdateResult.diagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, operationResults, diagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryUpdateResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    operationResults: ").append(toIndentedString(operationResults)).append("\n");
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


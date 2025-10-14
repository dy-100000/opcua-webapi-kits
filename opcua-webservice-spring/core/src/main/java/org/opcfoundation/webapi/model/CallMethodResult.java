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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2).
 */

@Schema(name = "CallMethodResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CallMethodResult {

  private @Nullable StatusCode statusCode;

  @Valid
  private List<@Valid StatusCode> inputArgumentResults = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> inputArgumentDiagnosticInfos = new ArrayList<>();

  @Valid
  private List<@Valid Variant> outputArguments = new ArrayList<>();

  public CallMethodResult statusCode(StatusCode statusCode) {
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

  public CallMethodResult inputArgumentResults(List<@Valid StatusCode> inputArgumentResults) {
    this.inputArgumentResults = inputArgumentResults;
    return this;
  }

  public CallMethodResult addInputArgumentResultsItem(StatusCode inputArgumentResultsItem) {
    if (this.inputArgumentResults == null) {
      this.inputArgumentResults = new ArrayList<>();
    }
    this.inputArgumentResults.add(inputArgumentResultsItem);
    return this;
  }

  /**
   * Get inputArgumentResults
   * @return inputArgumentResults
   */
  @Valid 
  @Schema(name = "InputArgumentResults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("InputArgumentResults")
  public List<@Valid StatusCode> getInputArgumentResults() {
    return inputArgumentResults;
  }

  public void setInputArgumentResults(List<@Valid StatusCode> inputArgumentResults) {
    this.inputArgumentResults = inputArgumentResults;
  }

  public CallMethodResult inputArgumentDiagnosticInfos(List<@Valid DiagnosticInfo> inputArgumentDiagnosticInfos) {
    this.inputArgumentDiagnosticInfos = inputArgumentDiagnosticInfos;
    return this;
  }

  public CallMethodResult addInputArgumentDiagnosticInfosItem(DiagnosticInfo inputArgumentDiagnosticInfosItem) {
    if (this.inputArgumentDiagnosticInfos == null) {
      this.inputArgumentDiagnosticInfos = new ArrayList<>();
    }
    this.inputArgumentDiagnosticInfos.add(inputArgumentDiagnosticInfosItem);
    return this;
  }

  /**
   * Get inputArgumentDiagnosticInfos
   * @return inputArgumentDiagnosticInfos
   */
  @Valid 
  @Schema(name = "InputArgumentDiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("InputArgumentDiagnosticInfos")
  public List<@Valid DiagnosticInfo> getInputArgumentDiagnosticInfos() {
    return inputArgumentDiagnosticInfos;
  }

  public void setInputArgumentDiagnosticInfos(List<@Valid DiagnosticInfo> inputArgumentDiagnosticInfos) {
    this.inputArgumentDiagnosticInfos = inputArgumentDiagnosticInfos;
  }

  public CallMethodResult outputArguments(List<@Valid Variant> outputArguments) {
    this.outputArguments = outputArguments;
    return this;
  }

  public CallMethodResult addOutputArgumentsItem(Variant outputArgumentsItem) {
    if (this.outputArguments == null) {
      this.outputArguments = new ArrayList<>();
    }
    this.outputArguments.add(outputArgumentsItem);
    return this;
  }

  /**
   * Get outputArguments
   * @return outputArguments
   */
  @Valid 
  @Schema(name = "OutputArguments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("OutputArguments")
  public List<@Valid Variant> getOutputArguments() {
    return outputArguments;
  }

  public void setOutputArguments(List<@Valid Variant> outputArguments) {
    this.outputArguments = outputArguments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CallMethodResult callMethodResult = (CallMethodResult) o;
    return Objects.equals(this.statusCode, callMethodResult.statusCode) &&
        Objects.equals(this.inputArgumentResults, callMethodResult.inputArgumentResults) &&
        Objects.equals(this.inputArgumentDiagnosticInfos, callMethodResult.inputArgumentDiagnosticInfos) &&
        Objects.equals(this.outputArguments, callMethodResult.outputArguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, inputArgumentResults, inputArgumentDiagnosticInfos, outputArguments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CallMethodResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    inputArgumentResults: ").append(toIndentedString(inputArgumentResults)).append("\n");
    sb.append("    inputArgumentDiagnosticInfos: ").append(toIndentedString(inputArgumentDiagnosticInfos)).append("\n");
    sb.append("    outputArguments: ").append(toIndentedString(outputArguments)).append("\n");
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


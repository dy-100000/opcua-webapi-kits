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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2).
 */

@Schema(name = "ActivateSessionResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ActivateSessionResponse {

  private @Nullable ResponseHeader responseHeader;

  private @Nullable byte[] serverNonce;

  @Valid
  private List<@Valid StatusCode> results = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> diagnosticInfos = new ArrayList<>();

  public ActivateSessionResponse responseHeader(ResponseHeader responseHeader) {
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

  public ActivateSessionResponse serverNonce(byte[] serverNonce) {
    this.serverNonce = serverNonce;
    return this;
  }

  /**
   * Get serverNonce
   * @return serverNonce
   */
  
  @Schema(name = "ServerNonce", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerNonce")
  public byte[] getServerNonce() {
    return serverNonce;
  }

  public void setServerNonce(byte[] serverNonce) {
    this.serverNonce = serverNonce;
  }

  public ActivateSessionResponse results(List<@Valid StatusCode> results) {
    this.results = results;
    return this;
  }

  public ActivateSessionResponse addResultsItem(StatusCode resultsItem) {
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

  public ActivateSessionResponse diagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
    return this;
  }

  public ActivateSessionResponse addDiagnosticInfosItem(DiagnosticInfo diagnosticInfosItem) {
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
    ActivateSessionResponse activateSessionResponse = (ActivateSessionResponse) o;
    return Objects.equals(this.responseHeader, activateSessionResponse.responseHeader) &&
        Arrays.equals(this.serverNonce, activateSessionResponse.serverNonce) &&
        Objects.equals(this.results, activateSessionResponse.results) &&
        Objects.equals(this.diagnosticInfos, activateSessionResponse.diagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, Arrays.hashCode(serverNonce), results, diagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivateSessionResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    serverNonce: ").append(toIndentedString(serverNonce)).append("\n");
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


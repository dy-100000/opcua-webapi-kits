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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.2).
 */

@Schema(name = "ContentFilterElementResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ContentFilterElementResult {

  private @Nullable StatusCode statusCode;

  @Valid
  private List<@Valid StatusCode> operandStatusCodes = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> operandDiagnosticInfos = new ArrayList<>();

  public ContentFilterElementResult statusCode(StatusCode statusCode) {
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

  public ContentFilterElementResult operandStatusCodes(List<@Valid StatusCode> operandStatusCodes) {
    this.operandStatusCodes = operandStatusCodes;
    return this;
  }

  public ContentFilterElementResult addOperandStatusCodesItem(StatusCode operandStatusCodesItem) {
    if (this.operandStatusCodes == null) {
      this.operandStatusCodes = new ArrayList<>();
    }
    this.operandStatusCodes.add(operandStatusCodesItem);
    return this;
  }

  /**
   * Get operandStatusCodes
   * @return operandStatusCodes
   */
  @Valid 
  @Schema(name = "OperandStatusCodes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("OperandStatusCodes")
  public List<@Valid StatusCode> getOperandStatusCodes() {
    return operandStatusCodes;
  }

  public void setOperandStatusCodes(List<@Valid StatusCode> operandStatusCodes) {
    this.operandStatusCodes = operandStatusCodes;
  }

  public ContentFilterElementResult operandDiagnosticInfos(List<@Valid DiagnosticInfo> operandDiagnosticInfos) {
    this.operandDiagnosticInfos = operandDiagnosticInfos;
    return this;
  }

  public ContentFilterElementResult addOperandDiagnosticInfosItem(DiagnosticInfo operandDiagnosticInfosItem) {
    if (this.operandDiagnosticInfos == null) {
      this.operandDiagnosticInfos = new ArrayList<>();
    }
    this.operandDiagnosticInfos.add(operandDiagnosticInfosItem);
    return this;
  }

  /**
   * Get operandDiagnosticInfos
   * @return operandDiagnosticInfos
   */
  @Valid 
  @Schema(name = "OperandDiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("OperandDiagnosticInfos")
  public List<@Valid DiagnosticInfo> getOperandDiagnosticInfos() {
    return operandDiagnosticInfos;
  }

  public void setOperandDiagnosticInfos(List<@Valid DiagnosticInfo> operandDiagnosticInfos) {
    this.operandDiagnosticInfos = operandDiagnosticInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentFilterElementResult contentFilterElementResult = (ContentFilterElementResult) o;
    return Objects.equals(this.statusCode, contentFilterElementResult.statusCode) &&
        Objects.equals(this.operandStatusCodes, contentFilterElementResult.operandStatusCodes) &&
        Objects.equals(this.operandDiagnosticInfos, contentFilterElementResult.operandDiagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, operandStatusCodes, operandDiagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentFilterElementResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    operandStatusCodes: ").append(toIndentedString(operandStatusCodes)).append("\n");
    sb.append("    operandDiagnosticInfos: ").append(toIndentedString(operandDiagnosticInfos)).append("\n");
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


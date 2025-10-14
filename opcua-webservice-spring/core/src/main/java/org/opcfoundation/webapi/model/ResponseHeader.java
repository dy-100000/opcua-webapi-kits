package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.34).
 */

@Schema(name = "ResponseHeader", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.34).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ResponseHeader {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Long requestHandle = 0l;

  private @Nullable StatusCode serviceResult;

  private @Nullable DiagnosticInfo serviceDiagnostics;

  @Valid
  private List<String> stringTable = new ArrayList<>();

  private @Nullable ExtensionObject additionalHeader;

  public ResponseHeader timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "Timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ResponseHeader requestHandle(Long requestHandle) {
    this.requestHandle = requestHandle;
    return this;
  }

  /**
   * Get requestHandle
   * minimum: 0
   * maximum: 4294967295
   * @return requestHandle
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RequestHandle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHandle")
  public Long getRequestHandle() {
    return requestHandle;
  }

  public void setRequestHandle(Long requestHandle) {
    this.requestHandle = requestHandle;
  }

  public ResponseHeader serviceResult(StatusCode serviceResult) {
    this.serviceResult = serviceResult;
    return this;
  }

  /**
   * Get serviceResult
   * @return serviceResult
   */
  @Valid 
  @Schema(name = "ServiceResult", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServiceResult")
  public StatusCode getServiceResult() {
    return serviceResult;
  }

  public void setServiceResult(StatusCode serviceResult) {
    this.serviceResult = serviceResult;
  }

  public ResponseHeader serviceDiagnostics(DiagnosticInfo serviceDiagnostics) {
    this.serviceDiagnostics = serviceDiagnostics;
    return this;
  }

  /**
   * Get serviceDiagnostics
   * @return serviceDiagnostics
   */
  @Valid 
  @Schema(name = "ServiceDiagnostics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServiceDiagnostics")
  public DiagnosticInfo getServiceDiagnostics() {
    return serviceDiagnostics;
  }

  public void setServiceDiagnostics(DiagnosticInfo serviceDiagnostics) {
    this.serviceDiagnostics = serviceDiagnostics;
  }

  public ResponseHeader stringTable(List<String> stringTable) {
    this.stringTable = stringTable;
    return this;
  }

  public ResponseHeader addStringTableItem(String stringTableItem) {
    if (this.stringTable == null) {
      this.stringTable = new ArrayList<>();
    }
    this.stringTable.add(stringTableItem);
    return this;
  }

  /**
   * Get stringTable
   * @return stringTable
   */
  
  @Schema(name = "StringTable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StringTable")
  public List<String> getStringTable() {
    return stringTable;
  }

  public void setStringTable(List<String> stringTable) {
    this.stringTable = stringTable;
  }

  public ResponseHeader additionalHeader(ExtensionObject additionalHeader) {
    this.additionalHeader = additionalHeader;
    return this;
  }

  /**
   * Get additionalHeader
   * @return additionalHeader
   */
  @Valid 
  @Schema(name = "AdditionalHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AdditionalHeader")
  public ExtensionObject getAdditionalHeader() {
    return additionalHeader;
  }

  public void setAdditionalHeader(ExtensionObject additionalHeader) {
    this.additionalHeader = additionalHeader;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseHeader responseHeader = (ResponseHeader) o;
    return Objects.equals(this.timestamp, responseHeader.timestamp) &&
        Objects.equals(this.requestHandle, responseHeader.requestHandle) &&
        Objects.equals(this.serviceResult, responseHeader.serviceResult) &&
        Objects.equals(this.serviceDiagnostics, responseHeader.serviceDiagnostics) &&
        Objects.equals(this.stringTable, responseHeader.stringTable) &&
        Objects.equals(this.additionalHeader, responseHeader.additionalHeader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, requestHandle, serviceResult, serviceDiagnostics, stringTable, additionalHeader);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseHeader {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    requestHandle: ").append(toIndentedString(requestHandle)).append("\n");
    sb.append("    serviceResult: ").append(toIndentedString(serviceResult)).append("\n");
    sb.append("    serviceDiagnostics: ").append(toIndentedString(serviceDiagnostics)).append("\n");
    sb.append("    stringTable: ").append(toIndentedString(stringTable)).append("\n");
    sb.append("    additionalHeader: ").append(toIndentedString(additionalHeader)).append("\n");
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


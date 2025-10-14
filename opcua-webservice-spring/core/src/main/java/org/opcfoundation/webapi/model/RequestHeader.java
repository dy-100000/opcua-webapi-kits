package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.33).
 */

@Schema(name = "RequestHeader", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.33).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RequestHeader {

  private @Nullable String authenticationToken;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Long requestHandle = 0l;

  private Long returnDiagnostics = 0l;

  private @Nullable String auditEntryId;

  private Long timeoutHint = 0l;

  private @Nullable ExtensionObject additionalHeader;

  public RequestHeader authenticationToken(String authenticationToken) {
    this.authenticationToken = authenticationToken;
    return this;
  }

  /**
   * Get authenticationToken
   * @return authenticationToken
   */
  
  @Schema(name = "AuthenticationToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AuthenticationToken")
  public String getAuthenticationToken() {
    return authenticationToken;
  }

  public void setAuthenticationToken(String authenticationToken) {
    this.authenticationToken = authenticationToken;
  }

  public RequestHeader timestamp(OffsetDateTime timestamp) {
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

  public RequestHeader requestHandle(Long requestHandle) {
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

  public RequestHeader returnDiagnostics(Long returnDiagnostics) {
    this.returnDiagnostics = returnDiagnostics;
    return this;
  }

  /**
   * Get returnDiagnostics
   * minimum: 0
   * maximum: 4294967295
   * @return returnDiagnostics
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ReturnDiagnostics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReturnDiagnostics")
  public Long getReturnDiagnostics() {
    return returnDiagnostics;
  }

  public void setReturnDiagnostics(Long returnDiagnostics) {
    this.returnDiagnostics = returnDiagnostics;
  }

  public RequestHeader auditEntryId(String auditEntryId) {
    this.auditEntryId = auditEntryId;
    return this;
  }

  /**
   * Get auditEntryId
   * @return auditEntryId
   */
  
  @Schema(name = "AuditEntryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AuditEntryId")
  public String getAuditEntryId() {
    return auditEntryId;
  }

  public void setAuditEntryId(String auditEntryId) {
    this.auditEntryId = auditEntryId;
  }

  public RequestHeader timeoutHint(Long timeoutHint) {
    this.timeoutHint = timeoutHint;
    return this;
  }

  /**
   * Get timeoutHint
   * minimum: 0
   * maximum: 4294967295
   * @return timeoutHint
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "TimeoutHint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TimeoutHint")
  public Long getTimeoutHint() {
    return timeoutHint;
  }

  public void setTimeoutHint(Long timeoutHint) {
    this.timeoutHint = timeoutHint;
  }

  public RequestHeader additionalHeader(ExtensionObject additionalHeader) {
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
    RequestHeader requestHeader = (RequestHeader) o;
    return Objects.equals(this.authenticationToken, requestHeader.authenticationToken) &&
        Objects.equals(this.timestamp, requestHeader.timestamp) &&
        Objects.equals(this.requestHandle, requestHeader.requestHandle) &&
        Objects.equals(this.returnDiagnostics, requestHeader.returnDiagnostics) &&
        Objects.equals(this.auditEntryId, requestHeader.auditEntryId) &&
        Objects.equals(this.timeoutHint, requestHeader.timeoutHint) &&
        Objects.equals(this.additionalHeader, requestHeader.additionalHeader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authenticationToken, timestamp, requestHandle, returnDiagnostics, auditEntryId, timeoutHint, additionalHeader);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestHeader {\n");
    sb.append("    authenticationToken: ").append(toIndentedString(authenticationToken)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    requestHandle: ").append(toIndentedString(requestHandle)).append("\n");
    sb.append("    returnDiagnostics: ").append(toIndentedString(returnDiagnostics)).append("\n");
    sb.append("    auditEntryId: ").append(toIndentedString(auditEntryId)).append("\n");
    sb.append("    timeoutHint: ").append(toIndentedString(timeoutHint)).append("\n");
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


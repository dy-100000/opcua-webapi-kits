package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2).
 */

@Schema(name = "CreateSessionResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateSessionResponse {

  private @Nullable ResponseHeader responseHeader;

  private @Nullable String sessionId;

  private @Nullable String authenticationToken;

  private Double revisedSessionTimeout = 0d;

  private @Nullable byte[] serverNonce;

  private @Nullable byte[] serverCertificate;

  @Valid
  private List<@Valid EndpointDescription> serverEndpoints = new ArrayList<>();

  @Valid
  private List<@Valid SignedSoftwareCertificate> serverSoftwareCertificates = new ArrayList<>();

  private @Nullable SignatureData serverSignature;

  private Long maxRequestMessageSize = 0l;

  public CreateSessionResponse responseHeader(ResponseHeader responseHeader) {
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

  public CreateSessionResponse sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "SessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SessionId")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public CreateSessionResponse authenticationToken(String authenticationToken) {
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

  public CreateSessionResponse revisedSessionTimeout(Double revisedSessionTimeout) {
    this.revisedSessionTimeout = revisedSessionTimeout;
    return this;
  }

  /**
   * Get revisedSessionTimeout
   * @return revisedSessionTimeout
   */
  
  @Schema(name = "RevisedSessionTimeout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RevisedSessionTimeout")
  public Double getRevisedSessionTimeout() {
    return revisedSessionTimeout;
  }

  public void setRevisedSessionTimeout(Double revisedSessionTimeout) {
    this.revisedSessionTimeout = revisedSessionTimeout;
  }

  public CreateSessionResponse serverNonce(byte[] serverNonce) {
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

  public CreateSessionResponse serverCertificate(byte[] serverCertificate) {
    this.serverCertificate = serverCertificate;
    return this;
  }

  /**
   * Get serverCertificate
   * @return serverCertificate
   */
  
  @Schema(name = "ServerCertificate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerCertificate")
  public byte[] getServerCertificate() {
    return serverCertificate;
  }

  public void setServerCertificate(byte[] serverCertificate) {
    this.serverCertificate = serverCertificate;
  }

  public CreateSessionResponse serverEndpoints(List<@Valid EndpointDescription> serverEndpoints) {
    this.serverEndpoints = serverEndpoints;
    return this;
  }

  public CreateSessionResponse addServerEndpointsItem(EndpointDescription serverEndpointsItem) {
    if (this.serverEndpoints == null) {
      this.serverEndpoints = new ArrayList<>();
    }
    this.serverEndpoints.add(serverEndpointsItem);
    return this;
  }

  /**
   * Get serverEndpoints
   * @return serverEndpoints
   */
  @Valid 
  @Schema(name = "ServerEndpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerEndpoints")
  public List<@Valid EndpointDescription> getServerEndpoints() {
    return serverEndpoints;
  }

  public void setServerEndpoints(List<@Valid EndpointDescription> serverEndpoints) {
    this.serverEndpoints = serverEndpoints;
  }

  public CreateSessionResponse serverSoftwareCertificates(List<@Valid SignedSoftwareCertificate> serverSoftwareCertificates) {
    this.serverSoftwareCertificates = serverSoftwareCertificates;
    return this;
  }

  public CreateSessionResponse addServerSoftwareCertificatesItem(SignedSoftwareCertificate serverSoftwareCertificatesItem) {
    if (this.serverSoftwareCertificates == null) {
      this.serverSoftwareCertificates = new ArrayList<>();
    }
    this.serverSoftwareCertificates.add(serverSoftwareCertificatesItem);
    return this;
  }

  /**
   * Get serverSoftwareCertificates
   * @return serverSoftwareCertificates
   */
  @Valid 
  @Schema(name = "ServerSoftwareCertificates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerSoftwareCertificates")
  public List<@Valid SignedSoftwareCertificate> getServerSoftwareCertificates() {
    return serverSoftwareCertificates;
  }

  public void setServerSoftwareCertificates(List<@Valid SignedSoftwareCertificate> serverSoftwareCertificates) {
    this.serverSoftwareCertificates = serverSoftwareCertificates;
  }

  public CreateSessionResponse serverSignature(SignatureData serverSignature) {
    this.serverSignature = serverSignature;
    return this;
  }

  /**
   * Get serverSignature
   * @return serverSignature
   */
  @Valid 
  @Schema(name = "ServerSignature", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerSignature")
  public SignatureData getServerSignature() {
    return serverSignature;
  }

  public void setServerSignature(SignatureData serverSignature) {
    this.serverSignature = serverSignature;
  }

  public CreateSessionResponse maxRequestMessageSize(Long maxRequestMessageSize) {
    this.maxRequestMessageSize = maxRequestMessageSize;
    return this;
  }

  /**
   * Get maxRequestMessageSize
   * minimum: 0
   * maximum: 4294967295
   * @return maxRequestMessageSize
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxRequestMessageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxRequestMessageSize")
  public Long getMaxRequestMessageSize() {
    return maxRequestMessageSize;
  }

  public void setMaxRequestMessageSize(Long maxRequestMessageSize) {
    this.maxRequestMessageSize = maxRequestMessageSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSessionResponse createSessionResponse = (CreateSessionResponse) o;
    return Objects.equals(this.responseHeader, createSessionResponse.responseHeader) &&
        Objects.equals(this.sessionId, createSessionResponse.sessionId) &&
        Objects.equals(this.authenticationToken, createSessionResponse.authenticationToken) &&
        Objects.equals(this.revisedSessionTimeout, createSessionResponse.revisedSessionTimeout) &&
        Arrays.equals(this.serverNonce, createSessionResponse.serverNonce) &&
        Arrays.equals(this.serverCertificate, createSessionResponse.serverCertificate) &&
        Objects.equals(this.serverEndpoints, createSessionResponse.serverEndpoints) &&
        Objects.equals(this.serverSoftwareCertificates, createSessionResponse.serverSoftwareCertificates) &&
        Objects.equals(this.serverSignature, createSessionResponse.serverSignature) &&
        Objects.equals(this.maxRequestMessageSize, createSessionResponse.maxRequestMessageSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, sessionId, authenticationToken, revisedSessionTimeout, Arrays.hashCode(serverNonce), Arrays.hashCode(serverCertificate), serverEndpoints, serverSoftwareCertificates, serverSignature, maxRequestMessageSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSessionResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    authenticationToken: ").append(toIndentedString(authenticationToken)).append("\n");
    sb.append("    revisedSessionTimeout: ").append(toIndentedString(revisedSessionTimeout)).append("\n");
    sb.append("    serverNonce: ").append(toIndentedString(serverNonce)).append("\n");
    sb.append("    serverCertificate: ").append(toIndentedString(serverCertificate)).append("\n");
    sb.append("    serverEndpoints: ").append(toIndentedString(serverEndpoints)).append("\n");
    sb.append("    serverSoftwareCertificates: ").append(toIndentedString(serverSoftwareCertificates)).append("\n");
    sb.append("    serverSignature: ").append(toIndentedString(serverSignature)).append("\n");
    sb.append("    maxRequestMessageSize: ").append(toIndentedString(maxRequestMessageSize)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2).
 */

@Schema(name = "CreateSessionRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.2/#5.7.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CreateSessionRequest {

  private @Nullable RequestHeader requestHeader;

  private @Nullable ApplicationDescription clientDescription;

  private @Nullable String serverUri;

  private @Nullable String endpointUrl;

  private @Nullable String sessionName;

  private @Nullable byte[] clientNonce;

  private @Nullable byte[] clientCertificate;

  private Double requestedSessionTimeout = 0d;

  private Long maxResponseMessageSize = 0l;

  public CreateSessionRequest requestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
    return this;
  }

  /**
   * Get requestHeader
   * @return requestHeader
   */
  @Valid 
  @Schema(name = "RequestHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHeader")
  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public void setRequestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
  }

  public CreateSessionRequest clientDescription(ApplicationDescription clientDescription) {
    this.clientDescription = clientDescription;
    return this;
  }

  /**
   * Get clientDescription
   * @return clientDescription
   */
  @Valid 
  @Schema(name = "ClientDescription", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientDescription")
  public ApplicationDescription getClientDescription() {
    return clientDescription;
  }

  public void setClientDescription(ApplicationDescription clientDescription) {
    this.clientDescription = clientDescription;
  }

  public CreateSessionRequest serverUri(String serverUri) {
    this.serverUri = serverUri;
    return this;
  }

  /**
   * Get serverUri
   * @return serverUri
   */
  
  @Schema(name = "ServerUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerUri")
  public String getServerUri() {
    return serverUri;
  }

  public void setServerUri(String serverUri) {
    this.serverUri = serverUri;
  }

  public CreateSessionRequest endpointUrl(String endpointUrl) {
    this.endpointUrl = endpointUrl;
    return this;
  }

  /**
   * Get endpointUrl
   * @return endpointUrl
   */
  
  @Schema(name = "EndpointUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("EndpointUrl")
  public String getEndpointUrl() {
    return endpointUrl;
  }

  public void setEndpointUrl(String endpointUrl) {
    this.endpointUrl = endpointUrl;
  }

  public CreateSessionRequest sessionName(String sessionName) {
    this.sessionName = sessionName;
    return this;
  }

  /**
   * Get sessionName
   * @return sessionName
   */
  
  @Schema(name = "SessionName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SessionName")
  public String getSessionName() {
    return sessionName;
  }

  public void setSessionName(String sessionName) {
    this.sessionName = sessionName;
  }

  public CreateSessionRequest clientNonce(byte[] clientNonce) {
    this.clientNonce = clientNonce;
    return this;
  }

  /**
   * Get clientNonce
   * @return clientNonce
   */
  
  @Schema(name = "ClientNonce", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientNonce")
  public byte[] getClientNonce() {
    return clientNonce;
  }

  public void setClientNonce(byte[] clientNonce) {
    this.clientNonce = clientNonce;
  }

  public CreateSessionRequest clientCertificate(byte[] clientCertificate) {
    this.clientCertificate = clientCertificate;
    return this;
  }

  /**
   * Get clientCertificate
   * @return clientCertificate
   */
  
  @Schema(name = "ClientCertificate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientCertificate")
  public byte[] getClientCertificate() {
    return clientCertificate;
  }

  public void setClientCertificate(byte[] clientCertificate) {
    this.clientCertificate = clientCertificate;
  }

  public CreateSessionRequest requestedSessionTimeout(Double requestedSessionTimeout) {
    this.requestedSessionTimeout = requestedSessionTimeout;
    return this;
  }

  /**
   * Get requestedSessionTimeout
   * @return requestedSessionTimeout
   */
  
  @Schema(name = "RequestedSessionTimeout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedSessionTimeout")
  public Double getRequestedSessionTimeout() {
    return requestedSessionTimeout;
  }

  public void setRequestedSessionTimeout(Double requestedSessionTimeout) {
    this.requestedSessionTimeout = requestedSessionTimeout;
  }

  public CreateSessionRequest maxResponseMessageSize(Long maxResponseMessageSize) {
    this.maxResponseMessageSize = maxResponseMessageSize;
    return this;
  }

  /**
   * Get maxResponseMessageSize
   * minimum: 0
   * maximum: 4294967295
   * @return maxResponseMessageSize
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxResponseMessageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxResponseMessageSize")
  public Long getMaxResponseMessageSize() {
    return maxResponseMessageSize;
  }

  public void setMaxResponseMessageSize(Long maxResponseMessageSize) {
    this.maxResponseMessageSize = maxResponseMessageSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateSessionRequest createSessionRequest = (CreateSessionRequest) o;
    return Objects.equals(this.requestHeader, createSessionRequest.requestHeader) &&
        Objects.equals(this.clientDescription, createSessionRequest.clientDescription) &&
        Objects.equals(this.serverUri, createSessionRequest.serverUri) &&
        Objects.equals(this.endpointUrl, createSessionRequest.endpointUrl) &&
        Objects.equals(this.sessionName, createSessionRequest.sessionName) &&
        Arrays.equals(this.clientNonce, createSessionRequest.clientNonce) &&
        Arrays.equals(this.clientCertificate, createSessionRequest.clientCertificate) &&
        Objects.equals(this.requestedSessionTimeout, createSessionRequest.requestedSessionTimeout) &&
        Objects.equals(this.maxResponseMessageSize, createSessionRequest.maxResponseMessageSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, clientDescription, serverUri, endpointUrl, sessionName, Arrays.hashCode(clientNonce), Arrays.hashCode(clientCertificate), requestedSessionTimeout, maxResponseMessageSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateSessionRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    clientDescription: ").append(toIndentedString(clientDescription)).append("\n");
    sb.append("    serverUri: ").append(toIndentedString(serverUri)).append("\n");
    sb.append("    endpointUrl: ").append(toIndentedString(endpointUrl)).append("\n");
    sb.append("    sessionName: ").append(toIndentedString(sessionName)).append("\n");
    sb.append("    clientNonce: ").append(toIndentedString(clientNonce)).append("\n");
    sb.append("    clientCertificate: ").append(toIndentedString(clientCertificate)).append("\n");
    sb.append("    requestedSessionTimeout: ").append(toIndentedString(requestedSessionTimeout)).append("\n");
    sb.append("    maxResponseMessageSize: ").append(toIndentedString(maxResponseMessageSize)).append("\n");
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


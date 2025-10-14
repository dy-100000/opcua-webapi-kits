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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.14).
 */

@Schema(name = "EndpointDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.14).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EndpointDescription {

  private @Nullable String endpointUrl;

  private @Nullable ApplicationDescription server;

  private @Nullable byte[] serverCertificate;

  private @Nullable Integer securityMode;

  private @Nullable String securityPolicyUri;

  @Valid
  private List<@Valid UserTokenPolicy> userIdentityTokens = new ArrayList<>();

  private @Nullable String transportProfileUri;

  private Integer securityLevel = 0;

  public EndpointDescription endpointUrl(String endpointUrl) {
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

  public EndpointDescription server(ApplicationDescription server) {
    this.server = server;
    return this;
  }

  /**
   * Get server
   * @return server
   */
  @Valid 
  @Schema(name = "Server", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Server")
  public ApplicationDescription getServer() {
    return server;
  }

  public void setServer(ApplicationDescription server) {
    this.server = server;
  }

  public EndpointDescription serverCertificate(byte[] serverCertificate) {
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

  public EndpointDescription securityMode(Integer securityMode) {
    this.securityMode = securityMode;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.10).
   * @return securityMode
   */
  
  @Schema(name = "SecurityMode", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.10).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityMode")
  public Integer getSecurityMode() {
    return securityMode;
  }

  public void setSecurityMode(Integer securityMode) {
    this.securityMode = securityMode;
  }

  public EndpointDescription securityPolicyUri(String securityPolicyUri) {
    this.securityPolicyUri = securityPolicyUri;
    return this;
  }

  /**
   * Get securityPolicyUri
   * @return securityPolicyUri
   */
  
  @Schema(name = "SecurityPolicyUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityPolicyUri")
  public String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  public void setSecurityPolicyUri(String securityPolicyUri) {
    this.securityPolicyUri = securityPolicyUri;
  }

  public EndpointDescription userIdentityTokens(List<@Valid UserTokenPolicy> userIdentityTokens) {
    this.userIdentityTokens = userIdentityTokens;
    return this;
  }

  public EndpointDescription addUserIdentityTokensItem(UserTokenPolicy userIdentityTokensItem) {
    if (this.userIdentityTokens == null) {
      this.userIdentityTokens = new ArrayList<>();
    }
    this.userIdentityTokens.add(userIdentityTokensItem);
    return this;
  }

  /**
   * Get userIdentityTokens
   * @return userIdentityTokens
   */
  @Valid 
  @Schema(name = "UserIdentityTokens", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UserIdentityTokens")
  public List<@Valid UserTokenPolicy> getUserIdentityTokens() {
    return userIdentityTokens;
  }

  public void setUserIdentityTokens(List<@Valid UserTokenPolicy> userIdentityTokens) {
    this.userIdentityTokens = userIdentityTokens;
  }

  public EndpointDescription transportProfileUri(String transportProfileUri) {
    this.transportProfileUri = transportProfileUri;
    return this;
  }

  /**
   * Get transportProfileUri
   * @return transportProfileUri
   */
  
  @Schema(name = "TransportProfileUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TransportProfileUri")
  public String getTransportProfileUri() {
    return transportProfileUri;
  }

  public void setTransportProfileUri(String transportProfileUri) {
    this.transportProfileUri = transportProfileUri;
  }

  public EndpointDescription securityLevel(Integer securityLevel) {
    this.securityLevel = securityLevel;
    return this;
  }

  /**
   * Get securityLevel
   * minimum: 0
   * maximum: 255
   * @return securityLevel
   */
  @Min(0) @Max(255) 
  @Schema(name = "SecurityLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityLevel")
  public Integer getSecurityLevel() {
    return securityLevel;
  }

  public void setSecurityLevel(Integer securityLevel) {
    this.securityLevel = securityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointDescription endpointDescription = (EndpointDescription) o;
    return Objects.equals(this.endpointUrl, endpointDescription.endpointUrl) &&
        Objects.equals(this.server, endpointDescription.server) &&
        Arrays.equals(this.serverCertificate, endpointDescription.serverCertificate) &&
        Objects.equals(this.securityMode, endpointDescription.securityMode) &&
        Objects.equals(this.securityPolicyUri, endpointDescription.securityPolicyUri) &&
        Objects.equals(this.userIdentityTokens, endpointDescription.userIdentityTokens) &&
        Objects.equals(this.transportProfileUri, endpointDescription.transportProfileUri) &&
        Objects.equals(this.securityLevel, endpointDescription.securityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endpointUrl, server, Arrays.hashCode(serverCertificate), securityMode, securityPolicyUri, userIdentityTokens, transportProfileUri, securityLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointDescription {\n");
    sb.append("    endpointUrl: ").append(toIndentedString(endpointUrl)).append("\n");
    sb.append("    server: ").append(toIndentedString(server)).append("\n");
    sb.append("    serverCertificate: ").append(toIndentedString(serverCertificate)).append("\n");
    sb.append("    securityMode: ").append(toIndentedString(securityMode)).append("\n");
    sb.append("    securityPolicyUri: ").append(toIndentedString(securityPolicyUri)).append("\n");
    sb.append("    userIdentityTokens: ").append(toIndentedString(userIdentityTokens)).append("\n");
    sb.append("    transportProfileUri: ").append(toIndentedString(transportProfileUri)).append("\n");
    sb.append("    securityLevel: ").append(toIndentedString(securityLevel)).append("\n");
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


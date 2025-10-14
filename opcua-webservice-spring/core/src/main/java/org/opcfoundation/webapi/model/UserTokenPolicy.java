package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.42).
 */

@Schema(name = "UserTokenPolicy", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.42).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UserTokenPolicy {

  private @Nullable String policyId;

  private @Nullable Integer tokenType;

  private @Nullable String issuedTokenType;

  private @Nullable String issuerEndpointUrl;

  private @Nullable String securityPolicyUri;

  public UserTokenPolicy policyId(String policyId) {
    this.policyId = policyId;
    return this;
  }

  /**
   * Get policyId
   * @return policyId
   */
  
  @Schema(name = "PolicyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PolicyId")
  public String getPolicyId() {
    return policyId;
  }

  public void setPolicyId(String policyId) {
    this.policyId = policyId;
  }

  public UserTokenPolicy tokenType(Integer tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.43).
   * @return tokenType
   */
  
  @Schema(name = "TokenType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.43).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TokenType")
  public Integer getTokenType() {
    return tokenType;
  }

  public void setTokenType(Integer tokenType) {
    this.tokenType = tokenType;
  }

  public UserTokenPolicy issuedTokenType(String issuedTokenType) {
    this.issuedTokenType = issuedTokenType;
    return this;
  }

  /**
   * Get issuedTokenType
   * @return issuedTokenType
   */
  
  @Schema(name = "IssuedTokenType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IssuedTokenType")
  public String getIssuedTokenType() {
    return issuedTokenType;
  }

  public void setIssuedTokenType(String issuedTokenType) {
    this.issuedTokenType = issuedTokenType;
  }

  public UserTokenPolicy issuerEndpointUrl(String issuerEndpointUrl) {
    this.issuerEndpointUrl = issuerEndpointUrl;
    return this;
  }

  /**
   * Get issuerEndpointUrl
   * @return issuerEndpointUrl
   */
  
  @Schema(name = "IssuerEndpointUrl", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("IssuerEndpointUrl")
  public String getIssuerEndpointUrl() {
    return issuerEndpointUrl;
  }

  public void setIssuerEndpointUrl(String issuerEndpointUrl) {
    this.issuerEndpointUrl = issuerEndpointUrl;
  }

  public UserTokenPolicy securityPolicyUri(String securityPolicyUri) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserTokenPolicy userTokenPolicy = (UserTokenPolicy) o;
    return Objects.equals(this.policyId, userTokenPolicy.policyId) &&
        Objects.equals(this.tokenType, userTokenPolicy.tokenType) &&
        Objects.equals(this.issuedTokenType, userTokenPolicy.issuedTokenType) &&
        Objects.equals(this.issuerEndpointUrl, userTokenPolicy.issuerEndpointUrl) &&
        Objects.equals(this.securityPolicyUri, userTokenPolicy.securityPolicyUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyId, tokenType, issuedTokenType, issuerEndpointUrl, securityPolicyUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserTokenPolicy {\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    issuedTokenType: ").append(toIndentedString(issuedTokenType)).append("\n");
    sb.append("    issuerEndpointUrl: ").append(toIndentedString(issuerEndpointUrl)).append("\n");
    sb.append("    securityPolicyUri: ").append(toIndentedString(securityPolicyUri)).append("\n");
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


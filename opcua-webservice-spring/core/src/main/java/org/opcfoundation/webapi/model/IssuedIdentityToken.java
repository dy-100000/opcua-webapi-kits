package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.2).
 */

@Schema(name = "IssuedIdentityToken", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class IssuedIdentityToken {

  private @Nullable byte[] tokenData;

  private @Nullable String encryptionAlgorithm;

  private @Nullable String policyId;

  public IssuedIdentityToken tokenData(byte[] tokenData) {
    this.tokenData = tokenData;
    return this;
  }

  /**
   * Get tokenData
   * @return tokenData
   */
  
  @Schema(name = "TokenData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TokenData")
  public byte[] getTokenData() {
    return tokenData;
  }

  public void setTokenData(byte[] tokenData) {
    this.tokenData = tokenData;
  }

  public IssuedIdentityToken encryptionAlgorithm(String encryptionAlgorithm) {
    this.encryptionAlgorithm = encryptionAlgorithm;
    return this;
  }

  /**
   * Get encryptionAlgorithm
   * @return encryptionAlgorithm
   */
  
  @Schema(name = "EncryptionAlgorithm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("EncryptionAlgorithm")
  public String getEncryptionAlgorithm() {
    return encryptionAlgorithm;
  }

  public void setEncryptionAlgorithm(String encryptionAlgorithm) {
    this.encryptionAlgorithm = encryptionAlgorithm;
  }

  public IssuedIdentityToken policyId(String policyId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IssuedIdentityToken issuedIdentityToken = (IssuedIdentityToken) o;
    return Arrays.equals(this.tokenData, issuedIdentityToken.tokenData) &&
        Objects.equals(this.encryptionAlgorithm, issuedIdentityToken.encryptionAlgorithm) &&
        Objects.equals(this.policyId, issuedIdentityToken.policyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(tokenData), encryptionAlgorithm, policyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IssuedIdentityToken {\n");
    sb.append("    tokenData: ").append(toIndentedString(tokenData)).append("\n");
    sb.append("    encryptionAlgorithm: ").append(toIndentedString(encryptionAlgorithm)).append("\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
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


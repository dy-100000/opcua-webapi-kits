package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.4).
 */

@Schema(name = "X509IdentityToken", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class X509IdentityToken {

  private @Nullable byte[] certificateData;

  private @Nullable String policyId;

  public X509IdentityToken certificateData(byte[] certificateData) {
    this.certificateData = certificateData;
    return this;
  }

  /**
   * Get certificateData
   * @return certificateData
   */
  
  @Schema(name = "CertificateData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("CertificateData")
  public byte[] getCertificateData() {
    return certificateData;
  }

  public void setCertificateData(byte[] certificateData) {
    this.certificateData = certificateData;
  }

  public X509IdentityToken policyId(String policyId) {
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
    X509IdentityToken x509IdentityToken = (X509IdentityToken) o;
    return Arrays.equals(this.certificateData, x509IdentityToken.certificateData) &&
        Objects.equals(this.policyId, x509IdentityToken.policyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(certificateData), policyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class X509IdentityToken {\n");
    sb.append("    certificateData: ").append(toIndentedString(certificateData)).append("\n");
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


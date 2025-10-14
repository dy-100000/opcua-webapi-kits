package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.13).
 */

@Schema(name = "SignedSoftwareCertificate", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.13).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SignedSoftwareCertificate {

  private @Nullable byte[] certificateData;

  private @Nullable byte[] signature;

  public SignedSoftwareCertificate certificateData(byte[] certificateData) {
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

  public SignedSoftwareCertificate signature(byte[] signature) {
    this.signature = signature;
    return this;
  }

  /**
   * Get signature
   * @return signature
   */
  
  @Schema(name = "Signature", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Signature")
  public byte[] getSignature() {
    return signature;
  }

  public void setSignature(byte[] signature) {
    this.signature = signature;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignedSoftwareCertificate signedSoftwareCertificate = (SignedSoftwareCertificate) o;
    return Arrays.equals(this.certificateData, signedSoftwareCertificate.certificateData) &&
        Arrays.equals(this.signature, signedSoftwareCertificate.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(certificateData), Arrays.hashCode(signature));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignedSoftwareCertificate {\n");
    sb.append("    certificateData: ").append(toIndentedString(certificateData)).append("\n");
    sb.append("    signature: ").append(toIndentedString(signature)).append("\n");
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


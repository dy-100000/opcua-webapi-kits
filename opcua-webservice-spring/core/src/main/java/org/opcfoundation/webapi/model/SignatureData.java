package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.37).
 */

@Schema(name = "SignatureData", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.37).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SignatureData {

  private @Nullable String algorithm;

  private @Nullable byte[] signature;

  public SignatureData algorithm(String algorithm) {
    this.algorithm = algorithm;
    return this;
  }

  /**
   * Get algorithm
   * @return algorithm
   */
  
  @Schema(name = "Algorithm", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Algorithm")
  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public SignatureData signature(byte[] signature) {
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
    SignatureData signatureData = (SignatureData) o;
    return Objects.equals(this.algorithm, signatureData.algorithm) &&
        Arrays.equals(this.signature, signatureData.signature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(algorithm, Arrays.hashCode(signature));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignatureData {\n");
    sb.append("    algorithm: ").append(toIndentedString(algorithm)).append("\n");
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


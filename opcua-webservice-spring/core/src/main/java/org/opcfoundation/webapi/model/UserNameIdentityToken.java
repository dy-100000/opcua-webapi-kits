package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.3).
 */

@Schema(name = "UserNameIdentityToken", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UserNameIdentityToken {

  private @Nullable String userName;

  private @Nullable byte[] password;

  private @Nullable String encryptionAlgorithm;

  private @Nullable String policyId;

  public UserNameIdentityToken userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
   */
  
  @Schema(name = "UserName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UserName")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public UserNameIdentityToken password(byte[] password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  
  @Schema(name = "Password", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Password")
  public byte[] getPassword() {
    return password;
  }

  public void setPassword(byte[] password) {
    this.password = password;
  }

  public UserNameIdentityToken encryptionAlgorithm(String encryptionAlgorithm) {
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

  public UserNameIdentityToken policyId(String policyId) {
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
    UserNameIdentityToken userNameIdentityToken = (UserNameIdentityToken) o;
    return Objects.equals(this.userName, userNameIdentityToken.userName) &&
        Arrays.equals(this.password, userNameIdentityToken.password) &&
        Objects.equals(this.encryptionAlgorithm, userNameIdentityToken.encryptionAlgorithm) &&
        Objects.equals(this.policyId, userNameIdentityToken.policyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, Arrays.hashCode(password), encryptionAlgorithm, policyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserNameIdentityToken {\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15).
 */

@Schema(name = "UserIdentityToken", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UserIdentityToken {

  private @Nullable String policyId;

  public UserIdentityToken policyId(String policyId) {
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
    UserIdentityToken userIdentityToken = (UserIdentityToken) o;
    return Objects.equals(this.policyId, userIdentityToken.policyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserIdentityToken {\n");
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


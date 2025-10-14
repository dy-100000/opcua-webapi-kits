package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.9).
 */

@Schema(name = "RolePermissionType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.9).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RolePermissionType {

  private @Nullable String roleId;

  private Long permissions = 0l;

  public RolePermissionType roleId(String roleId) {
    this.roleId = roleId;
    return this;
  }

  /**
   * Get roleId
   * @return roleId
   */
  
  @Schema(name = "RoleId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RoleId")
  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public RolePermissionType permissions(Long permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * Get permissions
   * minimum: 0
   * maximum: 4294967295
   * @return permissions
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "Permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Permissions")
  public Long getPermissions() {
    return permissions;
  }

  public void setPermissions(Long permissions) {
    this.permissions = permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RolePermissionType rolePermissionType = (RolePermissionType) o;
    return Objects.equals(this.roleId, rolePermissionType.roleId) &&
        Objects.equals(this.permissions, rolePermissionType.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roleId, permissions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RolePermissionType {\n");
    sb.append("    roleId: ").append(toIndentedString(roleId)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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


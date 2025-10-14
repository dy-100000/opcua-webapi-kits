package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.2).
 */

@Schema(name = "SecurityGroupDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class SecurityGroupDataType {

  private @Nullable String name;

  @Valid
  private List<String> securityGroupFolder = new ArrayList<>();

  private Double keyLifetime = 0d;

  private @Nullable String securityPolicyUri;

  private Long maxFutureKeyCount = 0l;

  private Long maxPastKeyCount = 0l;

  private @Nullable String securityGroupId;

  @Valid
  private List<@Valid RolePermissionType> rolePermissions = new ArrayList<>();

  @Valid
  private List<@Valid KeyValuePair> groupProperties = new ArrayList<>();

  public SecurityGroupDataType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "Name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SecurityGroupDataType securityGroupFolder(List<String> securityGroupFolder) {
    this.securityGroupFolder = securityGroupFolder;
    return this;
  }

  public SecurityGroupDataType addSecurityGroupFolderItem(String securityGroupFolderItem) {
    if (this.securityGroupFolder == null) {
      this.securityGroupFolder = new ArrayList<>();
    }
    this.securityGroupFolder.add(securityGroupFolderItem);
    return this;
  }

  /**
   * Get securityGroupFolder
   * @return securityGroupFolder
   */
  
  @Schema(name = "SecurityGroupFolder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityGroupFolder")
  public List<String> getSecurityGroupFolder() {
    return securityGroupFolder;
  }

  public void setSecurityGroupFolder(List<String> securityGroupFolder) {
    this.securityGroupFolder = securityGroupFolder;
  }

  public SecurityGroupDataType keyLifetime(Double keyLifetime) {
    this.keyLifetime = keyLifetime;
    return this;
  }

  /**
   * Get keyLifetime
   * @return keyLifetime
   */
  
  @Schema(name = "KeyLifetime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("KeyLifetime")
  public Double getKeyLifetime() {
    return keyLifetime;
  }

  public void setKeyLifetime(Double keyLifetime) {
    this.keyLifetime = keyLifetime;
  }

  public SecurityGroupDataType securityPolicyUri(String securityPolicyUri) {
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

  public SecurityGroupDataType maxFutureKeyCount(Long maxFutureKeyCount) {
    this.maxFutureKeyCount = maxFutureKeyCount;
    return this;
  }

  /**
   * Get maxFutureKeyCount
   * minimum: 0
   * maximum: 4294967295
   * @return maxFutureKeyCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxFutureKeyCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxFutureKeyCount")
  public Long getMaxFutureKeyCount() {
    return maxFutureKeyCount;
  }

  public void setMaxFutureKeyCount(Long maxFutureKeyCount) {
    this.maxFutureKeyCount = maxFutureKeyCount;
  }

  public SecurityGroupDataType maxPastKeyCount(Long maxPastKeyCount) {
    this.maxPastKeyCount = maxPastKeyCount;
    return this;
  }

  /**
   * Get maxPastKeyCount
   * minimum: 0
   * maximum: 4294967295
   * @return maxPastKeyCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxPastKeyCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxPastKeyCount")
  public Long getMaxPastKeyCount() {
    return maxPastKeyCount;
  }

  public void setMaxPastKeyCount(Long maxPastKeyCount) {
    this.maxPastKeyCount = maxPastKeyCount;
  }

  public SecurityGroupDataType securityGroupId(String securityGroupId) {
    this.securityGroupId = securityGroupId;
    return this;
  }

  /**
   * Get securityGroupId
   * @return securityGroupId
   */
  
  @Schema(name = "SecurityGroupId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityGroupId")
  public String getSecurityGroupId() {
    return securityGroupId;
  }

  public void setSecurityGroupId(String securityGroupId) {
    this.securityGroupId = securityGroupId;
  }

  public SecurityGroupDataType rolePermissions(List<@Valid RolePermissionType> rolePermissions) {
    this.rolePermissions = rolePermissions;
    return this;
  }

  public SecurityGroupDataType addRolePermissionsItem(RolePermissionType rolePermissionsItem) {
    if (this.rolePermissions == null) {
      this.rolePermissions = new ArrayList<>();
    }
    this.rolePermissions.add(rolePermissionsItem);
    return this;
  }

  /**
   * Get rolePermissions
   * @return rolePermissions
   */
  @Valid 
  @Schema(name = "RolePermissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RolePermissions")
  public List<@Valid RolePermissionType> getRolePermissions() {
    return rolePermissions;
  }

  public void setRolePermissions(List<@Valid RolePermissionType> rolePermissions) {
    this.rolePermissions = rolePermissions;
  }

  public SecurityGroupDataType groupProperties(List<@Valid KeyValuePair> groupProperties) {
    this.groupProperties = groupProperties;
    return this;
  }

  public SecurityGroupDataType addGroupPropertiesItem(KeyValuePair groupPropertiesItem) {
    if (this.groupProperties == null) {
      this.groupProperties = new ArrayList<>();
    }
    this.groupProperties.add(groupPropertiesItem);
    return this;
  }

  /**
   * Get groupProperties
   * @return groupProperties
   */
  @Valid 
  @Schema(name = "GroupProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("GroupProperties")
  public List<@Valid KeyValuePair> getGroupProperties() {
    return groupProperties;
  }

  public void setGroupProperties(List<@Valid KeyValuePair> groupProperties) {
    this.groupProperties = groupProperties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityGroupDataType securityGroupDataType = (SecurityGroupDataType) o;
    return Objects.equals(this.name, securityGroupDataType.name) &&
        Objects.equals(this.securityGroupFolder, securityGroupDataType.securityGroupFolder) &&
        Objects.equals(this.keyLifetime, securityGroupDataType.keyLifetime) &&
        Objects.equals(this.securityPolicyUri, securityGroupDataType.securityPolicyUri) &&
        Objects.equals(this.maxFutureKeyCount, securityGroupDataType.maxFutureKeyCount) &&
        Objects.equals(this.maxPastKeyCount, securityGroupDataType.maxPastKeyCount) &&
        Objects.equals(this.securityGroupId, securityGroupDataType.securityGroupId) &&
        Objects.equals(this.rolePermissions, securityGroupDataType.rolePermissions) &&
        Objects.equals(this.groupProperties, securityGroupDataType.groupProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, securityGroupFolder, keyLifetime, securityPolicyUri, maxFutureKeyCount, maxPastKeyCount, securityGroupId, rolePermissions, groupProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityGroupDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    securityGroupFolder: ").append(toIndentedString(securityGroupFolder)).append("\n");
    sb.append("    keyLifetime: ").append(toIndentedString(keyLifetime)).append("\n");
    sb.append("    securityPolicyUri: ").append(toIndentedString(securityPolicyUri)).append("\n");
    sb.append("    maxFutureKeyCount: ").append(toIndentedString(maxFutureKeyCount)).append("\n");
    sb.append("    maxPastKeyCount: ").append(toIndentedString(maxPastKeyCount)).append("\n");
    sb.append("    securityGroupId: ").append(toIndentedString(securityGroupId)).append("\n");
    sb.append("    rolePermissions: ").append(toIndentedString(rolePermissions)).append("\n");
    sb.append("    groupProperties: ").append(toIndentedString(groupProperties)).append("\n");
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


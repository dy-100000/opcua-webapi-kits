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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.3).
 */

@Schema(name = "PubSubKeyPushTargetDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PubSubKeyPushTargetDataType {

  private @Nullable String applicationUri;

  @Valid
  private List<String> pushTargetFolder = new ArrayList<>();

  private @Nullable String endpointUrl;

  private @Nullable String securityPolicyUri;

  private @Nullable UserTokenPolicy userTokenType;

  private Integer requestedKeyCount = 0;

  private Double retryInterval = 0d;

  @Valid
  private List<@Valid KeyValuePair> pushTargetProperties = new ArrayList<>();

  @Valid
  private List<String> securityGroups = new ArrayList<>();

  public PubSubKeyPushTargetDataType applicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  /**
   * Get applicationUri
   * @return applicationUri
   */
  
  @Schema(name = "ApplicationUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ApplicationUri")
  public String getApplicationUri() {
    return applicationUri;
  }

  public void setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
  }

  public PubSubKeyPushTargetDataType pushTargetFolder(List<String> pushTargetFolder) {
    this.pushTargetFolder = pushTargetFolder;
    return this;
  }

  public PubSubKeyPushTargetDataType addPushTargetFolderItem(String pushTargetFolderItem) {
    if (this.pushTargetFolder == null) {
      this.pushTargetFolder = new ArrayList<>();
    }
    this.pushTargetFolder.add(pushTargetFolderItem);
    return this;
  }

  /**
   * Get pushTargetFolder
   * @return pushTargetFolder
   */
  
  @Schema(name = "PushTargetFolder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PushTargetFolder")
  public List<String> getPushTargetFolder() {
    return pushTargetFolder;
  }

  public void setPushTargetFolder(List<String> pushTargetFolder) {
    this.pushTargetFolder = pushTargetFolder;
  }

  public PubSubKeyPushTargetDataType endpointUrl(String endpointUrl) {
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

  public PubSubKeyPushTargetDataType securityPolicyUri(String securityPolicyUri) {
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

  public PubSubKeyPushTargetDataType userTokenType(UserTokenPolicy userTokenType) {
    this.userTokenType = userTokenType;
    return this;
  }

  /**
   * Get userTokenType
   * @return userTokenType
   */
  @Valid 
  @Schema(name = "UserTokenType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UserTokenType")
  public UserTokenPolicy getUserTokenType() {
    return userTokenType;
  }

  public void setUserTokenType(UserTokenPolicy userTokenType) {
    this.userTokenType = userTokenType;
  }

  public PubSubKeyPushTargetDataType requestedKeyCount(Integer requestedKeyCount) {
    this.requestedKeyCount = requestedKeyCount;
    return this;
  }

  /**
   * Get requestedKeyCount
   * minimum: 0
   * maximum: 65535
   * @return requestedKeyCount
   */
  @Min(0) @Max(65535) 
  @Schema(name = "RequestedKeyCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedKeyCount")
  public Integer getRequestedKeyCount() {
    return requestedKeyCount;
  }

  public void setRequestedKeyCount(Integer requestedKeyCount) {
    this.requestedKeyCount = requestedKeyCount;
  }

  public PubSubKeyPushTargetDataType retryInterval(Double retryInterval) {
    this.retryInterval = retryInterval;
    return this;
  }

  /**
   * Get retryInterval
   * @return retryInterval
   */
  
  @Schema(name = "RetryInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RetryInterval")
  public Double getRetryInterval() {
    return retryInterval;
  }

  public void setRetryInterval(Double retryInterval) {
    this.retryInterval = retryInterval;
  }

  public PubSubKeyPushTargetDataType pushTargetProperties(List<@Valid KeyValuePair> pushTargetProperties) {
    this.pushTargetProperties = pushTargetProperties;
    return this;
  }

  public PubSubKeyPushTargetDataType addPushTargetPropertiesItem(KeyValuePair pushTargetPropertiesItem) {
    if (this.pushTargetProperties == null) {
      this.pushTargetProperties = new ArrayList<>();
    }
    this.pushTargetProperties.add(pushTargetPropertiesItem);
    return this;
  }

  /**
   * Get pushTargetProperties
   * @return pushTargetProperties
   */
  @Valid 
  @Schema(name = "PushTargetProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PushTargetProperties")
  public List<@Valid KeyValuePair> getPushTargetProperties() {
    return pushTargetProperties;
  }

  public void setPushTargetProperties(List<@Valid KeyValuePair> pushTargetProperties) {
    this.pushTargetProperties = pushTargetProperties;
  }

  public PubSubKeyPushTargetDataType securityGroups(List<String> securityGroups) {
    this.securityGroups = securityGroups;
    return this;
  }

  public PubSubKeyPushTargetDataType addSecurityGroupsItem(String securityGroupsItem) {
    if (this.securityGroups == null) {
      this.securityGroups = new ArrayList<>();
    }
    this.securityGroups.add(securityGroupsItem);
    return this;
  }

  /**
   * Get securityGroups
   * @return securityGroups
   */
  
  @Schema(name = "SecurityGroups", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityGroups")
  public List<String> getSecurityGroups() {
    return securityGroups;
  }

  public void setSecurityGroups(List<String> securityGroups) {
    this.securityGroups = securityGroups;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PubSubKeyPushTargetDataType pubSubKeyPushTargetDataType = (PubSubKeyPushTargetDataType) o;
    return Objects.equals(this.applicationUri, pubSubKeyPushTargetDataType.applicationUri) &&
        Objects.equals(this.pushTargetFolder, pubSubKeyPushTargetDataType.pushTargetFolder) &&
        Objects.equals(this.endpointUrl, pubSubKeyPushTargetDataType.endpointUrl) &&
        Objects.equals(this.securityPolicyUri, pubSubKeyPushTargetDataType.securityPolicyUri) &&
        Objects.equals(this.userTokenType, pubSubKeyPushTargetDataType.userTokenType) &&
        Objects.equals(this.requestedKeyCount, pubSubKeyPushTargetDataType.requestedKeyCount) &&
        Objects.equals(this.retryInterval, pubSubKeyPushTargetDataType.retryInterval) &&
        Objects.equals(this.pushTargetProperties, pubSubKeyPushTargetDataType.pushTargetProperties) &&
        Objects.equals(this.securityGroups, pubSubKeyPushTargetDataType.securityGroups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationUri, pushTargetFolder, endpointUrl, securityPolicyUri, userTokenType, requestedKeyCount, retryInterval, pushTargetProperties, securityGroups);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PubSubKeyPushTargetDataType {\n");
    sb.append("    applicationUri: ").append(toIndentedString(applicationUri)).append("\n");
    sb.append("    pushTargetFolder: ").append(toIndentedString(pushTargetFolder)).append("\n");
    sb.append("    endpointUrl: ").append(toIndentedString(endpointUrl)).append("\n");
    sb.append("    securityPolicyUri: ").append(toIndentedString(securityPolicyUri)).append("\n");
    sb.append("    userTokenType: ").append(toIndentedString(userTokenType)).append("\n");
    sb.append("    requestedKeyCount: ").append(toIndentedString(requestedKeyCount)).append("\n");
    sb.append("    retryInterval: ").append(toIndentedString(retryInterval)).append("\n");
    sb.append("    pushTargetProperties: ").append(toIndentedString(pushTargetProperties)).append("\n");
    sb.append("    securityGroups: ").append(toIndentedString(securityGroups)).append("\n");
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


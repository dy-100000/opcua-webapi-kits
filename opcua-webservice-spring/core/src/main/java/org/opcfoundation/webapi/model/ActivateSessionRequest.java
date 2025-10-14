package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2).
 */

@Schema(name = "ActivateSessionRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.3/#5.7.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ActivateSessionRequest {

  private @Nullable RequestHeader requestHeader;

  private @Nullable SignatureData clientSignature;

  @Valid
  private List<@Valid SignedSoftwareCertificate> clientSoftwareCertificates = new ArrayList<>();

  @Valid
  private List<String> localeIds = new ArrayList<>();

  private @Nullable ExtensionObject userIdentityToken;

  private @Nullable SignatureData userTokenSignature;

  public ActivateSessionRequest requestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
    return this;
  }

  /**
   * Get requestHeader
   * @return requestHeader
   */
  @Valid 
  @Schema(name = "RequestHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHeader")
  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public void setRequestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
  }

  public ActivateSessionRequest clientSignature(SignatureData clientSignature) {
    this.clientSignature = clientSignature;
    return this;
  }

  /**
   * Get clientSignature
   * @return clientSignature
   */
  @Valid 
  @Schema(name = "ClientSignature", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientSignature")
  public SignatureData getClientSignature() {
    return clientSignature;
  }

  public void setClientSignature(SignatureData clientSignature) {
    this.clientSignature = clientSignature;
  }

  public ActivateSessionRequest clientSoftwareCertificates(List<@Valid SignedSoftwareCertificate> clientSoftwareCertificates) {
    this.clientSoftwareCertificates = clientSoftwareCertificates;
    return this;
  }

  public ActivateSessionRequest addClientSoftwareCertificatesItem(SignedSoftwareCertificate clientSoftwareCertificatesItem) {
    if (this.clientSoftwareCertificates == null) {
      this.clientSoftwareCertificates = new ArrayList<>();
    }
    this.clientSoftwareCertificates.add(clientSoftwareCertificatesItem);
    return this;
  }

  /**
   * Get clientSoftwareCertificates
   * @return clientSoftwareCertificates
   */
  @Valid 
  @Schema(name = "ClientSoftwareCertificates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientSoftwareCertificates")
  public List<@Valid SignedSoftwareCertificate> getClientSoftwareCertificates() {
    return clientSoftwareCertificates;
  }

  public void setClientSoftwareCertificates(List<@Valid SignedSoftwareCertificate> clientSoftwareCertificates) {
    this.clientSoftwareCertificates = clientSoftwareCertificates;
  }

  public ActivateSessionRequest localeIds(List<String> localeIds) {
    this.localeIds = localeIds;
    return this;
  }

  public ActivateSessionRequest addLocaleIdsItem(String localeIdsItem) {
    if (this.localeIds == null) {
      this.localeIds = new ArrayList<>();
    }
    this.localeIds.add(localeIdsItem);
    return this;
  }

  /**
   * Get localeIds
   * @return localeIds
   */
  
  @Schema(name = "LocaleIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("LocaleIds")
  public List<String> getLocaleIds() {
    return localeIds;
  }

  public void setLocaleIds(List<String> localeIds) {
    this.localeIds = localeIds;
  }

  public ActivateSessionRequest userIdentityToken(ExtensionObject userIdentityToken) {
    this.userIdentityToken = userIdentityToken;
    return this;
  }

  /**
   * Get userIdentityToken
   * @return userIdentityToken
   */
  @Valid 
  @Schema(name = "UserIdentityToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UserIdentityToken")
  public ExtensionObject getUserIdentityToken() {
    return userIdentityToken;
  }

  public void setUserIdentityToken(ExtensionObject userIdentityToken) {
    this.userIdentityToken = userIdentityToken;
  }

  public ActivateSessionRequest userTokenSignature(SignatureData userTokenSignature) {
    this.userTokenSignature = userTokenSignature;
    return this;
  }

  /**
   * Get userTokenSignature
   * @return userTokenSignature
   */
  @Valid 
  @Schema(name = "UserTokenSignature", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UserTokenSignature")
  public SignatureData getUserTokenSignature() {
    return userTokenSignature;
  }

  public void setUserTokenSignature(SignatureData userTokenSignature) {
    this.userTokenSignature = userTokenSignature;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivateSessionRequest activateSessionRequest = (ActivateSessionRequest) o;
    return Objects.equals(this.requestHeader, activateSessionRequest.requestHeader) &&
        Objects.equals(this.clientSignature, activateSessionRequest.clientSignature) &&
        Objects.equals(this.clientSoftwareCertificates, activateSessionRequest.clientSoftwareCertificates) &&
        Objects.equals(this.localeIds, activateSessionRequest.localeIds) &&
        Objects.equals(this.userIdentityToken, activateSessionRequest.userIdentityToken) &&
        Objects.equals(this.userTokenSignature, activateSessionRequest.userTokenSignature);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, clientSignature, clientSoftwareCertificates, localeIds, userIdentityToken, userTokenSignature);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivateSessionRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    clientSignature: ").append(toIndentedString(clientSignature)).append("\n");
    sb.append("    clientSoftwareCertificates: ").append(toIndentedString(clientSoftwareCertificates)).append("\n");
    sb.append("    localeIds: ").append(toIndentedString(localeIds)).append("\n");
    sb.append("    userIdentityToken: ").append(toIndentedString(userIdentityToken)).append("\n");
    sb.append("    userTokenSignature: ").append(toIndentedString(userTokenSignature)).append("\n");
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


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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.4/#5.5.4.2).
 */

@Schema(name = "GetEndpointsRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.4/#5.5.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class GetEndpointsRequest {

  private @Nullable RequestHeader requestHeader;

  private @Nullable String endpointUrl;

  @Valid
  private List<String> localeIds = new ArrayList<>();

  @Valid
  private List<String> profileUris = new ArrayList<>();

  public GetEndpointsRequest requestHeader(RequestHeader requestHeader) {
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

  public GetEndpointsRequest endpointUrl(String endpointUrl) {
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

  public GetEndpointsRequest localeIds(List<String> localeIds) {
    this.localeIds = localeIds;
    return this;
  }

  public GetEndpointsRequest addLocaleIdsItem(String localeIdsItem) {
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

  public GetEndpointsRequest profileUris(List<String> profileUris) {
    this.profileUris = profileUris;
    return this;
  }

  public GetEndpointsRequest addProfileUrisItem(String profileUrisItem) {
    if (this.profileUris == null) {
      this.profileUris = new ArrayList<>();
    }
    this.profileUris.add(profileUrisItem);
    return this;
  }

  /**
   * Get profileUris
   * @return profileUris
   */
  
  @Schema(name = "ProfileUris", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ProfileUris")
  public List<String> getProfileUris() {
    return profileUris;
  }

  public void setProfileUris(List<String> profileUris) {
    this.profileUris = profileUris;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEndpointsRequest getEndpointsRequest = (GetEndpointsRequest) o;
    return Objects.equals(this.requestHeader, getEndpointsRequest.requestHeader) &&
        Objects.equals(this.endpointUrl, getEndpointsRequest.endpointUrl) &&
        Objects.equals(this.localeIds, getEndpointsRequest.localeIds) &&
        Objects.equals(this.profileUris, getEndpointsRequest.profileUris);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, endpointUrl, localeIds, profileUris);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEndpointsRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    endpointUrl: ").append(toIndentedString(endpointUrl)).append("\n");
    sb.append("    localeIds: ").append(toIndentedString(localeIds)).append("\n");
    sb.append("    profileUris: ").append(toIndentedString(profileUris)).append("\n");
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


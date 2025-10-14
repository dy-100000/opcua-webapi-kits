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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2).
 */

@Schema(name = "FindServersRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class FindServersRequest {

  private @Nullable RequestHeader requestHeader;

  private @Nullable String endpointUrl;

  @Valid
  private List<String> localeIds = new ArrayList<>();

  @Valid
  private List<String> serverUris = new ArrayList<>();

  public FindServersRequest requestHeader(RequestHeader requestHeader) {
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

  public FindServersRequest endpointUrl(String endpointUrl) {
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

  public FindServersRequest localeIds(List<String> localeIds) {
    this.localeIds = localeIds;
    return this;
  }

  public FindServersRequest addLocaleIdsItem(String localeIdsItem) {
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

  public FindServersRequest serverUris(List<String> serverUris) {
    this.serverUris = serverUris;
    return this;
  }

  public FindServersRequest addServerUrisItem(String serverUrisItem) {
    if (this.serverUris == null) {
      this.serverUris = new ArrayList<>();
    }
    this.serverUris.add(serverUrisItem);
    return this;
  }

  /**
   * Get serverUris
   * @return serverUris
   */
  
  @Schema(name = "ServerUris", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerUris")
  public List<String> getServerUris() {
    return serverUris;
  }

  public void setServerUris(List<String> serverUris) {
    this.serverUris = serverUris;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FindServersRequest findServersRequest = (FindServersRequest) o;
    return Objects.equals(this.requestHeader, findServersRequest.requestHeader) &&
        Objects.equals(this.endpointUrl, findServersRequest.endpointUrl) &&
        Objects.equals(this.localeIds, findServersRequest.localeIds) &&
        Objects.equals(this.serverUris, findServersRequest.serverUris);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, endpointUrl, localeIds, serverUris);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindServersRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    endpointUrl: ").append(toIndentedString(endpointUrl)).append("\n");
    sb.append("    localeIds: ").append(toIndentedString(localeIds)).append("\n");
    sb.append("    serverUris: ").append(toIndentedString(serverUris)).append("\n");
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


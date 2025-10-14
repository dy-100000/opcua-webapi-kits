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

@Schema(name = "GetEndpointsResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.4/#5.5.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class GetEndpointsResponse {

  private @Nullable ResponseHeader responseHeader;

  @Valid
  private List<@Valid EndpointDescription> endpoints = new ArrayList<>();

  public GetEndpointsResponse responseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
    return this;
  }

  /**
   * Get responseHeader
   * @return responseHeader
   */
  @Valid 
  @Schema(name = "ResponseHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResponseHeader")
  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public void setResponseHeader(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
  }

  public GetEndpointsResponse endpoints(List<@Valid EndpointDescription> endpoints) {
    this.endpoints = endpoints;
    return this;
  }

  public GetEndpointsResponse addEndpointsItem(EndpointDescription endpointsItem) {
    if (this.endpoints == null) {
      this.endpoints = new ArrayList<>();
    }
    this.endpoints.add(endpointsItem);
    return this;
  }

  /**
   * Get endpoints
   * @return endpoints
   */
  @Valid 
  @Schema(name = "Endpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Endpoints")
  public List<@Valid EndpointDescription> getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(List<@Valid EndpointDescription> endpoints) {
    this.endpoints = endpoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEndpointsResponse getEndpointsResponse = (GetEndpointsResponse) o;
    return Objects.equals(this.responseHeader, getEndpointsResponse.responseHeader) &&
        Objects.equals(this.endpoints, getEndpointsResponse.endpoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, endpoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEndpointsResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
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


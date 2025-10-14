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

@Schema(name = "FindServersResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.2/#5.5.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class FindServersResponse {

  private @Nullable ResponseHeader responseHeader;

  @Valid
  private List<@Valid ApplicationDescription> servers = new ArrayList<>();

  public FindServersResponse responseHeader(ResponseHeader responseHeader) {
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

  public FindServersResponse servers(List<@Valid ApplicationDescription> servers) {
    this.servers = servers;
    return this;
  }

  public FindServersResponse addServersItem(ApplicationDescription serversItem) {
    if (this.servers == null) {
      this.servers = new ArrayList<>();
    }
    this.servers.add(serversItem);
    return this;
  }

  /**
   * Get servers
   * @return servers
   */
  @Valid 
  @Schema(name = "Servers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Servers")
  public List<@Valid ApplicationDescription> getServers() {
    return servers;
  }

  public void setServers(List<@Valid ApplicationDescription> servers) {
    this.servers = servers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FindServersResponse findServersResponse = (FindServersResponse) o;
    return Objects.equals(this.responseHeader, findServersResponse.responseHeader) &&
        Objects.equals(this.servers, findServersResponse.servers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, servers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindServersResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    servers: ").append(toIndentedString(servers)).append("\n");
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


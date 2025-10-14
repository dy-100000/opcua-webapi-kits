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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.6/#5.9.6.2).
 */

@Schema(name = "UnregisterNodesRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.6/#5.9.6.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UnregisterNodesRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<String> nodesToUnregister = new ArrayList<>();

  public UnregisterNodesRequest requestHeader(RequestHeader requestHeader) {
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

  public UnregisterNodesRequest nodesToUnregister(List<String> nodesToUnregister) {
    this.nodesToUnregister = nodesToUnregister;
    return this;
  }

  public UnregisterNodesRequest addNodesToUnregisterItem(String nodesToUnregisterItem) {
    if (this.nodesToUnregister == null) {
      this.nodesToUnregister = new ArrayList<>();
    }
    this.nodesToUnregister.add(nodesToUnregisterItem);
    return this;
  }

  /**
   * Get nodesToUnregister
   * @return nodesToUnregister
   */
  
  @Schema(name = "NodesToUnregister", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodesToUnregister")
  public List<String> getNodesToUnregister() {
    return nodesToUnregister;
  }

  public void setNodesToUnregister(List<String> nodesToUnregister) {
    this.nodesToUnregister = nodesToUnregister;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnregisterNodesRequest unregisterNodesRequest = (UnregisterNodesRequest) o;
    return Objects.equals(this.requestHeader, unregisterNodesRequest.requestHeader) &&
        Objects.equals(this.nodesToUnregister, unregisterNodesRequest.nodesToUnregister);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, nodesToUnregister);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnregisterNodesRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    nodesToUnregister: ").append(toIndentedString(nodesToUnregister)).append("\n");
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


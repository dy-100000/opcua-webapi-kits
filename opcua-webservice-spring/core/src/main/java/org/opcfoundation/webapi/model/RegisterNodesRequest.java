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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.5/#5.9.5.2).
 */

@Schema(name = "RegisterNodesRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.5/#5.9.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RegisterNodesRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<String> nodesToRegister = new ArrayList<>();

  public RegisterNodesRequest requestHeader(RequestHeader requestHeader) {
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

  public RegisterNodesRequest nodesToRegister(List<String> nodesToRegister) {
    this.nodesToRegister = nodesToRegister;
    return this;
  }

  public RegisterNodesRequest addNodesToRegisterItem(String nodesToRegisterItem) {
    if (this.nodesToRegister == null) {
      this.nodesToRegister = new ArrayList<>();
    }
    this.nodesToRegister.add(nodesToRegisterItem);
    return this;
  }

  /**
   * Get nodesToRegister
   * @return nodesToRegister
   */
  
  @Schema(name = "NodesToRegister", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodesToRegister")
  public List<String> getNodesToRegister() {
    return nodesToRegister;
  }

  public void setNodesToRegister(List<String> nodesToRegister) {
    this.nodesToRegister = nodesToRegister;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterNodesRequest registerNodesRequest = (RegisterNodesRequest) o;
    return Objects.equals(this.requestHeader, registerNodesRequest.requestHeader) &&
        Objects.equals(this.nodesToRegister, registerNodesRequest.nodesToRegister);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, nodesToRegister);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterNodesRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    nodesToRegister: ").append(toIndentedString(nodesToRegister)).append("\n");
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


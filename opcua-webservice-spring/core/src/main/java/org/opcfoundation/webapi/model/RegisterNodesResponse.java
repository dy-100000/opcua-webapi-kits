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

@Schema(name = "RegisterNodesResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.5/#5.9.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RegisterNodesResponse {

  private @Nullable ResponseHeader responseHeader;

  @Valid
  private List<String> registeredNodeIds = new ArrayList<>();

  public RegisterNodesResponse responseHeader(ResponseHeader responseHeader) {
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

  public RegisterNodesResponse registeredNodeIds(List<String> registeredNodeIds) {
    this.registeredNodeIds = registeredNodeIds;
    return this;
  }

  public RegisterNodesResponse addRegisteredNodeIdsItem(String registeredNodeIdsItem) {
    if (this.registeredNodeIds == null) {
      this.registeredNodeIds = new ArrayList<>();
    }
    this.registeredNodeIds.add(registeredNodeIdsItem);
    return this;
  }

  /**
   * Get registeredNodeIds
   * @return registeredNodeIds
   */
  
  @Schema(name = "RegisteredNodeIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RegisteredNodeIds")
  public List<String> getRegisteredNodeIds() {
    return registeredNodeIds;
  }

  public void setRegisteredNodeIds(List<String> registeredNodeIds) {
    this.registeredNodeIds = registeredNodeIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterNodesResponse registerNodesResponse = (RegisterNodesResponse) o;
    return Objects.equals(this.responseHeader, registerNodesResponse.responseHeader) &&
        Objects.equals(this.registeredNodeIds, registerNodesResponse.registeredNodeIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, registeredNodeIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterNodesResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    registeredNodeIds: ").append(toIndentedString(registeredNodeIds)).append("\n");
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


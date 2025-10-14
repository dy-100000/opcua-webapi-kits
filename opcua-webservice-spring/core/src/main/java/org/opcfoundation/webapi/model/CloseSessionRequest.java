package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2).
 */

@Schema(name = "CloseSessionRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.4/#5.7.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CloseSessionRequest {

  private @Nullable RequestHeader requestHeader;

  private Boolean deleteSubscriptions = false;

  public CloseSessionRequest requestHeader(RequestHeader requestHeader) {
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

  public CloseSessionRequest deleteSubscriptions(Boolean deleteSubscriptions) {
    this.deleteSubscriptions = deleteSubscriptions;
    return this;
  }

  /**
   * Get deleteSubscriptions
   * @return deleteSubscriptions
   */
  
  @Schema(name = "DeleteSubscriptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DeleteSubscriptions")
  public Boolean getDeleteSubscriptions() {
    return deleteSubscriptions;
  }

  public void setDeleteSubscriptions(Boolean deleteSubscriptions) {
    this.deleteSubscriptions = deleteSubscriptions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CloseSessionRequest closeSessionRequest = (CloseSessionRequest) o;
    return Objects.equals(this.requestHeader, closeSessionRequest.requestHeader) &&
        Objects.equals(this.deleteSubscriptions, closeSessionRequest.deleteSubscriptions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, deleteSubscriptions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CloseSessionRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    deleteSubscriptions: ").append(toIndentedString(deleteSubscriptions)).append("\n");
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


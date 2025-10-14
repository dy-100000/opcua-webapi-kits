package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.5/#5.7.5.2).
 */

@Schema(name = "CancelRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.5/#5.7.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CancelRequest {

  private @Nullable RequestHeader requestHeader;

  private Long requestHandle = 0l;

  public CancelRequest requestHeader(RequestHeader requestHeader) {
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

  public CancelRequest requestHandle(Long requestHandle) {
    this.requestHandle = requestHandle;
    return this;
  }

  /**
   * Get requestHandle
   * minimum: 0
   * maximum: 4294967295
   * @return requestHandle
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RequestHandle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHandle")
  public Long getRequestHandle() {
    return requestHandle;
  }

  public void setRequestHandle(Long requestHandle) {
    this.requestHandle = requestHandle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancelRequest cancelRequest = (CancelRequest) o;
    return Objects.equals(this.requestHeader, cancelRequest.requestHeader) &&
        Objects.equals(this.requestHandle, cancelRequest.requestHandle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, requestHandle);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancelRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    requestHandle: ").append(toIndentedString(requestHandle)).append("\n");
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


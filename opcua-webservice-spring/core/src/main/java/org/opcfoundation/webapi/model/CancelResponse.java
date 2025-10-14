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

@Schema(name = "CancelResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.7.5/#5.7.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CancelResponse {

  private @Nullable ResponseHeader responseHeader;

  private Long cancelCount = 0l;

  public CancelResponse responseHeader(ResponseHeader responseHeader) {
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

  public CancelResponse cancelCount(Long cancelCount) {
    this.cancelCount = cancelCount;
    return this;
  }

  /**
   * Get cancelCount
   * minimum: 0
   * maximum: 4294967295
   * @return cancelCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "CancelCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("CancelCount")
  public Long getCancelCount() {
    return cancelCount;
  }

  public void setCancelCount(Long cancelCount) {
    this.cancelCount = cancelCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancelResponse cancelResponse = (CancelResponse) o;
    return Objects.equals(this.responseHeader, cancelResponse.responseHeader) &&
        Objects.equals(this.cancelCount, cancelResponse.cancelCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, cancelCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancelResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    cancelCount: ").append(toIndentedString(cancelCount)).append("\n");
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


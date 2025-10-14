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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2).
 */

@Schema(name = "WriteRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.4/#5.11.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class WriteRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<@Valid WriteValue> nodesToWrite = new ArrayList<>();

  public WriteRequest requestHeader(RequestHeader requestHeader) {
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

  public WriteRequest nodesToWrite(List<@Valid WriteValue> nodesToWrite) {
    this.nodesToWrite = nodesToWrite;
    return this;
  }

  public WriteRequest addNodesToWriteItem(WriteValue nodesToWriteItem) {
    if (this.nodesToWrite == null) {
      this.nodesToWrite = new ArrayList<>();
    }
    this.nodesToWrite.add(nodesToWriteItem);
    return this;
  }

  /**
   * Get nodesToWrite
   * @return nodesToWrite
   */
  @Valid 
  @Schema(name = "NodesToWrite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodesToWrite")
  public List<@Valid WriteValue> getNodesToWrite() {
    return nodesToWrite;
  }

  public void setNodesToWrite(List<@Valid WriteValue> nodesToWrite) {
    this.nodesToWrite = nodesToWrite;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WriteRequest writeRequest = (WriteRequest) o;
    return Objects.equals(this.requestHeader, writeRequest.requestHeader) &&
        Objects.equals(this.nodesToWrite, writeRequest.nodesToWrite);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, nodesToWrite);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WriteRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    nodesToWrite: ").append(toIndentedString(nodesToWrite)).append("\n");
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


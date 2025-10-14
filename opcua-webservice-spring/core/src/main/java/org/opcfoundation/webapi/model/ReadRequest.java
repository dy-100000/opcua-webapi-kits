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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.2/#5.11.2.2).
 */

@Schema(name = "ReadRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.2/#5.11.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReadRequest {

  private @Nullable RequestHeader requestHeader;

  private Double maxAge = 0d;

  private @Nullable Integer timestampsToReturn;

  @Valid
  private List<@Valid ReadValueId> nodesToRead = new ArrayList<>();

  public ReadRequest requestHeader(RequestHeader requestHeader) {
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

  public ReadRequest maxAge(Double maxAge) {
    this.maxAge = maxAge;
    return this;
  }

  /**
   * Get maxAge
   * @return maxAge
   */
  
  @Schema(name = "MaxAge", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxAge")
  public Double getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(Double maxAge) {
    this.maxAge = maxAge;
  }

  public ReadRequest timestampsToReturn(Integer timestampsToReturn) {
    this.timestampsToReturn = timestampsToReturn;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.40).
   * @return timestampsToReturn
   */
  
  @Schema(name = "TimestampsToReturn", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.40).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TimestampsToReturn")
  public Integer getTimestampsToReturn() {
    return timestampsToReturn;
  }

  public void setTimestampsToReturn(Integer timestampsToReturn) {
    this.timestampsToReturn = timestampsToReturn;
  }

  public ReadRequest nodesToRead(List<@Valid ReadValueId> nodesToRead) {
    this.nodesToRead = nodesToRead;
    return this;
  }

  public ReadRequest addNodesToReadItem(ReadValueId nodesToReadItem) {
    if (this.nodesToRead == null) {
      this.nodesToRead = new ArrayList<>();
    }
    this.nodesToRead.add(nodesToReadItem);
    return this;
  }

  /**
   * Get nodesToRead
   * @return nodesToRead
   */
  @Valid 
  @Schema(name = "NodesToRead", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodesToRead")
  public List<@Valid ReadValueId> getNodesToRead() {
    return nodesToRead;
  }

  public void setNodesToRead(List<@Valid ReadValueId> nodesToRead) {
    this.nodesToRead = nodesToRead;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReadRequest readRequest = (ReadRequest) o;
    return Objects.equals(this.requestHeader, readRequest.requestHeader) &&
        Objects.equals(this.maxAge, readRequest.maxAge) &&
        Objects.equals(this.timestampsToReturn, readRequest.timestampsToReturn) &&
        Objects.equals(this.nodesToRead, readRequest.nodesToRead);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, maxAge, timestampsToReturn, nodesToRead);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    maxAge: ").append(toIndentedString(maxAge)).append("\n");
    sb.append("    timestampsToReturn: ").append(toIndentedString(timestampsToReturn)).append("\n");
    sb.append("    nodesToRead: ").append(toIndentedString(nodesToRead)).append("\n");
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


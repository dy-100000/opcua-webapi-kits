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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).
 */

@Schema(name = "TranslateBrowsePathsToNodeIdsRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class TranslateBrowsePathsToNodeIdsRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<@Valid BrowsePath> browsePaths = new ArrayList<>();

  public TranslateBrowsePathsToNodeIdsRequest requestHeader(RequestHeader requestHeader) {
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

  public TranslateBrowsePathsToNodeIdsRequest browsePaths(List<@Valid BrowsePath> browsePaths) {
    this.browsePaths = browsePaths;
    return this;
  }

  public TranslateBrowsePathsToNodeIdsRequest addBrowsePathsItem(BrowsePath browsePathsItem) {
    if (this.browsePaths == null) {
      this.browsePaths = new ArrayList<>();
    }
    this.browsePaths.add(browsePathsItem);
    return this;
  }

  /**
   * Get browsePaths
   * @return browsePaths
   */
  @Valid 
  @Schema(name = "BrowsePaths", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("BrowsePaths")
  public List<@Valid BrowsePath> getBrowsePaths() {
    return browsePaths;
  }

  public void setBrowsePaths(List<@Valid BrowsePath> browsePaths) {
    this.browsePaths = browsePaths;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranslateBrowsePathsToNodeIdsRequest translateBrowsePathsToNodeIdsRequest = (TranslateBrowsePathsToNodeIdsRequest) o;
    return Objects.equals(this.requestHeader, translateBrowsePathsToNodeIdsRequest.requestHeader) &&
        Objects.equals(this.browsePaths, translateBrowsePathsToNodeIdsRequest.browsePaths);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, browsePaths);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranslateBrowsePathsToNodeIdsRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    browsePaths: ").append(toIndentedString(browsePaths)).append("\n");
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


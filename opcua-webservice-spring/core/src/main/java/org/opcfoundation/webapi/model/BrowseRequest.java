package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2).
 */

@Schema(name = "BrowseRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowseRequest {

  private @Nullable RequestHeader requestHeader;

  private @Nullable ViewDescription view;

  private Long requestedMaxReferencesPerNode = 0l;

  @Valid
  private List<@Valid BrowseDescription> nodesToBrowse = new ArrayList<>();

  public BrowseRequest requestHeader(RequestHeader requestHeader) {
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

  public BrowseRequest view(ViewDescription view) {
    this.view = view;
    return this;
  }

  /**
   * Get view
   * @return view
   */
  @Valid 
  @Schema(name = "View", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("View")
  public ViewDescription getView() {
    return view;
  }

  public void setView(ViewDescription view) {
    this.view = view;
  }

  public BrowseRequest requestedMaxReferencesPerNode(Long requestedMaxReferencesPerNode) {
    this.requestedMaxReferencesPerNode = requestedMaxReferencesPerNode;
    return this;
  }

  /**
   * Get requestedMaxReferencesPerNode
   * minimum: 0
   * maximum: 4294967295
   * @return requestedMaxReferencesPerNode
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "RequestedMaxReferencesPerNode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedMaxReferencesPerNode")
  public Long getRequestedMaxReferencesPerNode() {
    return requestedMaxReferencesPerNode;
  }

  public void setRequestedMaxReferencesPerNode(Long requestedMaxReferencesPerNode) {
    this.requestedMaxReferencesPerNode = requestedMaxReferencesPerNode;
  }

  public BrowseRequest nodesToBrowse(List<@Valid BrowseDescription> nodesToBrowse) {
    this.nodesToBrowse = nodesToBrowse;
    return this;
  }

  public BrowseRequest addNodesToBrowseItem(BrowseDescription nodesToBrowseItem) {
    if (this.nodesToBrowse == null) {
      this.nodesToBrowse = new ArrayList<>();
    }
    this.nodesToBrowse.add(nodesToBrowseItem);
    return this;
  }

  /**
   * Get nodesToBrowse
   * @return nodesToBrowse
   */
  @Valid 
  @Schema(name = "NodesToBrowse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodesToBrowse")
  public List<@Valid BrowseDescription> getNodesToBrowse() {
    return nodesToBrowse;
  }

  public void setNodesToBrowse(List<@Valid BrowseDescription> nodesToBrowse) {
    this.nodesToBrowse = nodesToBrowse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowseRequest browseRequest = (BrowseRequest) o;
    return Objects.equals(this.requestHeader, browseRequest.requestHeader) &&
        Objects.equals(this.view, browseRequest.view) &&
        Objects.equals(this.requestedMaxReferencesPerNode, browseRequest.requestedMaxReferencesPerNode) &&
        Objects.equals(this.nodesToBrowse, browseRequest.nodesToBrowse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, view, requestedMaxReferencesPerNode, nodesToBrowse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowseRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    view: ").append(toIndentedString(view)).append("\n");
    sb.append("    requestedMaxReferencesPerNode: ").append(toIndentedString(requestedMaxReferencesPerNode)).append("\n");
    sb.append("    nodesToBrowse: ").append(toIndentedString(nodesToBrowse)).append("\n");
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


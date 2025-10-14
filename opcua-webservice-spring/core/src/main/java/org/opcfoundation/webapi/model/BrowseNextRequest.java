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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.3/#5.9.3.2).
 */

@Schema(name = "BrowseNextRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.3/#5.9.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrowseNextRequest {

  private @Nullable RequestHeader requestHeader;

  private Boolean releaseContinuationPoints = false;

  @Valid
  private List<byte[]> continuationPoints = new ArrayList<>();

  public BrowseNextRequest requestHeader(RequestHeader requestHeader) {
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

  public BrowseNextRequest releaseContinuationPoints(Boolean releaseContinuationPoints) {
    this.releaseContinuationPoints = releaseContinuationPoints;
    return this;
  }

  /**
   * Get releaseContinuationPoints
   * @return releaseContinuationPoints
   */
  
  @Schema(name = "ReleaseContinuationPoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReleaseContinuationPoints")
  public Boolean getReleaseContinuationPoints() {
    return releaseContinuationPoints;
  }

  public void setReleaseContinuationPoints(Boolean releaseContinuationPoints) {
    this.releaseContinuationPoints = releaseContinuationPoints;
  }

  public BrowseNextRequest continuationPoints(List<byte[]> continuationPoints) {
    this.continuationPoints = continuationPoints;
    return this;
  }

  public BrowseNextRequest addContinuationPointsItem(byte[] continuationPointsItem) {
    if (this.continuationPoints == null) {
      this.continuationPoints = new ArrayList<>();
    }
    this.continuationPoints.add(continuationPointsItem);
    return this;
  }

  /**
   * Get continuationPoints
   * @return continuationPoints
   */
  
  @Schema(name = "ContinuationPoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ContinuationPoints")
  public List<byte[]> getContinuationPoints() {
    return continuationPoints;
  }

  public void setContinuationPoints(List<byte[]> continuationPoints) {
    this.continuationPoints = continuationPoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrowseNextRequest browseNextRequest = (BrowseNextRequest) o;
    return Objects.equals(this.requestHeader, browseNextRequest.requestHeader) &&
        Objects.equals(this.releaseContinuationPoints, browseNextRequest.releaseContinuationPoints) &&
        Objects.equals(this.continuationPoints, browseNextRequest.continuationPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, releaseContinuationPoints, continuationPoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrowseNextRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    releaseContinuationPoints: ").append(toIndentedString(releaseContinuationPoints)).append("\n");
    sb.append("    continuationPoints: ").append(toIndentedString(continuationPoints)).append("\n");
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


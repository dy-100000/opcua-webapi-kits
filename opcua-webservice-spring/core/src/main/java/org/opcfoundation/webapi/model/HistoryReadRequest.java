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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2).
 */

@Schema(name = "HistoryReadRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryReadRequest {

  private @Nullable RequestHeader requestHeader;

  private @Nullable ExtensionObject historyReadDetails;

  private @Nullable Integer timestampsToReturn;

  private Boolean releaseContinuationPoints = false;

  @Valid
  private List<@Valid HistoryReadValueId> nodesToRead = new ArrayList<>();

  public HistoryReadRequest requestHeader(RequestHeader requestHeader) {
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

  public HistoryReadRequest historyReadDetails(ExtensionObject historyReadDetails) {
    this.historyReadDetails = historyReadDetails;
    return this;
  }

  /**
   * Get historyReadDetails
   * @return historyReadDetails
   */
  @Valid 
  @Schema(name = "HistoryReadDetails", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("HistoryReadDetails")
  public ExtensionObject getHistoryReadDetails() {
    return historyReadDetails;
  }

  public void setHistoryReadDetails(ExtensionObject historyReadDetails) {
    this.historyReadDetails = historyReadDetails;
  }

  public HistoryReadRequest timestampsToReturn(Integer timestampsToReturn) {
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

  public HistoryReadRequest releaseContinuationPoints(Boolean releaseContinuationPoints) {
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

  public HistoryReadRequest nodesToRead(List<@Valid HistoryReadValueId> nodesToRead) {
    this.nodesToRead = nodesToRead;
    return this;
  }

  public HistoryReadRequest addNodesToReadItem(HistoryReadValueId nodesToReadItem) {
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
  public List<@Valid HistoryReadValueId> getNodesToRead() {
    return nodesToRead;
  }

  public void setNodesToRead(List<@Valid HistoryReadValueId> nodesToRead) {
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
    HistoryReadRequest historyReadRequest = (HistoryReadRequest) o;
    return Objects.equals(this.requestHeader, historyReadRequest.requestHeader) &&
        Objects.equals(this.historyReadDetails, historyReadRequest.historyReadDetails) &&
        Objects.equals(this.timestampsToReturn, historyReadRequest.timestampsToReturn) &&
        Objects.equals(this.releaseContinuationPoints, historyReadRequest.releaseContinuationPoints) &&
        Objects.equals(this.nodesToRead, historyReadRequest.nodesToRead);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, historyReadDetails, timestampsToReturn, releaseContinuationPoints, nodesToRead);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryReadRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    historyReadDetails: ").append(toIndentedString(historyReadDetails)).append("\n");
    sb.append("    timestampsToReturn: ").append(toIndentedString(timestampsToReturn)).append("\n");
    sb.append("    releaseContinuationPoints: ").append(toIndentedString(releaseContinuationPoints)).append("\n");
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


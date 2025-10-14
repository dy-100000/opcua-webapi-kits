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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2).
 */

@Schema(name = "HistoryUpdateRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.5/#5.11.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryUpdateRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<@Valid ExtensionObject> historyUpdateDetails = new ArrayList<>();

  public HistoryUpdateRequest requestHeader(RequestHeader requestHeader) {
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

  public HistoryUpdateRequest historyUpdateDetails(List<@Valid ExtensionObject> historyUpdateDetails) {
    this.historyUpdateDetails = historyUpdateDetails;
    return this;
  }

  public HistoryUpdateRequest addHistoryUpdateDetailsItem(ExtensionObject historyUpdateDetailsItem) {
    if (this.historyUpdateDetails == null) {
      this.historyUpdateDetails = new ArrayList<>();
    }
    this.historyUpdateDetails.add(historyUpdateDetailsItem);
    return this;
  }

  /**
   * Get historyUpdateDetails
   * @return historyUpdateDetails
   */
  @Valid 
  @Schema(name = "HistoryUpdateDetails", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("HistoryUpdateDetails")
  public List<@Valid ExtensionObject> getHistoryUpdateDetails() {
    return historyUpdateDetails;
  }

  public void setHistoryUpdateDetails(List<@Valid ExtensionObject> historyUpdateDetails) {
    this.historyUpdateDetails = historyUpdateDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryUpdateRequest historyUpdateRequest = (HistoryUpdateRequest) o;
    return Objects.equals(this.requestHeader, historyUpdateRequest.requestHeader) &&
        Objects.equals(this.historyUpdateDetails, historyUpdateRequest.historyUpdateDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, historyUpdateDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryUpdateRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    historyUpdateDetails: ").append(toIndentedString(historyUpdateDetails)).append("\n");
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


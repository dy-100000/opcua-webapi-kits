package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2).
 */

@Schema(name = "HistoryReadResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryReadResult {

  private @Nullable StatusCode statusCode;

  private @Nullable byte[] continuationPoint;

  private @Nullable ExtensionObject historyData;

  public HistoryReadResult statusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Get statusCode
   * @return statusCode
   */
  @Valid 
  @Schema(name = "StatusCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StatusCode")
  public StatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public HistoryReadResult continuationPoint(byte[] continuationPoint) {
    this.continuationPoint = continuationPoint;
    return this;
  }

  /**
   * Get continuationPoint
   * @return continuationPoint
   */
  
  @Schema(name = "ContinuationPoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ContinuationPoint")
  public byte[] getContinuationPoint() {
    return continuationPoint;
  }

  public void setContinuationPoint(byte[] continuationPoint) {
    this.continuationPoint = continuationPoint;
  }

  public HistoryReadResult historyData(ExtensionObject historyData) {
    this.historyData = historyData;
    return this;
  }

  /**
   * Get historyData
   * @return historyData
   */
  @Valid 
  @Schema(name = "HistoryData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("HistoryData")
  public ExtensionObject getHistoryData() {
    return historyData;
  }

  public void setHistoryData(ExtensionObject historyData) {
    this.historyData = historyData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryReadResult historyReadResult = (HistoryReadResult) o;
    return Objects.equals(this.statusCode, historyReadResult.statusCode) &&
        Arrays.equals(this.continuationPoint, historyReadResult.continuationPoint) &&
        Objects.equals(this.historyData, historyReadResult.historyData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, Arrays.hashCode(continuationPoint), historyData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryReadResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    continuationPoint: ").append(toIndentedString(continuationPoint)).append("\n");
    sb.append("    historyData: ").append(toIndentedString(historyData)).append("\n");
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


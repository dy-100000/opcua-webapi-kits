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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2).
 */

@Schema(name = "PublishResponse", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PublishResponse {

  private @Nullable ResponseHeader responseHeader;

  private Long subscriptionId = 0l;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> availableSequenceNumbers = new ArrayList<>();

  private Boolean moreNotifications = false;

  private @Nullable NotificationMessage notificationMessage;

  @Valid
  private List<@Valid StatusCode> results = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> diagnosticInfos = new ArrayList<>();

  public PublishResponse responseHeader(ResponseHeader responseHeader) {
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

  public PublishResponse subscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * Get subscriptionId
   * minimum: 0
   * maximum: 4294967295
   * @return subscriptionId
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "SubscriptionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscriptionId")
  public Long getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(Long subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public PublishResponse availableSequenceNumbers(List<@Min(0L) @Max(4294967295L)Long> availableSequenceNumbers) {
    this.availableSequenceNumbers = availableSequenceNumbers;
    return this;
  }

  public PublishResponse addAvailableSequenceNumbersItem(Long availableSequenceNumbersItem) {
    if (this.availableSequenceNumbers == null) {
      this.availableSequenceNumbers = new ArrayList<>();
    }
    this.availableSequenceNumbers.add(availableSequenceNumbersItem);
    return this;
  }

  /**
   * Get availableSequenceNumbers
   * @return availableSequenceNumbers
   */
  
  @Schema(name = "AvailableSequenceNumbers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AvailableSequenceNumbers")
  public List<@Min(0L) @Max(4294967295L)Long> getAvailableSequenceNumbers() {
    return availableSequenceNumbers;
  }

  public void setAvailableSequenceNumbers(List<@Min(0L) @Max(4294967295L)Long> availableSequenceNumbers) {
    this.availableSequenceNumbers = availableSequenceNumbers;
  }

  public PublishResponse moreNotifications(Boolean moreNotifications) {
    this.moreNotifications = moreNotifications;
    return this;
  }

  /**
   * Get moreNotifications
   * @return moreNotifications
   */
  
  @Schema(name = "MoreNotifications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MoreNotifications")
  public Boolean getMoreNotifications() {
    return moreNotifications;
  }

  public void setMoreNotifications(Boolean moreNotifications) {
    this.moreNotifications = moreNotifications;
  }

  public PublishResponse notificationMessage(NotificationMessage notificationMessage) {
    this.notificationMessage = notificationMessage;
    return this;
  }

  /**
   * Get notificationMessage
   * @return notificationMessage
   */
  @Valid 
  @Schema(name = "NotificationMessage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NotificationMessage")
  public NotificationMessage getNotificationMessage() {
    return notificationMessage;
  }

  public void setNotificationMessage(NotificationMessage notificationMessage) {
    this.notificationMessage = notificationMessage;
  }

  public PublishResponse results(List<@Valid StatusCode> results) {
    this.results = results;
    return this;
  }

  public PublishResponse addResultsItem(StatusCode resultsItem) {
    if (this.results == null) {
      this.results = new ArrayList<>();
    }
    this.results.add(resultsItem);
    return this;
  }

  /**
   * Get results
   * @return results
   */
  @Valid 
  @Schema(name = "Results", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Results")
  public List<@Valid StatusCode> getResults() {
    return results;
  }

  public void setResults(List<@Valid StatusCode> results) {
    this.results = results;
  }

  public PublishResponse diagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
    return this;
  }

  public PublishResponse addDiagnosticInfosItem(DiagnosticInfo diagnosticInfosItem) {
    if (this.diagnosticInfos == null) {
      this.diagnosticInfos = new ArrayList<>();
    }
    this.diagnosticInfos.add(diagnosticInfosItem);
    return this;
  }

  /**
   * Get diagnosticInfos
   * @return diagnosticInfos
   */
  @Valid 
  @Schema(name = "DiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiagnosticInfos")
  public List<@Valid DiagnosticInfo> getDiagnosticInfos() {
    return diagnosticInfos;
  }

  public void setDiagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PublishResponse publishResponse = (PublishResponse) o;
    return Objects.equals(this.responseHeader, publishResponse.responseHeader) &&
        Objects.equals(this.subscriptionId, publishResponse.subscriptionId) &&
        Objects.equals(this.availableSequenceNumbers, publishResponse.availableSequenceNumbers) &&
        Objects.equals(this.moreNotifications, publishResponse.moreNotifications) &&
        Objects.equals(this.notificationMessage, publishResponse.notificationMessage) &&
        Objects.equals(this.results, publishResponse.results) &&
        Objects.equals(this.diagnosticInfos, publishResponse.diagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseHeader, subscriptionId, availableSequenceNumbers, moreNotifications, notificationMessage, results, diagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PublishResponse {\n");
    sb.append("    responseHeader: ").append(toIndentedString(responseHeader)).append("\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    availableSequenceNumbers: ").append(toIndentedString(availableSequenceNumbers)).append("\n");
    sb.append("    moreNotifications: ").append(toIndentedString(moreNotifications)).append("\n");
    sb.append("    notificationMessage: ").append(toIndentedString(notificationMessage)).append("\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
    sb.append("    diagnosticInfos: ").append(toIndentedString(diagnosticInfos)).append("\n");
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


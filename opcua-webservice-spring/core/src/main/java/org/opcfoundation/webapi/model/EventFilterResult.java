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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3).
 */

@Schema(name = "EventFilterResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EventFilterResult {

  @Valid
  private List<@Valid StatusCode> selectClauseResults = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> selectClauseDiagnosticInfos = new ArrayList<>();

  private @Nullable ContentFilterResult whereClauseResult;

  public EventFilterResult selectClauseResults(List<@Valid StatusCode> selectClauseResults) {
    this.selectClauseResults = selectClauseResults;
    return this;
  }

  public EventFilterResult addSelectClauseResultsItem(StatusCode selectClauseResultsItem) {
    if (this.selectClauseResults == null) {
      this.selectClauseResults = new ArrayList<>();
    }
    this.selectClauseResults.add(selectClauseResultsItem);
    return this;
  }

  /**
   * Get selectClauseResults
   * @return selectClauseResults
   */
  @Valid 
  @Schema(name = "SelectClauseResults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SelectClauseResults")
  public List<@Valid StatusCode> getSelectClauseResults() {
    return selectClauseResults;
  }

  public void setSelectClauseResults(List<@Valid StatusCode> selectClauseResults) {
    this.selectClauseResults = selectClauseResults;
  }

  public EventFilterResult selectClauseDiagnosticInfos(List<@Valid DiagnosticInfo> selectClauseDiagnosticInfos) {
    this.selectClauseDiagnosticInfos = selectClauseDiagnosticInfos;
    return this;
  }

  public EventFilterResult addSelectClauseDiagnosticInfosItem(DiagnosticInfo selectClauseDiagnosticInfosItem) {
    if (this.selectClauseDiagnosticInfos == null) {
      this.selectClauseDiagnosticInfos = new ArrayList<>();
    }
    this.selectClauseDiagnosticInfos.add(selectClauseDiagnosticInfosItem);
    return this;
  }

  /**
   * Get selectClauseDiagnosticInfos
   * @return selectClauseDiagnosticInfos
   */
  @Valid 
  @Schema(name = "SelectClauseDiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SelectClauseDiagnosticInfos")
  public List<@Valid DiagnosticInfo> getSelectClauseDiagnosticInfos() {
    return selectClauseDiagnosticInfos;
  }

  public void setSelectClauseDiagnosticInfos(List<@Valid DiagnosticInfo> selectClauseDiagnosticInfos) {
    this.selectClauseDiagnosticInfos = selectClauseDiagnosticInfos;
  }

  public EventFilterResult whereClauseResult(ContentFilterResult whereClauseResult) {
    this.whereClauseResult = whereClauseResult;
    return this;
  }

  /**
   * Get whereClauseResult
   * @return whereClauseResult
   */
  @Valid 
  @Schema(name = "WhereClauseResult", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("WhereClauseResult")
  public ContentFilterResult getWhereClauseResult() {
    return whereClauseResult;
  }

  public void setWhereClauseResult(ContentFilterResult whereClauseResult) {
    this.whereClauseResult = whereClauseResult;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventFilterResult eventFilterResult = (EventFilterResult) o;
    return Objects.equals(this.selectClauseResults, eventFilterResult.selectClauseResults) &&
        Objects.equals(this.selectClauseDiagnosticInfos, eventFilterResult.selectClauseDiagnosticInfos) &&
        Objects.equals(this.whereClauseResult, eventFilterResult.whereClauseResult);
  }

  @Override
  public int hashCode() {
    return Objects.hash(selectClauseResults, selectClauseDiagnosticInfos, whereClauseResult);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventFilterResult {\n");
    sb.append("    selectClauseResults: ").append(toIndentedString(selectClauseResults)).append("\n");
    sb.append("    selectClauseDiagnosticInfos: ").append(toIndentedString(selectClauseDiagnosticInfos)).append("\n");
    sb.append("    whereClauseResult: ").append(toIndentedString(whereClauseResult)).append("\n");
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


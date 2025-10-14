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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.4/#6.9.4.1).
 */

@Schema(name = "UpdateEventDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.4/#6.9.4.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UpdateEventDetails {

  private @Nullable String nodeId;

  private @Nullable Integer performInsertReplace;

  private @Nullable EventFilter filter;

  @Valid
  private List<@Valid HistoryEventFieldList> eventData = new ArrayList<>();

  public UpdateEventDetails nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  
  @Schema(name = "NodeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodeId")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public UpdateEventDetails performInsertReplace(Integer performInsertReplace) {
    this.performInsertReplace = performInsertReplace;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.8).
   * @return performInsertReplace
   */
  
  @Schema(name = "PerformInsertReplace", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.8).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PerformInsertReplace")
  public Integer getPerformInsertReplace() {
    return performInsertReplace;
  }

  public void setPerformInsertReplace(Integer performInsertReplace) {
    this.performInsertReplace = performInsertReplace;
  }

  public UpdateEventDetails filter(EventFilter filter) {
    this.filter = filter;
    return this;
  }

  /**
   * Get filter
   * @return filter
   */
  @Valid 
  @Schema(name = "Filter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Filter")
  public EventFilter getFilter() {
    return filter;
  }

  public void setFilter(EventFilter filter) {
    this.filter = filter;
  }

  public UpdateEventDetails eventData(List<@Valid HistoryEventFieldList> eventData) {
    this.eventData = eventData;
    return this;
  }

  public UpdateEventDetails addEventDataItem(HistoryEventFieldList eventDataItem) {
    if (this.eventData == null) {
      this.eventData = new ArrayList<>();
    }
    this.eventData.add(eventDataItem);
    return this;
  }

  /**
   * Get eventData
   * @return eventData
   */
  @Valid 
  @Schema(name = "EventData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("EventData")
  public List<@Valid HistoryEventFieldList> getEventData() {
    return eventData;
  }

  public void setEventData(List<@Valid HistoryEventFieldList> eventData) {
    this.eventData = eventData;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateEventDetails updateEventDetails = (UpdateEventDetails) o;
    return Objects.equals(this.nodeId, updateEventDetails.nodeId) &&
        Objects.equals(this.performInsertReplace, updateEventDetails.performInsertReplace) &&
        Objects.equals(this.filter, updateEventDetails.filter) &&
        Objects.equals(this.eventData, updateEventDetails.eventData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, performInsertReplace, filter, eventData);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateEventDetails {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    performInsertReplace: ").append(toIndentedString(performInsertReplace)).append("\n");
    sb.append("    filter: ").append(toIndentedString(filter)).append("\n");
    sb.append("    eventData: ").append(toIndentedString(eventData)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.4).
 */

@Schema(name = "HistoryEventFieldList", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryEventFieldList {

  @Valid
  private List<@Valid Variant> eventFields = new ArrayList<>();

  public HistoryEventFieldList eventFields(List<@Valid Variant> eventFields) {
    this.eventFields = eventFields;
    return this;
  }

  public HistoryEventFieldList addEventFieldsItem(Variant eventFieldsItem) {
    if (this.eventFields == null) {
      this.eventFields = new ArrayList<>();
    }
    this.eventFields.add(eventFieldsItem);
    return this;
  }

  /**
   * Get eventFields
   * @return eventFields
   */
  @Valid 
  @Schema(name = "EventFields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("EventFields")
  public List<@Valid Variant> getEventFields() {
    return eventFields;
  }

  public void setEventFields(List<@Valid Variant> eventFields) {
    this.eventFields = eventFields;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryEventFieldList historyEventFieldList = (HistoryEventFieldList) o;
    return Objects.equals(this.eventFields, historyEventFieldList.eventFields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventFields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryEventFieldList {\n");
    sb.append("    eventFields: ").append(toIndentedString(eventFields)).append("\n");
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


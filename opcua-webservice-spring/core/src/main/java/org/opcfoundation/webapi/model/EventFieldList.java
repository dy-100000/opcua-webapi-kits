package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3).
 */

@Schema(name = "EventFieldList", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EventFieldList {

  private Long clientHandle = 0l;

  @Valid
  private List<@Valid Variant> eventFields = new ArrayList<>();

  public EventFieldList clientHandle(Long clientHandle) {
    this.clientHandle = clientHandle;
    return this;
  }

  /**
   * Get clientHandle
   * minimum: 0
   * maximum: 4294967295
   * @return clientHandle
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ClientHandle", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ClientHandle")
  public Long getClientHandle() {
    return clientHandle;
  }

  public void setClientHandle(Long clientHandle) {
    this.clientHandle = clientHandle;
  }

  public EventFieldList eventFields(List<@Valid Variant> eventFields) {
    this.eventFields = eventFields;
    return this;
  }

  public EventFieldList addEventFieldsItem(Variant eventFieldsItem) {
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
    EventFieldList eventFieldList = (EventFieldList) o;
    return Objects.equals(this.clientHandle, eventFieldList.clientHandle) &&
        Objects.equals(this.eventFields, eventFieldList.eventFields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientHandle, eventFields);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventFieldList {\n");
    sb.append("    clientHandle: ").append(toIndentedString(clientHandle)).append("\n");
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


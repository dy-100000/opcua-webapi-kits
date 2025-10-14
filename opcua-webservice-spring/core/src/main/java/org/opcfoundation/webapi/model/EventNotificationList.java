package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3).
 */

@Schema(name = "EventNotificationList", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EventNotificationList {

  @Valid
  private List<@Valid EventFieldList> events = new ArrayList<>();

  public EventNotificationList events(List<@Valid EventFieldList> events) {
    this.events = events;
    return this;
  }

  public EventNotificationList addEventsItem(EventFieldList eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @Valid 
  @Schema(name = "Events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Events")
  public List<@Valid EventFieldList> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid EventFieldList> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventNotificationList eventNotificationList = (EventNotificationList) o;
    return Objects.equals(this.events, eventNotificationList.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventNotificationList {\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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


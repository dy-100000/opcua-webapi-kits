package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets JsonMessageType
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum JsonMessageType {
  
  Data("ua-data"),
  
  DataSetMetadata("ua-metadata"),
  
  Application("ua-application"),
  
  Endpoints("ua-endpoints"),
  
  Status("ua-status"),
  
  Connection("ua-connection"),
  
  ActionRequest("ua-action-request"),
  
  ActionResponse("ua-action-response"),
  
  ActionMetadata("ua-action-metadata"),
  
  ActionResponder("ua-action-responder"),
  
  KeyFrame("ua-keyframe"),
  
  DeltaFrame("ua-deltaframe"),
  
  Event("ua-event"),
  
  KeepAlive("ua-keepalive");

  private String value;

  JsonMessageType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static JsonMessageType fromValue(String value) {
    for (JsonMessageType b : JsonMessageType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


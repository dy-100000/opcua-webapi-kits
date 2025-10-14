package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.40).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum TimestampsToReturn {
  
  Source(0),
  
  Server(1),
  
  Both(2),
  
  Neither(3),
  
  Invalid(4);

  private Integer value;

  TimestampsToReturn(Integer value) {
    this.value = value;
  }

  @JsonValue
  public Integer getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static TimestampsToReturn fromValue(Integer value) {
    for (TimestampsToReturn b : TimestampsToReturn.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


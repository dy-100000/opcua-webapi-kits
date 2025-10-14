package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.5/#12.2.5.2).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum NodeClass {
  
  Unspecified(0),
  
  Object(1),
  
  Variable(2),
  
  Method(4),
  
  ObjectType(8),
  
  VariableType(16),
  
  ReferenceType(32),
  
  DataType(64),
  
  View(128);

  private Integer value;

  NodeClass(Integer value) {
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
  public static NodeClass fromValue(Integer value) {
    for (NodeClass b : NodeClass.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part3/8.55).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum PermissionTypeBits {
  
  Browse(1),
  
  ReadRolePermissions(2),
  
  WriteAttribute(4),
  
  WriteRolePermissions(8),
  
  WriteHistorizing(16),
  
  Read(32),
  
  Write(64),
  
  ReadHistory(128),
  
  InsertHistory(256),
  
  ModifyHistory(512),
  
  DeleteHistory(1024),
  
  ReceiveEvents(2048),
  
  Call(4096),
  
  AddReference(8192),
  
  RemoveReference(16384),
  
  DeleteNode(32768),
  
  AddNode(65536);

  private Integer value;

  PermissionTypeBits(Integer value) {
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
  public static PermissionTypeBits fromValue(Integer value) {
    for (PermissionTypeBits b : PermissionTypeBits.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


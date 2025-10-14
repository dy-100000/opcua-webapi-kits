package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.1).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum JsonNetworkMessageContentMaskBits {
  
  NetworkMessageHeader(1),
  
  DataSetMessageHeader(2),
  
  SingleDataSetMessage(4),
  
  PublisherId(8),
  
  DataSetClassId(16),
  
  ReplyTo(32),
  
  WriterGroupName(64);

  private Integer value;

  JsonNetworkMessageContentMaskBits(Integer value) {
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
  public static JsonNetworkMessageContentMaskBits fromValue(Integer value) {
    for (JsonNetworkMessageContentMaskBits b : JsonNetworkMessageContentMaskBits.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


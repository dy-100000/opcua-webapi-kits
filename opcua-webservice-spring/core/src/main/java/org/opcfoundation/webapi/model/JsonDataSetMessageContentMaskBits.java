package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.1).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum JsonDataSetMessageContentMaskBits {
  
  DataSetWriterId(1),
  
  MetaDataVersion(2),
  
  SequenceNumber(4),
  
  Timestamp(8),
  
  Status(16),
  
  MessageType(32),
  
  DataSetWriterName(64),
  
  FieldEncoding1(128),
  
  PublisherId(256),
  
  WriterGroupName(512),
  
  MinorVersion(1024),
  
  FieldEncoding2(2048);

  private Integer value;

  JsonDataSetMessageContentMaskBits(Integer value) {
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
  public static JsonDataSetMessageContentMaskBits fromValue(Integer value) {
    for (JsonDataSetMessageContentMaskBits b : JsonDataSetMessageContentMaskBits.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


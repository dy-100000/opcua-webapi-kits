package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.4/#6.2.4.2).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum DataSetFieldContentMaskBits {
  
  StatusCode(1),
  
  SourceTimestamp(2),
  
  ServerTimestamp(4),
  
  SourcePicoSeconds(8),
  
  ServerPicoSeconds(16),
  
  RawData(32);

  private Integer value;

  DataSetFieldContentMaskBits(Integer value) {
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
  public static DataSetFieldContentMaskBits fromValue(Integer value) {
    for (DataSetFieldContentMaskBits b : DataSetFieldContentMaskBits.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


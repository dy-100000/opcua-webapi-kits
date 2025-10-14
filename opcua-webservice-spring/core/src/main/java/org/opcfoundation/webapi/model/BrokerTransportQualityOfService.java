package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum BrokerTransportQualityOfService {
  
  NotSpecified(0),
  
  BestEffort(1),
  
  AtLeastOnce(2),
  
  AtMostOnce(3),
  
  ExactlyOnce(4);

  private Integer value;

  BrokerTransportQualityOfService(Integer value) {
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
  public static BrokerTransportQualityOfService fromValue(Integer value) {
    for (BrokerTransportQualityOfService b : BrokerTransportQualityOfService.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


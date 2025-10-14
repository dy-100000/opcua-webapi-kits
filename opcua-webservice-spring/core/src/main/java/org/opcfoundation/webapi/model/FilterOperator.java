package org.opcfoundation.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;


import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.3).
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public enum FilterOperator {
  
  Equals(0),
  
  IsNull(1),
  
  GreaterThan(2),
  
  LessThan(3),
  
  GreaterThanOrEqual(4),
  
  LessThanOrEqual(5),
  
  Like(6),
  
  Not(7),
  
  Between(8),
  
  InList(9),
  
  And(10),
  
  Or(11),
  
  Cast(12),
  
  InView(13),
  
  OfType(14),
  
  RelatedTo(15),
  
  BitwiseAnd(16),
  
  BitwiseOr(17);

  private Integer value;

  FilterOperator(Integer value) {
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
  public static FilterOperator fromValue(Integer value) {
    for (FilterOperator b : FilterOperator.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}


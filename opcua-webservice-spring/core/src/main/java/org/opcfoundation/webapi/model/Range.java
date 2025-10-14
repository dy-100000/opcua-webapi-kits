package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.2).
 */

@Schema(name = "Range", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Range {

  private Double low = 0d;

  private Double high = 0d;

  public Range low(Double low) {
    this.low = low;
    return this;
  }

  /**
   * Get low
   * @return low
   */
  
  @Schema(name = "Low", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Low")
  public Double getLow() {
    return low;
  }

  public void setLow(Double low) {
    this.low = low;
  }

  public Range high(Double high) {
    this.high = high;
    return this;
  }

  /**
   * Get high
   * @return high
   */
  
  @Schema(name = "High", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("High")
  public Double getHigh() {
    return high;
  }

  public void setHigh(Double high) {
    this.high = high;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Range range = (Range) o;
    return Objects.equals(this.low, range.low) &&
        Objects.equals(this.high, range.high);
  }

  @Override
  public int hashCode() {
    return Objects.hash(low, high);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Range {\n");
    sb.append("    low: ").append(toIndentedString(low)).append("\n");
    sb.append("    high: ").append(toIndentedString(high)).append("\n");
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


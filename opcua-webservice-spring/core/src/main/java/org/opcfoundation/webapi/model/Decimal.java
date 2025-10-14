package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.3).
 */

@Schema(name = "Decimal", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Decimal {

  private Integer scale = 0;

  private String value = "0";

  public Decimal scale(Integer scale) {
    this.scale = scale;
    return this;
  }

  /**
   * Get scale
   * minimum: -32768
   * maximum: 32767
   * @return scale
   */
  @Min(-32768) @Max(32767) 
  @Schema(name = "Scale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Scale")
  public Integer getScale() {
    return scale;
  }

  public void setScale(Integer scale) {
    this.scale = scale;
  }

  public Decimal value(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "Value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Value")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Decimal decimal = (Decimal) o;
    return Objects.equals(this.scale, decimal.scale) &&
        Objects.equals(this.value, decimal.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scale, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Decimal {\n");
    sb.append("    scale: ").append(toIndentedString(scale)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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


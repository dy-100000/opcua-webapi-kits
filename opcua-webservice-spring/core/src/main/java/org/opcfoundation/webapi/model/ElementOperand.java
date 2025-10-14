package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.2).
 */

@Schema(name = "ElementOperand", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.4/#7.7.4.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ElementOperand {

  private Long index = 0l;

  public ElementOperand index(Long index) {
    this.index = index;
    return this;
  }

  /**
   * Get index
   * minimum: 0
   * maximum: 4294967295
   * @return index
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "Index", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Index")
  public Long getIndex() {
    return index;
  }

  public void setIndex(Long index) {
    this.index = index;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElementOperand elementOperand = (ElementOperand) o;
    return Objects.equals(this.index, elementOperand.index);
  }

  @Override
  public int hashCode() {
    return Objects.hash(index);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElementOperand {\n");
    sb.append("    index: ").append(toIndentedString(index)).append("\n");
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


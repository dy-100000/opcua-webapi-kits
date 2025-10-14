package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.5).
 */

@Schema(name = "Matrix", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Matrix {

  @Valid
  private List<Object> array = new ArrayList<>();

  @Valid
  private List<@Min(0)Integer> dimensions = new ArrayList<>();

  public Matrix array(List<Object> array) {
    this.array = array;
    return this;
  }

  public Matrix addArrayItem(Object arrayItem) {
    if (this.array == null) {
      this.array = new ArrayList<>();
    }
    this.array.add(arrayItem);
    return this;
  }

  /**
   * Get array
   * @return array
   */
  
  @Schema(name = "Array", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Array")
  public List<Object> getArray() {
    return array;
  }

  public void setArray(List<Object> array) {
    this.array = array;
  }

  public Matrix dimensions(List<@Min(0)Integer> dimensions) {
    this.dimensions = dimensions;
    return this;
  }

  public Matrix addDimensionsItem(Integer dimensionsItem) {
    if (this.dimensions == null) {
      this.dimensions = new ArrayList<>();
    }
    this.dimensions.add(dimensionsItem);
    return this;
  }

  /**
   * Get dimensions
   * @return dimensions
   */
  
  @Schema(name = "Dimensions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Dimensions")
  public List<@Min(0)Integer> getDimensions() {
    return dimensions;
  }

  public void setDimensions(List<@Min(0)Integer> dimensions) {
    this.dimensions = dimensions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Matrix matrix = (Matrix) o;
    return Objects.equals(this.array, matrix.array) &&
        Objects.equals(this.dimensions, matrix.dimensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(array, dimensions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Matrix {\n");
    sb.append("    array: ").append(toIndentedString(array)).append("\n");
    sb.append("    dimensions: ").append(toIndentedString(dimensions)).append("\n");
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


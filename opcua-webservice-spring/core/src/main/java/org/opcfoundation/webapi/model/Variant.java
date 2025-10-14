package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.2.17).
 */

@Schema(name = "Variant", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.2.17).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class Variant {

  private @Nullable Integer uaType;

  private @Nullable Object value = null;

  @Valid
  private List<@Min(0)Integer> dimensions = new ArrayList<>();

  public Variant uaType(Integer uaType) {
    this.uaType = uaType;
    return this;
  }

  /**
   * Get uaType
   * minimum: 0
   * maximum: 255
   * @return uaType
   */
  @Min(0) @Max(255) 
  @Schema(name = "UaType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UaType")
  public Integer getUaType() {
    return uaType;
  }

  public void setUaType(Integer uaType) {
    this.uaType = uaType;
  }

  public Variant value(Object value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "Value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Value")
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Variant dimensions(List<@Min(0)Integer> dimensions) {
    this.dimensions = dimensions;
    return this;
  }

  public Variant addDimensionsItem(Integer dimensionsItem) {
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
    Variant variant = (Variant) o;
    return Objects.equals(this.uaType, variant.uaType) &&
        Objects.equals(this.value, variant.value) &&
        Objects.equals(this.dimensions, variant.dimensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaType, value, dimensions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Variant {\n");
    sb.append("    uaType: ").append(toIndentedString(uaType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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


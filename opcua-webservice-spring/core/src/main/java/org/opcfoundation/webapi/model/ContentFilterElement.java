package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.1).
 */

@Schema(name = "ContentFilterElement", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ContentFilterElement {

  private @Nullable Integer filterOperator;

  @Valid
  private List<@Valid ExtensionObject> filterOperands = new ArrayList<>();

  public ContentFilterElement filterOperator(Integer filterOperator) {
    this.filterOperator = filterOperator;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.3).
   * @return filterOperator
   */
  
  @Schema(name = "FilterOperator", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.3).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("FilterOperator")
  public Integer getFilterOperator() {
    return filterOperator;
  }

  public void setFilterOperator(Integer filterOperator) {
    this.filterOperator = filterOperator;
  }

  public ContentFilterElement filterOperands(List<@Valid ExtensionObject> filterOperands) {
    this.filterOperands = filterOperands;
    return this;
  }

  public ContentFilterElement addFilterOperandsItem(ExtensionObject filterOperandsItem) {
    if (this.filterOperands == null) {
      this.filterOperands = new ArrayList<>();
    }
    this.filterOperands.add(filterOperandsItem);
    return this;
  }

  /**
   * Get filterOperands
   * @return filterOperands
   */
  @Valid 
  @Schema(name = "FilterOperands", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("FilterOperands")
  public List<@Valid ExtensionObject> getFilterOperands() {
    return filterOperands;
  }

  public void setFilterOperands(List<@Valid ExtensionObject> filterOperands) {
    this.filterOperands = filterOperands;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentFilterElement contentFilterElement = (ContentFilterElement) o;
    return Objects.equals(this.filterOperator, contentFilterElement.filterOperator) &&
        Objects.equals(this.filterOperands, contentFilterElement.filterOperands);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filterOperator, filterOperands);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentFilterElement {\n");
    sb.append("    filterOperator: ").append(toIndentedString(filterOperator)).append("\n");
    sb.append("    filterOperands: ").append(toIndentedString(filterOperands)).append("\n");
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


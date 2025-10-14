package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31).
 */

@Schema(name = "RelativePath", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.31).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class RelativePath {

  @Valid
  private List<@Valid RelativePathElement> elements = new ArrayList<>();

  public RelativePath elements(List<@Valid RelativePathElement> elements) {
    this.elements = elements;
    return this;
  }

  public RelativePath addElementsItem(RelativePathElement elementsItem) {
    if (this.elements == null) {
      this.elements = new ArrayList<>();
    }
    this.elements.add(elementsItem);
    return this;
  }

  /**
   * Get elements
   * @return elements
   */
  @Valid 
  @Schema(name = "Elements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Elements")
  public List<@Valid RelativePathElement> getElements() {
    return elements;
  }

  public void setElements(List<@Valid RelativePathElement> elements) {
    this.elements = elements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelativePath relativePath = (RelativePath) o;
    return Objects.equals(this.elements, relativePath.elements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelativePath {\n");
    sb.append("    elements: ").append(toIndentedString(elements)).append("\n");
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


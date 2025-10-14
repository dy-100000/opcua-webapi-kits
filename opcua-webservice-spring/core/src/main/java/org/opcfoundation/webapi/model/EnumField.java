package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.7).
 */

@Schema(name = "EnumField", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.7).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EnumField {

  private @Nullable String name;

  private Long value = 0l;

  private @Nullable LocalizedText displayName;

  private @Nullable LocalizedText description;

  public EnumField name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "Name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EnumField value(Long value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "Value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Value")
  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  public EnumField displayName(LocalizedText displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  @Valid 
  @Schema(name = "DisplayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DisplayName")
  public LocalizedText getDisplayName() {
    return displayName;
  }

  public void setDisplayName(LocalizedText displayName) {
    this.displayName = displayName;
  }

  public EnumField description(LocalizedText description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Valid 
  @Schema(name = "Description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Description")
  public LocalizedText getDescription() {
    return description;
  }

  public void setDescription(LocalizedText description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnumField enumField = (EnumField) o;
    return Objects.equals(this.name, enumField.name) &&
        Objects.equals(this.value, enumField.value) &&
        Objects.equals(this.displayName, enumField.displayName) &&
        Objects.equals(this.description, enumField.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value, displayName, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnumField {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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


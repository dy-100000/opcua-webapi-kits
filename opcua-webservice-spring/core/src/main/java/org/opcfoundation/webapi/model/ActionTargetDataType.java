package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.3).
 */

@Schema(name = "ActionTargetDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ActionTargetDataType {

  private Integer actionTargetId = 0;

  private @Nullable String name;

  private @Nullable LocalizedText description;

  public ActionTargetDataType actionTargetId(Integer actionTargetId) {
    this.actionTargetId = actionTargetId;
    return this;
  }

  /**
   * Get actionTargetId
   * minimum: 0
   * maximum: 65535
   * @return actionTargetId
   */
  @Min(0) @Max(65535) 
  @Schema(name = "ActionTargetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ActionTargetId")
  public Integer getActionTargetId() {
    return actionTargetId;
  }

  public void setActionTargetId(Integer actionTargetId) {
    this.actionTargetId = actionTargetId;
  }

  public ActionTargetDataType name(String name) {
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

  public ActionTargetDataType description(LocalizedText description) {
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
    ActionTargetDataType actionTargetDataType = (ActionTargetDataType) o;
    return Objects.equals(this.actionTargetId, actionTargetDataType.actionTargetId) &&
        Objects.equals(this.name, actionTargetDataType.name) &&
        Objects.equals(this.description, actionTargetDataType.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionTargetId, name, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionTargetDataType {\n");
    sb.append("    actionTargetId: ").append(toIndentedString(actionTargetId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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


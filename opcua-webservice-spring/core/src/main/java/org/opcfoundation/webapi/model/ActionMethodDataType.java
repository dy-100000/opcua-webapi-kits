package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.5).
 */

@Schema(name = "ActionMethodDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ActionMethodDataType {

  private @Nullable String objectId;

  private @Nullable String methodId;

  public ActionMethodDataType objectId(String objectId) {
    this.objectId = objectId;
    return this;
  }

  /**
   * Get objectId
   * @return objectId
   */
  
  @Schema(name = "ObjectId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ObjectId")
  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public ActionMethodDataType methodId(String methodId) {
    this.methodId = methodId;
    return this;
  }

  /**
   * Get methodId
   * @return methodId
   */
  
  @Schema(name = "MethodId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MethodId")
  public String getMethodId() {
    return methodId;
  }

  public void setMethodId(String methodId) {
    this.methodId = methodId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionMethodDataType actionMethodDataType = (ActionMethodDataType) o;
    return Objects.equals(this.objectId, actionMethodDataType.objectId) &&
        Objects.equals(this.methodId, actionMethodDataType.methodId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectId, methodId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionMethodDataType {\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
    sb.append("    methodId: ").append(toIndentedString(methodId)).append("\n");
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


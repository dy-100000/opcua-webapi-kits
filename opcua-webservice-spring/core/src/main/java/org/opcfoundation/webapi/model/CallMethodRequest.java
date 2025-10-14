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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2).
 */

@Schema(name = "CallMethodRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CallMethodRequest {

  private @Nullable String objectId;

  private @Nullable String methodId;

  @Valid
  private List<@Valid Variant> inputArguments = new ArrayList<>();

  public CallMethodRequest objectId(String objectId) {
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

  public CallMethodRequest methodId(String methodId) {
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

  public CallMethodRequest inputArguments(List<@Valid Variant> inputArguments) {
    this.inputArguments = inputArguments;
    return this;
  }

  public CallMethodRequest addInputArgumentsItem(Variant inputArgumentsItem) {
    if (this.inputArguments == null) {
      this.inputArguments = new ArrayList<>();
    }
    this.inputArguments.add(inputArgumentsItem);
    return this;
  }

  /**
   * Get inputArguments
   * @return inputArguments
   */
  @Valid 
  @Schema(name = "InputArguments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("InputArguments")
  public List<@Valid Variant> getInputArguments() {
    return inputArguments;
  }

  public void setInputArguments(List<@Valid Variant> inputArguments) {
    this.inputArguments = inputArguments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CallMethodRequest callMethodRequest = (CallMethodRequest) o;
    return Objects.equals(this.objectId, callMethodRequest.objectId) &&
        Objects.equals(this.methodId, callMethodRequest.methodId) &&
        Objects.equals(this.inputArguments, callMethodRequest.inputArguments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectId, methodId, inputArguments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CallMethodRequest {\n");
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
    sb.append("    methodId: ").append(toIndentedString(methodId)).append("\n");
    sb.append("    inputArguments: ").append(toIndentedString(inputArguments)).append("\n");
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


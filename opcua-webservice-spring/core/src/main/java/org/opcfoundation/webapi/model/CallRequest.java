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

@Schema(name = "CallRequest", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class CallRequest {

  private @Nullable RequestHeader requestHeader;

  @Valid
  private List<@Valid CallMethodRequest> methodsToCall = new ArrayList<>();

  public CallRequest requestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
    return this;
  }

  /**
   * Get requestHeader
   * @return requestHeader
   */
  @Valid 
  @Schema(name = "RequestHeader", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestHeader")
  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public void setRequestHeader(RequestHeader requestHeader) {
    this.requestHeader = requestHeader;
  }

  public CallRequest methodsToCall(List<@Valid CallMethodRequest> methodsToCall) {
    this.methodsToCall = methodsToCall;
    return this;
  }

  public CallRequest addMethodsToCallItem(CallMethodRequest methodsToCallItem) {
    if (this.methodsToCall == null) {
      this.methodsToCall = new ArrayList<>();
    }
    this.methodsToCall.add(methodsToCallItem);
    return this;
  }

  /**
   * Get methodsToCall
   * @return methodsToCall
   */
  @Valid 
  @Schema(name = "MethodsToCall", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MethodsToCall")
  public List<@Valid CallMethodRequest> getMethodsToCall() {
    return methodsToCall;
  }

  public void setMethodsToCall(List<@Valid CallMethodRequest> methodsToCall) {
    this.methodsToCall = methodsToCall;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CallRequest callRequest = (CallRequest) o;
    return Objects.equals(this.requestHeader, callRequest.requestHeader) &&
        Objects.equals(this.methodsToCall, callRequest.methodsToCall);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestHeader, methodsToCall);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CallRequest {\n");
    sb.append("    requestHeader: ").append(toIndentedString(requestHeader)).append("\n");
    sb.append("    methodsToCall: ").append(toIndentedString(methodsToCall)).append("\n");
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


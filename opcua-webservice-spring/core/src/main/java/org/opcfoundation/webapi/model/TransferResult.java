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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2).
 */

@Schema(name = "TransferResult", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class TransferResult {

  private @Nullable StatusCode statusCode;

  @Valid
  private List<@Min(0L) @Max(4294967295L)Long> availableSequenceNumbers = new ArrayList<>();

  public TransferResult statusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Get statusCode
   * @return statusCode
   */
  @Valid 
  @Schema(name = "StatusCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StatusCode")
  public StatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public TransferResult availableSequenceNumbers(List<@Min(0L) @Max(4294967295L)Long> availableSequenceNumbers) {
    this.availableSequenceNumbers = availableSequenceNumbers;
    return this;
  }

  public TransferResult addAvailableSequenceNumbersItem(Long availableSequenceNumbersItem) {
    if (this.availableSequenceNumbers == null) {
      this.availableSequenceNumbers = new ArrayList<>();
    }
    this.availableSequenceNumbers.add(availableSequenceNumbersItem);
    return this;
  }

  /**
   * Get availableSequenceNumbers
   * @return availableSequenceNumbers
   */
  
  @Schema(name = "AvailableSequenceNumbers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AvailableSequenceNumbers")
  public List<@Min(0L) @Max(4294967295L)Long> getAvailableSequenceNumbers() {
    return availableSequenceNumbers;
  }

  public void setAvailableSequenceNumbers(List<@Min(0L) @Max(4294967295L)Long> availableSequenceNumbers) {
    this.availableSequenceNumbers = availableSequenceNumbers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferResult transferResult = (TransferResult) o;
    return Objects.equals(this.statusCode, transferResult.statusCode) &&
        Objects.equals(this.availableSequenceNumbers, transferResult.availableSequenceNumbers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, availableSequenceNumbers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferResult {\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    availableSequenceNumbers: ").append(toIndentedString(availableSequenceNumbers)).append("\n");
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


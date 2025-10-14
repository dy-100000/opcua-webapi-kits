package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part4/v105/docs/7.39). [Set of defined codes](https://github.com/OPCFoundation/UA-Nodeset/tree/latest/Schema/StatusCode.csv).
 */

@Schema(name = "StatusCode", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part4/v105/docs/7.39). [Set of defined codes](https://github.com/OPCFoundation/UA-Nodeset/tree/latest/Schema/StatusCode.csv).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class StatusCode {

  private @Nullable Long code;

  private @Nullable String symbol;

  public StatusCode code(Long code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * minimum: 0
   * maximum: 4294967295
   * @return code
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "Code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Code")
  public Long getCode() {
    return code;
  }

  public void setCode(Long code) {
    this.code = code;
  }

  public StatusCode symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

  /**
   * Get symbol
   * @return symbol
   */
  
  @Schema(name = "Symbol", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusCode statusCode = (StatusCode) o;
    return Objects.equals(this.code, statusCode.code) &&
        Objects.equals(this.symbol, statusCode.symbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, symbol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusCode {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
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


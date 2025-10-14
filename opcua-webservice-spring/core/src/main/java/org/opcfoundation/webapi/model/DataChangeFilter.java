package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.2).
 */

@Schema(name = "DataChangeFilter", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataChangeFilter {

  private @Nullable Integer trigger;

  private Long deadbandType = 0l;

  private Double deadbandValue = 0d;

  public DataChangeFilter trigger(Integer trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.10).
   * @return trigger
   */
  
  @Schema(name = "Trigger", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.10).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Trigger")
  public Integer getTrigger() {
    return trigger;
  }

  public void setTrigger(Integer trigger) {
    this.trigger = trigger;
  }

  public DataChangeFilter deadbandType(Long deadbandType) {
    this.deadbandType = deadbandType;
    return this;
  }

  /**
   * Get deadbandType
   * minimum: 0
   * maximum: 4294967295
   * @return deadbandType
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "DeadbandType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DeadbandType")
  public Long getDeadbandType() {
    return deadbandType;
  }

  public void setDeadbandType(Long deadbandType) {
    this.deadbandType = deadbandType;
  }

  public DataChangeFilter deadbandValue(Double deadbandValue) {
    this.deadbandValue = deadbandValue;
    return this;
  }

  /**
   * Get deadbandValue
   * @return deadbandValue
   */
  
  @Schema(name = "DeadbandValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DeadbandValue")
  public Double getDeadbandValue() {
    return deadbandValue;
  }

  public void setDeadbandValue(Double deadbandValue) {
    this.deadbandValue = deadbandValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataChangeFilter dataChangeFilter = (DataChangeFilter) o;
    return Objects.equals(this.trigger, dataChangeFilter.trigger) &&
        Objects.equals(this.deadbandType, dataChangeFilter.deadbandType) &&
        Objects.equals(this.deadbandValue, dataChangeFilter.deadbandValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trigger, deadbandType, deadbandValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataChangeFilter {\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    deadbandType: ").append(toIndentedString(deadbandType)).append("\n");
    sb.append("    deadbandValue: ").append(toIndentedString(deadbandValue)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.4/#6.5.4.1).
 */

@Schema(name = "AggregateConfiguration", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.4/#6.5.4.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class AggregateConfiguration {

  private Boolean useServerCapabilitiesDefaults = false;

  private Boolean treatUncertainAsBad = false;

  private Integer percentDataBad = 0;

  private Integer percentDataGood = 0;

  private Boolean useSlopedExtrapolation = false;

  public AggregateConfiguration useServerCapabilitiesDefaults(Boolean useServerCapabilitiesDefaults) {
    this.useServerCapabilitiesDefaults = useServerCapabilitiesDefaults;
    return this;
  }

  /**
   * Get useServerCapabilitiesDefaults
   * @return useServerCapabilitiesDefaults
   */
  
  @Schema(name = "UseServerCapabilitiesDefaults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UseServerCapabilitiesDefaults")
  public Boolean getUseServerCapabilitiesDefaults() {
    return useServerCapabilitiesDefaults;
  }

  public void setUseServerCapabilitiesDefaults(Boolean useServerCapabilitiesDefaults) {
    this.useServerCapabilitiesDefaults = useServerCapabilitiesDefaults;
  }

  public AggregateConfiguration treatUncertainAsBad(Boolean treatUncertainAsBad) {
    this.treatUncertainAsBad = treatUncertainAsBad;
    return this;
  }

  /**
   * Get treatUncertainAsBad
   * @return treatUncertainAsBad
   */
  
  @Schema(name = "TreatUncertainAsBad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TreatUncertainAsBad")
  public Boolean getTreatUncertainAsBad() {
    return treatUncertainAsBad;
  }

  public void setTreatUncertainAsBad(Boolean treatUncertainAsBad) {
    this.treatUncertainAsBad = treatUncertainAsBad;
  }

  public AggregateConfiguration percentDataBad(Integer percentDataBad) {
    this.percentDataBad = percentDataBad;
    return this;
  }

  /**
   * Get percentDataBad
   * minimum: 0
   * maximum: 255
   * @return percentDataBad
   */
  @Min(0) @Max(255) 
  @Schema(name = "PercentDataBad", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PercentDataBad")
  public Integer getPercentDataBad() {
    return percentDataBad;
  }

  public void setPercentDataBad(Integer percentDataBad) {
    this.percentDataBad = percentDataBad;
  }

  public AggregateConfiguration percentDataGood(Integer percentDataGood) {
    this.percentDataGood = percentDataGood;
    return this;
  }

  /**
   * Get percentDataGood
   * minimum: 0
   * maximum: 255
   * @return percentDataGood
   */
  @Min(0) @Max(255) 
  @Schema(name = "PercentDataGood", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PercentDataGood")
  public Integer getPercentDataGood() {
    return percentDataGood;
  }

  public void setPercentDataGood(Integer percentDataGood) {
    this.percentDataGood = percentDataGood;
  }

  public AggregateConfiguration useSlopedExtrapolation(Boolean useSlopedExtrapolation) {
    this.useSlopedExtrapolation = useSlopedExtrapolation;
    return this;
  }

  /**
   * Get useSlopedExtrapolation
   * @return useSlopedExtrapolation
   */
  
  @Schema(name = "UseSlopedExtrapolation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UseSlopedExtrapolation")
  public Boolean getUseSlopedExtrapolation() {
    return useSlopedExtrapolation;
  }

  public void setUseSlopedExtrapolation(Boolean useSlopedExtrapolation) {
    this.useSlopedExtrapolation = useSlopedExtrapolation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggregateConfiguration aggregateConfiguration = (AggregateConfiguration) o;
    return Objects.equals(this.useServerCapabilitiesDefaults, aggregateConfiguration.useServerCapabilitiesDefaults) &&
        Objects.equals(this.treatUncertainAsBad, aggregateConfiguration.treatUncertainAsBad) &&
        Objects.equals(this.percentDataBad, aggregateConfiguration.percentDataBad) &&
        Objects.equals(this.percentDataGood, aggregateConfiguration.percentDataGood) &&
        Objects.equals(this.useSlopedExtrapolation, aggregateConfiguration.useSlopedExtrapolation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(useServerCapabilitiesDefaults, treatUncertainAsBad, percentDataBad, percentDataGood, useSlopedExtrapolation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggregateConfiguration {\n");
    sb.append("    useServerCapabilitiesDefaults: ").append(toIndentedString(useServerCapabilitiesDefaults)).append("\n");
    sb.append("    treatUncertainAsBad: ").append(toIndentedString(treatUncertainAsBad)).append("\n");
    sb.append("    percentDataBad: ").append(toIndentedString(percentDataBad)).append("\n");
    sb.append("    percentDataGood: ").append(toIndentedString(percentDataGood)).append("\n");
    sb.append("    useSlopedExtrapolation: ").append(toIndentedString(useSlopedExtrapolation)).append("\n");
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


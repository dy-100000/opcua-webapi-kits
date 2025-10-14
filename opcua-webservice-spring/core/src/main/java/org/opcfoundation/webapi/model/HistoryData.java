package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.2).
 */

@Schema(name = "HistoryData", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryData {

  @Valid
  private List<@Valid DataValue> dataValues = new ArrayList<>();

  public HistoryData dataValues(List<@Valid DataValue> dataValues) {
    this.dataValues = dataValues;
    return this;
  }

  public HistoryData addDataValuesItem(DataValue dataValuesItem) {
    if (this.dataValues == null) {
      this.dataValues = new ArrayList<>();
    }
    this.dataValues.add(dataValuesItem);
    return this;
  }

  /**
   * Get dataValues
   * @return dataValues
   */
  @Valid 
  @Schema(name = "DataValues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataValues")
  public List<@Valid DataValue> getDataValues() {
    return dataValues;
  }

  public void setDataValues(List<@Valid DataValue> dataValues) {
    this.dataValues = dataValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HistoryData historyData = (HistoryData) o;
    return Objects.equals(this.dataValues, historyData.dataValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryData {\n");
    sb.append("    dataValues: ").append(toIndentedString(dataValues)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.3).
 */

@Schema(name = "HistoryModifiedData", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class HistoryModifiedData {

  @Valid
  private List<@Valid ModificationInfo> modificationInfos = new ArrayList<>();

  @Valid
  private List<@Valid DataValue> dataValues = new ArrayList<>();

  public HistoryModifiedData modificationInfos(List<@Valid ModificationInfo> modificationInfos) {
    this.modificationInfos = modificationInfos;
    return this;
  }

  public HistoryModifiedData addModificationInfosItem(ModificationInfo modificationInfosItem) {
    if (this.modificationInfos == null) {
      this.modificationInfos = new ArrayList<>();
    }
    this.modificationInfos.add(modificationInfosItem);
    return this;
  }

  /**
   * Get modificationInfos
   * @return modificationInfos
   */
  @Valid 
  @Schema(name = "ModificationInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ModificationInfos")
  public List<@Valid ModificationInfo> getModificationInfos() {
    return modificationInfos;
  }

  public void setModificationInfos(List<@Valid ModificationInfo> modificationInfos) {
    this.modificationInfos = modificationInfos;
  }

  public HistoryModifiedData dataValues(List<@Valid DataValue> dataValues) {
    this.dataValues = dataValues;
    return this;
  }

  public HistoryModifiedData addDataValuesItem(DataValue dataValuesItem) {
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
    HistoryModifiedData historyModifiedData = (HistoryModifiedData) o;
    return Objects.equals(this.modificationInfos, historyModifiedData.modificationInfos) &&
        Objects.equals(this.dataValues, historyModifiedData.dataValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modificationInfos, dataValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HistoryModifiedData {\n");
    sb.append("    modificationInfos: ").append(toIndentedString(modificationInfos)).append("\n");
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


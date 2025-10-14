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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.3/#6.9.3.1).
 */

@Schema(name = "UpdateStructureDataDetails", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.3/#6.9.3.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class UpdateStructureDataDetails {

  private @Nullable String nodeId;

  private @Nullable Integer performInsertReplace;

  @Valid
  private List<@Valid DataValue> updateValues = new ArrayList<>();

  public UpdateStructureDataDetails nodeId(String nodeId) {
    this.nodeId = nodeId;
    return this;
  }

  /**
   * Get nodeId
   * @return nodeId
   */
  
  @Schema(name = "NodeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NodeId")
  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public UpdateStructureDataDetails performInsertReplace(Integer performInsertReplace) {
    this.performInsertReplace = performInsertReplace;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.8).
   * @return performInsertReplace
   */
  
  @Schema(name = "PerformInsertReplace", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.8).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PerformInsertReplace")
  public Integer getPerformInsertReplace() {
    return performInsertReplace;
  }

  public void setPerformInsertReplace(Integer performInsertReplace) {
    this.performInsertReplace = performInsertReplace;
  }

  public UpdateStructureDataDetails updateValues(List<@Valid DataValue> updateValues) {
    this.updateValues = updateValues;
    return this;
  }

  public UpdateStructureDataDetails addUpdateValuesItem(DataValue updateValuesItem) {
    if (this.updateValues == null) {
      this.updateValues = new ArrayList<>();
    }
    this.updateValues.add(updateValuesItem);
    return this;
  }

  /**
   * Get updateValues
   * @return updateValues
   */
  @Valid 
  @Schema(name = "UpdateValues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UpdateValues")
  public List<@Valid DataValue> getUpdateValues() {
    return updateValues;
  }

  public void setUpdateValues(List<@Valid DataValue> updateValues) {
    this.updateValues = updateValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateStructureDataDetails updateStructureDataDetails = (UpdateStructureDataDetails) o;
    return Objects.equals(this.nodeId, updateStructureDataDetails.nodeId) &&
        Objects.equals(this.performInsertReplace, updateStructureDataDetails.performInsertReplace) &&
        Objects.equals(this.updateValues, updateStructureDataDetails.updateValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeId, performInsertReplace, updateValues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateStructureDataDetails {\n");
    sb.append("    nodeId: ").append(toIndentedString(nodeId)).append("\n");
    sb.append("    performInsertReplace: ").append(toIndentedString(performInsertReplace)).append("\n");
    sb.append("    updateValues: ").append(toIndentedString(updateValues)).append("\n");
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


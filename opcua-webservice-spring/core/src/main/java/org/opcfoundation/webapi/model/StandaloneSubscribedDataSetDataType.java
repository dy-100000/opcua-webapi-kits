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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.5).
 */

@Schema(name = "StandaloneSubscribedDataSetDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.10/#6.2.10.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class StandaloneSubscribedDataSetDataType {

  private @Nullable String name;

  @Valid
  private List<String> dataSetFolder = new ArrayList<>();

  private @Nullable DataSetMetaDataType dataSetMetaData;

  private @Nullable Object subscribedDataSet;

  public StandaloneSubscribedDataSetDataType name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "Name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public StandaloneSubscribedDataSetDataType dataSetFolder(List<String> dataSetFolder) {
    this.dataSetFolder = dataSetFolder;
    return this;
  }

  public StandaloneSubscribedDataSetDataType addDataSetFolderItem(String dataSetFolderItem) {
    if (this.dataSetFolder == null) {
      this.dataSetFolder = new ArrayList<>();
    }
    this.dataSetFolder.add(dataSetFolderItem);
    return this;
  }

  /**
   * Get dataSetFolder
   * @return dataSetFolder
   */
  
  @Schema(name = "DataSetFolder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetFolder")
  public List<String> getDataSetFolder() {
    return dataSetFolder;
  }

  public void setDataSetFolder(List<String> dataSetFolder) {
    this.dataSetFolder = dataSetFolder;
  }

  public StandaloneSubscribedDataSetDataType dataSetMetaData(DataSetMetaDataType dataSetMetaData) {
    this.dataSetMetaData = dataSetMetaData;
    return this;
  }

  /**
   * Get dataSetMetaData
   * @return dataSetMetaData
   */
  @Valid 
  @Schema(name = "DataSetMetaData", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetMetaData")
  public DataSetMetaDataType getDataSetMetaData() {
    return dataSetMetaData;
  }

  public void setDataSetMetaData(DataSetMetaDataType dataSetMetaData) {
    this.dataSetMetaData = dataSetMetaData;
  }

  public StandaloneSubscribedDataSetDataType subscribedDataSet(Object subscribedDataSet) {
    this.subscribedDataSet = subscribedDataSet;
    return this;
  }

  /**
   * Get subscribedDataSet
   * @return subscribedDataSet
   */
  
  @Schema(name = "SubscribedDataSet", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscribedDataSet")
  public Object getSubscribedDataSet() {
    return subscribedDataSet;
  }

  public void setSubscribedDataSet(Object subscribedDataSet) {
    this.subscribedDataSet = subscribedDataSet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StandaloneSubscribedDataSetDataType standaloneSubscribedDataSetDataType = (StandaloneSubscribedDataSetDataType) o;
    return Objects.equals(this.name, standaloneSubscribedDataSetDataType.name) &&
        Objects.equals(this.dataSetFolder, standaloneSubscribedDataSetDataType.dataSetFolder) &&
        Objects.equals(this.dataSetMetaData, standaloneSubscribedDataSetDataType.dataSetMetaData) &&
        Objects.equals(this.subscribedDataSet, standaloneSubscribedDataSetDataType.subscribedDataSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dataSetFolder, dataSetMetaData, subscribedDataSet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StandaloneSubscribedDataSetDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dataSetFolder: ").append(toIndentedString(dataSetFolder)).append("\n");
    sb.append("    dataSetMetaData: ").append(toIndentedString(dataSetMetaData)).append("\n");
    sb.append("    subscribedDataSet: ").append(toIndentedString(subscribedDataSet)).append("\n");
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


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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.5).
 */

@Schema(name = "PublishedDataSetDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PublishedDataSetDataType {

  private @Nullable String name;

  @Valid
  private List<String> dataSetFolder = new ArrayList<>();

  private @Nullable DataSetMetaDataType dataSetMetaData;

  @Valid
  private List<@Valid KeyValuePair> extensionFields = new ArrayList<>();

  private @Nullable Object dataSetSource;

  public PublishedDataSetDataType name(String name) {
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

  public PublishedDataSetDataType dataSetFolder(List<String> dataSetFolder) {
    this.dataSetFolder = dataSetFolder;
    return this;
  }

  public PublishedDataSetDataType addDataSetFolderItem(String dataSetFolderItem) {
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

  public PublishedDataSetDataType dataSetMetaData(DataSetMetaDataType dataSetMetaData) {
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

  public PublishedDataSetDataType extensionFields(List<@Valid KeyValuePair> extensionFields) {
    this.extensionFields = extensionFields;
    return this;
  }

  public PublishedDataSetDataType addExtensionFieldsItem(KeyValuePair extensionFieldsItem) {
    if (this.extensionFields == null) {
      this.extensionFields = new ArrayList<>();
    }
    this.extensionFields.add(extensionFieldsItem);
    return this;
  }

  /**
   * Get extensionFields
   * @return extensionFields
   */
  @Valid 
  @Schema(name = "ExtensionFields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ExtensionFields")
  public List<@Valid KeyValuePair> getExtensionFields() {
    return extensionFields;
  }

  public void setExtensionFields(List<@Valid KeyValuePair> extensionFields) {
    this.extensionFields = extensionFields;
  }

  public PublishedDataSetDataType dataSetSource(Object dataSetSource) {
    this.dataSetSource = dataSetSource;
    return this;
  }

  /**
   * Get dataSetSource
   * @return dataSetSource
   */
  
  @Schema(name = "DataSetSource", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetSource")
  public Object getDataSetSource() {
    return dataSetSource;
  }

  public void setDataSetSource(Object dataSetSource) {
    this.dataSetSource = dataSetSource;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PublishedDataSetDataType publishedDataSetDataType = (PublishedDataSetDataType) o;
    return Objects.equals(this.name, publishedDataSetDataType.name) &&
        Objects.equals(this.dataSetFolder, publishedDataSetDataType.dataSetFolder) &&
        Objects.equals(this.dataSetMetaData, publishedDataSetDataType.dataSetMetaData) &&
        Objects.equals(this.extensionFields, publishedDataSetDataType.extensionFields) &&
        Objects.equals(this.dataSetSource, publishedDataSetDataType.dataSetSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dataSetFolder, dataSetMetaData, extensionFields, dataSetSource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PublishedDataSetDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dataSetFolder: ").append(toIndentedString(dataSetFolder)).append("\n");
    sb.append("    dataSetMetaData: ").append(toIndentedString(dataSetMetaData)).append("\n");
    sb.append("    extensionFields: ").append(toIndentedString(extensionFields)).append("\n");
    sb.append("    dataSetSource: ").append(toIndentedString(dataSetSource)).append("\n");
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


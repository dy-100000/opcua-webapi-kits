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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.4/#6.2.4.5.1).
 */

@Schema(name = "DataSetWriterDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.4/#6.2.4.5.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataSetWriterDataType {

  private @Nullable String name;

  private Boolean enabled = false;

  private Integer dataSetWriterId = 0;

  private Long dataSetFieldContentMask = 0l;

  private Long keyFrameCount = 0l;

  private @Nullable String dataSetName;

  @Valid
  private List<@Valid KeyValuePair> dataSetWriterProperties = new ArrayList<>();

  private @Nullable Object transportSettings;

  private @Nullable Object messageSettings;

  public DataSetWriterDataType name(String name) {
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

  public DataSetWriterDataType enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "Enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public DataSetWriterDataType dataSetWriterId(Integer dataSetWriterId) {
    this.dataSetWriterId = dataSetWriterId;
    return this;
  }

  /**
   * Get dataSetWriterId
   * minimum: 0
   * maximum: 65535
   * @return dataSetWriterId
   */
  @Min(0) @Max(65535) 
  @Schema(name = "DataSetWriterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetWriterId")
  public Integer getDataSetWriterId() {
    return dataSetWriterId;
  }

  public void setDataSetWriterId(Integer dataSetWriterId) {
    this.dataSetWriterId = dataSetWriterId;
  }

  public DataSetWriterDataType dataSetFieldContentMask(Long dataSetFieldContentMask) {
    this.dataSetFieldContentMask = dataSetFieldContentMask;
    return this;
  }

  /**
   * Get dataSetFieldContentMask
   * minimum: 0
   * maximum: 4294967295
   * @return dataSetFieldContentMask
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "DataSetFieldContentMask", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetFieldContentMask")
  public Long getDataSetFieldContentMask() {
    return dataSetFieldContentMask;
  }

  public void setDataSetFieldContentMask(Long dataSetFieldContentMask) {
    this.dataSetFieldContentMask = dataSetFieldContentMask;
  }

  public DataSetWriterDataType keyFrameCount(Long keyFrameCount) {
    this.keyFrameCount = keyFrameCount;
    return this;
  }

  /**
   * Get keyFrameCount
   * minimum: 0
   * maximum: 4294967295
   * @return keyFrameCount
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "KeyFrameCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("KeyFrameCount")
  public Long getKeyFrameCount() {
    return keyFrameCount;
  }

  public void setKeyFrameCount(Long keyFrameCount) {
    this.keyFrameCount = keyFrameCount;
  }

  public DataSetWriterDataType dataSetName(String dataSetName) {
    this.dataSetName = dataSetName;
    return this;
  }

  /**
   * Get dataSetName
   * @return dataSetName
   */
  
  @Schema(name = "DataSetName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetName")
  public String getDataSetName() {
    return dataSetName;
  }

  public void setDataSetName(String dataSetName) {
    this.dataSetName = dataSetName;
  }

  public DataSetWriterDataType dataSetWriterProperties(List<@Valid KeyValuePair> dataSetWriterProperties) {
    this.dataSetWriterProperties = dataSetWriterProperties;
    return this;
  }

  public DataSetWriterDataType addDataSetWriterPropertiesItem(KeyValuePair dataSetWriterPropertiesItem) {
    if (this.dataSetWriterProperties == null) {
      this.dataSetWriterProperties = new ArrayList<>();
    }
    this.dataSetWriterProperties.add(dataSetWriterPropertiesItem);
    return this;
  }

  /**
   * Get dataSetWriterProperties
   * @return dataSetWriterProperties
   */
  @Valid 
  @Schema(name = "DataSetWriterProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetWriterProperties")
  public List<@Valid KeyValuePair> getDataSetWriterProperties() {
    return dataSetWriterProperties;
  }

  public void setDataSetWriterProperties(List<@Valid KeyValuePair> dataSetWriterProperties) {
    this.dataSetWriterProperties = dataSetWriterProperties;
  }

  public DataSetWriterDataType transportSettings(Object transportSettings) {
    this.transportSettings = transportSettings;
    return this;
  }

  /**
   * Get transportSettings
   * @return transportSettings
   */
  
  @Schema(name = "TransportSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TransportSettings")
  public Object getTransportSettings() {
    return transportSettings;
  }

  public void setTransportSettings(Object transportSettings) {
    this.transportSettings = transportSettings;
  }

  public DataSetWriterDataType messageSettings(Object messageSettings) {
    this.messageSettings = messageSettings;
    return this;
  }

  /**
   * Get messageSettings
   * @return messageSettings
   */
  
  @Schema(name = "MessageSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MessageSettings")
  public Object getMessageSettings() {
    return messageSettings;
  }

  public void setMessageSettings(Object messageSettings) {
    this.messageSettings = messageSettings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataSetWriterDataType dataSetWriterDataType = (DataSetWriterDataType) o;
    return Objects.equals(this.name, dataSetWriterDataType.name) &&
        Objects.equals(this.enabled, dataSetWriterDataType.enabled) &&
        Objects.equals(this.dataSetWriterId, dataSetWriterDataType.dataSetWriterId) &&
        Objects.equals(this.dataSetFieldContentMask, dataSetWriterDataType.dataSetFieldContentMask) &&
        Objects.equals(this.keyFrameCount, dataSetWriterDataType.keyFrameCount) &&
        Objects.equals(this.dataSetName, dataSetWriterDataType.dataSetName) &&
        Objects.equals(this.dataSetWriterProperties, dataSetWriterDataType.dataSetWriterProperties) &&
        Objects.equals(this.transportSettings, dataSetWriterDataType.transportSettings) &&
        Objects.equals(this.messageSettings, dataSetWriterDataType.messageSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, enabled, dataSetWriterId, dataSetFieldContentMask, keyFrameCount, dataSetName, dataSetWriterProperties, transportSettings, messageSettings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataSetWriterDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    dataSetWriterId: ").append(toIndentedString(dataSetWriterId)).append("\n");
    sb.append("    dataSetFieldContentMask: ").append(toIndentedString(dataSetFieldContentMask)).append("\n");
    sb.append("    keyFrameCount: ").append(toIndentedString(keyFrameCount)).append("\n");
    sb.append("    dataSetName: ").append(toIndentedString(dataSetName)).append("\n");
    sb.append("    dataSetWriterProperties: ").append(toIndentedString(dataSetWriterProperties)).append("\n");
    sb.append("    transportSettings: ").append(toIndentedString(transportSettings)).append("\n");
    sb.append("    messageSettings: ").append(toIndentedString(messageSettings)).append("\n");
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


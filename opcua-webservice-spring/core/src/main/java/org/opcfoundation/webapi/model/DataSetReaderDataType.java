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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.9/#6.2.9.13.1).
 */

@Schema(name = "DataSetReaderDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.9/#6.2.9.13.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataSetReaderDataType {

  private @Nullable String name;

  private Boolean enabled = false;

  private @Nullable Variant publisherId;

  private Integer writerGroupId = 0;

  private Integer dataSetWriterId = 0;

  private @Nullable DataSetMetaDataType dataSetMetaData;

  private Long dataSetFieldContentMask = 0l;

  private Double messageReceiveTimeout = 0d;

  private Long keyFrameCount = 0l;

  private @Nullable String headerLayoutUri;

  private @Nullable Integer securityMode;

  private @Nullable String securityGroupId;

  @Valid
  private List<@Valid EndpointDescription> securityKeyServices = new ArrayList<>();

  @Valid
  private List<@Valid KeyValuePair> dataSetReaderProperties = new ArrayList<>();

  private @Nullable Object transportSettings;

  private @Nullable Object messageSettings;

  private @Nullable Object subscribedDataSet;

  public DataSetReaderDataType name(String name) {
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

  public DataSetReaderDataType enabled(Boolean enabled) {
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

  public DataSetReaderDataType publisherId(Variant publisherId) {
    this.publisherId = publisherId;
    return this;
  }

  /**
   * Get publisherId
   * @return publisherId
   */
  @Valid 
  @Schema(name = "PublisherId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PublisherId")
  public Variant getPublisherId() {
    return publisherId;
  }

  public void setPublisherId(Variant publisherId) {
    this.publisherId = publisherId;
  }

  public DataSetReaderDataType writerGroupId(Integer writerGroupId) {
    this.writerGroupId = writerGroupId;
    return this;
  }

  /**
   * Get writerGroupId
   * minimum: 0
   * maximum: 65535
   * @return writerGroupId
   */
  @Min(0) @Max(65535) 
  @Schema(name = "WriterGroupId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("WriterGroupId")
  public Integer getWriterGroupId() {
    return writerGroupId;
  }

  public void setWriterGroupId(Integer writerGroupId) {
    this.writerGroupId = writerGroupId;
  }

  public DataSetReaderDataType dataSetWriterId(Integer dataSetWriterId) {
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

  public DataSetReaderDataType dataSetMetaData(DataSetMetaDataType dataSetMetaData) {
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

  public DataSetReaderDataType dataSetFieldContentMask(Long dataSetFieldContentMask) {
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

  public DataSetReaderDataType messageReceiveTimeout(Double messageReceiveTimeout) {
    this.messageReceiveTimeout = messageReceiveTimeout;
    return this;
  }

  /**
   * Get messageReceiveTimeout
   * @return messageReceiveTimeout
   */
  
  @Schema(name = "MessageReceiveTimeout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MessageReceiveTimeout")
  public Double getMessageReceiveTimeout() {
    return messageReceiveTimeout;
  }

  public void setMessageReceiveTimeout(Double messageReceiveTimeout) {
    this.messageReceiveTimeout = messageReceiveTimeout;
  }

  public DataSetReaderDataType keyFrameCount(Long keyFrameCount) {
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

  public DataSetReaderDataType headerLayoutUri(String headerLayoutUri) {
    this.headerLayoutUri = headerLayoutUri;
    return this;
  }

  /**
   * Get headerLayoutUri
   * @return headerLayoutUri
   */
  
  @Schema(name = "HeaderLayoutUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("HeaderLayoutUri")
  public String getHeaderLayoutUri() {
    return headerLayoutUri;
  }

  public void setHeaderLayoutUri(String headerLayoutUri) {
    this.headerLayoutUri = headerLayoutUri;
  }

  public DataSetReaderDataType securityMode(Integer securityMode) {
    this.securityMode = securityMode;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.10).
   * @return securityMode
   */
  
  @Schema(name = "SecurityMode", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.10).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityMode")
  public Integer getSecurityMode() {
    return securityMode;
  }

  public void setSecurityMode(Integer securityMode) {
    this.securityMode = securityMode;
  }

  public DataSetReaderDataType securityGroupId(String securityGroupId) {
    this.securityGroupId = securityGroupId;
    return this;
  }

  /**
   * Get securityGroupId
   * @return securityGroupId
   */
  
  @Schema(name = "SecurityGroupId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityGroupId")
  public String getSecurityGroupId() {
    return securityGroupId;
  }

  public void setSecurityGroupId(String securityGroupId) {
    this.securityGroupId = securityGroupId;
  }

  public DataSetReaderDataType securityKeyServices(List<@Valid EndpointDescription> securityKeyServices) {
    this.securityKeyServices = securityKeyServices;
    return this;
  }

  public DataSetReaderDataType addSecurityKeyServicesItem(EndpointDescription securityKeyServicesItem) {
    if (this.securityKeyServices == null) {
      this.securityKeyServices = new ArrayList<>();
    }
    this.securityKeyServices.add(securityKeyServicesItem);
    return this;
  }

  /**
   * Get securityKeyServices
   * @return securityKeyServices
   */
  @Valid 
  @Schema(name = "SecurityKeyServices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityKeyServices")
  public List<@Valid EndpointDescription> getSecurityKeyServices() {
    return securityKeyServices;
  }

  public void setSecurityKeyServices(List<@Valid EndpointDescription> securityKeyServices) {
    this.securityKeyServices = securityKeyServices;
  }

  public DataSetReaderDataType dataSetReaderProperties(List<@Valid KeyValuePair> dataSetReaderProperties) {
    this.dataSetReaderProperties = dataSetReaderProperties;
    return this;
  }

  public DataSetReaderDataType addDataSetReaderPropertiesItem(KeyValuePair dataSetReaderPropertiesItem) {
    if (this.dataSetReaderProperties == null) {
      this.dataSetReaderProperties = new ArrayList<>();
    }
    this.dataSetReaderProperties.add(dataSetReaderPropertiesItem);
    return this;
  }

  /**
   * Get dataSetReaderProperties
   * @return dataSetReaderProperties
   */
  @Valid 
  @Schema(name = "DataSetReaderProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetReaderProperties")
  public List<@Valid KeyValuePair> getDataSetReaderProperties() {
    return dataSetReaderProperties;
  }

  public void setDataSetReaderProperties(List<@Valid KeyValuePair> dataSetReaderProperties) {
    this.dataSetReaderProperties = dataSetReaderProperties;
  }

  public DataSetReaderDataType transportSettings(Object transportSettings) {
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

  public DataSetReaderDataType messageSettings(Object messageSettings) {
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

  public DataSetReaderDataType subscribedDataSet(Object subscribedDataSet) {
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
    DataSetReaderDataType dataSetReaderDataType = (DataSetReaderDataType) o;
    return Objects.equals(this.name, dataSetReaderDataType.name) &&
        Objects.equals(this.enabled, dataSetReaderDataType.enabled) &&
        Objects.equals(this.publisherId, dataSetReaderDataType.publisherId) &&
        Objects.equals(this.writerGroupId, dataSetReaderDataType.writerGroupId) &&
        Objects.equals(this.dataSetWriterId, dataSetReaderDataType.dataSetWriterId) &&
        Objects.equals(this.dataSetMetaData, dataSetReaderDataType.dataSetMetaData) &&
        Objects.equals(this.dataSetFieldContentMask, dataSetReaderDataType.dataSetFieldContentMask) &&
        Objects.equals(this.messageReceiveTimeout, dataSetReaderDataType.messageReceiveTimeout) &&
        Objects.equals(this.keyFrameCount, dataSetReaderDataType.keyFrameCount) &&
        Objects.equals(this.headerLayoutUri, dataSetReaderDataType.headerLayoutUri) &&
        Objects.equals(this.securityMode, dataSetReaderDataType.securityMode) &&
        Objects.equals(this.securityGroupId, dataSetReaderDataType.securityGroupId) &&
        Objects.equals(this.securityKeyServices, dataSetReaderDataType.securityKeyServices) &&
        Objects.equals(this.dataSetReaderProperties, dataSetReaderDataType.dataSetReaderProperties) &&
        Objects.equals(this.transportSettings, dataSetReaderDataType.transportSettings) &&
        Objects.equals(this.messageSettings, dataSetReaderDataType.messageSettings) &&
        Objects.equals(this.subscribedDataSet, dataSetReaderDataType.subscribedDataSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, enabled, publisherId, writerGroupId, dataSetWriterId, dataSetMetaData, dataSetFieldContentMask, messageReceiveTimeout, keyFrameCount, headerLayoutUri, securityMode, securityGroupId, securityKeyServices, dataSetReaderProperties, transportSettings, messageSettings, subscribedDataSet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataSetReaderDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    writerGroupId: ").append(toIndentedString(writerGroupId)).append("\n");
    sb.append("    dataSetWriterId: ").append(toIndentedString(dataSetWriterId)).append("\n");
    sb.append("    dataSetMetaData: ").append(toIndentedString(dataSetMetaData)).append("\n");
    sb.append("    dataSetFieldContentMask: ").append(toIndentedString(dataSetFieldContentMask)).append("\n");
    sb.append("    messageReceiveTimeout: ").append(toIndentedString(messageReceiveTimeout)).append("\n");
    sb.append("    keyFrameCount: ").append(toIndentedString(keyFrameCount)).append("\n");
    sb.append("    headerLayoutUri: ").append(toIndentedString(headerLayoutUri)).append("\n");
    sb.append("    securityMode: ").append(toIndentedString(securityMode)).append("\n");
    sb.append("    securityGroupId: ").append(toIndentedString(securityGroupId)).append("\n");
    sb.append("    securityKeyServices: ").append(toIndentedString(securityKeyServices)).append("\n");
    sb.append("    dataSetReaderProperties: ").append(toIndentedString(dataSetReaderProperties)).append("\n");
    sb.append("    transportSettings: ").append(toIndentedString(transportSettings)).append("\n");
    sb.append("    messageSettings: ").append(toIndentedString(messageSettings)).append("\n");
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


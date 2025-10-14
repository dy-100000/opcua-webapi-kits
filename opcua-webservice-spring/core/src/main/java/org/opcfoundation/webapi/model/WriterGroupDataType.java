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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.6/#6.2.6.7.1).
 */

@Schema(name = "WriterGroupDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.6/#6.2.6.7.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class WriterGroupDataType {

  private Integer writerGroupId = 0;

  private Double publishingInterval = 0d;

  private Double keepAliveTime = 0d;

  private Integer priority = 0;

  @Valid
  private List<String> localeIds = new ArrayList<>();

  private @Nullable String headerLayoutUri;

  private @Nullable Object transportSettings;

  private @Nullable Object messageSettings;

  @Valid
  private List<@Valid DataSetWriterDataType> dataSetWriters = new ArrayList<>();

  private @Nullable String name;

  private Boolean enabled = false;

  private @Nullable Integer securityMode;

  private @Nullable String securityGroupId;

  @Valid
  private List<@Valid EndpointDescription> securityKeyServices = new ArrayList<>();

  private Long maxNetworkMessageSize = 0l;

  @Valid
  private List<@Valid KeyValuePair> groupProperties = new ArrayList<>();

  public WriterGroupDataType writerGroupId(Integer writerGroupId) {
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

  public WriterGroupDataType publishingInterval(Double publishingInterval) {
    this.publishingInterval = publishingInterval;
    return this;
  }

  /**
   * Get publishingInterval
   * @return publishingInterval
   */
  
  @Schema(name = "PublishingInterval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PublishingInterval")
  public Double getPublishingInterval() {
    return publishingInterval;
  }

  public void setPublishingInterval(Double publishingInterval) {
    this.publishingInterval = publishingInterval;
  }

  public WriterGroupDataType keepAliveTime(Double keepAliveTime) {
    this.keepAliveTime = keepAliveTime;
    return this;
  }

  /**
   * Get keepAliveTime
   * @return keepAliveTime
   */
  
  @Schema(name = "KeepAliveTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("KeepAliveTime")
  public Double getKeepAliveTime() {
    return keepAliveTime;
  }

  public void setKeepAliveTime(Double keepAliveTime) {
    this.keepAliveTime = keepAliveTime;
  }

  public WriterGroupDataType priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * minimum: 0
   * maximum: 255
   * @return priority
   */
  @Min(0) @Max(255) 
  @Schema(name = "Priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Priority")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public WriterGroupDataType localeIds(List<String> localeIds) {
    this.localeIds = localeIds;
    return this;
  }

  public WriterGroupDataType addLocaleIdsItem(String localeIdsItem) {
    if (this.localeIds == null) {
      this.localeIds = new ArrayList<>();
    }
    this.localeIds.add(localeIdsItem);
    return this;
  }

  /**
   * Get localeIds
   * @return localeIds
   */
  
  @Schema(name = "LocaleIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("LocaleIds")
  public List<String> getLocaleIds() {
    return localeIds;
  }

  public void setLocaleIds(List<String> localeIds) {
    this.localeIds = localeIds;
  }

  public WriterGroupDataType headerLayoutUri(String headerLayoutUri) {
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

  public WriterGroupDataType transportSettings(Object transportSettings) {
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

  public WriterGroupDataType messageSettings(Object messageSettings) {
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

  public WriterGroupDataType dataSetWriters(List<@Valid DataSetWriterDataType> dataSetWriters) {
    this.dataSetWriters = dataSetWriters;
    return this;
  }

  public WriterGroupDataType addDataSetWritersItem(DataSetWriterDataType dataSetWritersItem) {
    if (this.dataSetWriters == null) {
      this.dataSetWriters = new ArrayList<>();
    }
    this.dataSetWriters.add(dataSetWritersItem);
    return this;
  }

  /**
   * Get dataSetWriters
   * @return dataSetWriters
   */
  @Valid 
  @Schema(name = "DataSetWriters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetWriters")
  public List<@Valid DataSetWriterDataType> getDataSetWriters() {
    return dataSetWriters;
  }

  public void setDataSetWriters(List<@Valid DataSetWriterDataType> dataSetWriters) {
    this.dataSetWriters = dataSetWriters;
  }

  public WriterGroupDataType name(String name) {
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

  public WriterGroupDataType enabled(Boolean enabled) {
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

  public WriterGroupDataType securityMode(Integer securityMode) {
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

  public WriterGroupDataType securityGroupId(String securityGroupId) {
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

  public WriterGroupDataType securityKeyServices(List<@Valid EndpointDescription> securityKeyServices) {
    this.securityKeyServices = securityKeyServices;
    return this;
  }

  public WriterGroupDataType addSecurityKeyServicesItem(EndpointDescription securityKeyServicesItem) {
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

  public WriterGroupDataType maxNetworkMessageSize(Long maxNetworkMessageSize) {
    this.maxNetworkMessageSize = maxNetworkMessageSize;
    return this;
  }

  /**
   * Get maxNetworkMessageSize
   * minimum: 0
   * maximum: 4294967295
   * @return maxNetworkMessageSize
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MaxNetworkMessageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MaxNetworkMessageSize")
  public Long getMaxNetworkMessageSize() {
    return maxNetworkMessageSize;
  }

  public void setMaxNetworkMessageSize(Long maxNetworkMessageSize) {
    this.maxNetworkMessageSize = maxNetworkMessageSize;
  }

  public WriterGroupDataType groupProperties(List<@Valid KeyValuePair> groupProperties) {
    this.groupProperties = groupProperties;
    return this;
  }

  public WriterGroupDataType addGroupPropertiesItem(KeyValuePair groupPropertiesItem) {
    if (this.groupProperties == null) {
      this.groupProperties = new ArrayList<>();
    }
    this.groupProperties.add(groupPropertiesItem);
    return this;
  }

  /**
   * Get groupProperties
   * @return groupProperties
   */
  @Valid 
  @Schema(name = "GroupProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("GroupProperties")
  public List<@Valid KeyValuePair> getGroupProperties() {
    return groupProperties;
  }

  public void setGroupProperties(List<@Valid KeyValuePair> groupProperties) {
    this.groupProperties = groupProperties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WriterGroupDataType writerGroupDataType = (WriterGroupDataType) o;
    return Objects.equals(this.writerGroupId, writerGroupDataType.writerGroupId) &&
        Objects.equals(this.publishingInterval, writerGroupDataType.publishingInterval) &&
        Objects.equals(this.keepAliveTime, writerGroupDataType.keepAliveTime) &&
        Objects.equals(this.priority, writerGroupDataType.priority) &&
        Objects.equals(this.localeIds, writerGroupDataType.localeIds) &&
        Objects.equals(this.headerLayoutUri, writerGroupDataType.headerLayoutUri) &&
        Objects.equals(this.transportSettings, writerGroupDataType.transportSettings) &&
        Objects.equals(this.messageSettings, writerGroupDataType.messageSettings) &&
        Objects.equals(this.dataSetWriters, writerGroupDataType.dataSetWriters) &&
        Objects.equals(this.name, writerGroupDataType.name) &&
        Objects.equals(this.enabled, writerGroupDataType.enabled) &&
        Objects.equals(this.securityMode, writerGroupDataType.securityMode) &&
        Objects.equals(this.securityGroupId, writerGroupDataType.securityGroupId) &&
        Objects.equals(this.securityKeyServices, writerGroupDataType.securityKeyServices) &&
        Objects.equals(this.maxNetworkMessageSize, writerGroupDataType.maxNetworkMessageSize) &&
        Objects.equals(this.groupProperties, writerGroupDataType.groupProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(writerGroupId, publishingInterval, keepAliveTime, priority, localeIds, headerLayoutUri, transportSettings, messageSettings, dataSetWriters, name, enabled, securityMode, securityGroupId, securityKeyServices, maxNetworkMessageSize, groupProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WriterGroupDataType {\n");
    sb.append("    writerGroupId: ").append(toIndentedString(writerGroupId)).append("\n");
    sb.append("    publishingInterval: ").append(toIndentedString(publishingInterval)).append("\n");
    sb.append("    keepAliveTime: ").append(toIndentedString(keepAliveTime)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    localeIds: ").append(toIndentedString(localeIds)).append("\n");
    sb.append("    headerLayoutUri: ").append(toIndentedString(headerLayoutUri)).append("\n");
    sb.append("    transportSettings: ").append(toIndentedString(transportSettings)).append("\n");
    sb.append("    messageSettings: ").append(toIndentedString(messageSettings)).append("\n");
    sb.append("    dataSetWriters: ").append(toIndentedString(dataSetWriters)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    securityMode: ").append(toIndentedString(securityMode)).append("\n");
    sb.append("    securityGroupId: ").append(toIndentedString(securityGroupId)).append("\n");
    sb.append("    securityKeyServices: ").append(toIndentedString(securityKeyServices)).append("\n");
    sb.append("    maxNetworkMessageSize: ").append(toIndentedString(maxNetworkMessageSize)).append("\n");
    sb.append("    groupProperties: ").append(toIndentedString(groupProperties)).append("\n");
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


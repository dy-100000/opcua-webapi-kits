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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.8/#6.2.8.2.1).
 */

@Schema(name = "ReaderGroupDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.8/#6.2.8.2.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ReaderGroupDataType {

  private @Nullable Object transportSettings;

  private @Nullable Object messageSettings;

  @Valid
  private List<@Valid DataSetReaderDataType> dataSetReaders = new ArrayList<>();

  private @Nullable String name;

  private Boolean enabled = false;

  private @Nullable Integer securityMode;

  private @Nullable String securityGroupId;

  @Valid
  private List<@Valid EndpointDescription> securityKeyServices = new ArrayList<>();

  private Long maxNetworkMessageSize = 0l;

  @Valid
  private List<@Valid KeyValuePair> groupProperties = new ArrayList<>();

  public ReaderGroupDataType transportSettings(Object transportSettings) {
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

  public ReaderGroupDataType messageSettings(Object messageSettings) {
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

  public ReaderGroupDataType dataSetReaders(List<@Valid DataSetReaderDataType> dataSetReaders) {
    this.dataSetReaders = dataSetReaders;
    return this;
  }

  public ReaderGroupDataType addDataSetReadersItem(DataSetReaderDataType dataSetReadersItem) {
    if (this.dataSetReaders == null) {
      this.dataSetReaders = new ArrayList<>();
    }
    this.dataSetReaders.add(dataSetReadersItem);
    return this;
  }

  /**
   * Get dataSetReaders
   * @return dataSetReaders
   */
  @Valid 
  @Schema(name = "DataSetReaders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetReaders")
  public List<@Valid DataSetReaderDataType> getDataSetReaders() {
    return dataSetReaders;
  }

  public void setDataSetReaders(List<@Valid DataSetReaderDataType> dataSetReaders) {
    this.dataSetReaders = dataSetReaders;
  }

  public ReaderGroupDataType name(String name) {
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

  public ReaderGroupDataType enabled(Boolean enabled) {
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

  public ReaderGroupDataType securityMode(Integer securityMode) {
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

  public ReaderGroupDataType securityGroupId(String securityGroupId) {
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

  public ReaderGroupDataType securityKeyServices(List<@Valid EndpointDescription> securityKeyServices) {
    this.securityKeyServices = securityKeyServices;
    return this;
  }

  public ReaderGroupDataType addSecurityKeyServicesItem(EndpointDescription securityKeyServicesItem) {
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

  public ReaderGroupDataType maxNetworkMessageSize(Long maxNetworkMessageSize) {
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

  public ReaderGroupDataType groupProperties(List<@Valid KeyValuePair> groupProperties) {
    this.groupProperties = groupProperties;
    return this;
  }

  public ReaderGroupDataType addGroupPropertiesItem(KeyValuePair groupPropertiesItem) {
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
    ReaderGroupDataType readerGroupDataType = (ReaderGroupDataType) o;
    return Objects.equals(this.transportSettings, readerGroupDataType.transportSettings) &&
        Objects.equals(this.messageSettings, readerGroupDataType.messageSettings) &&
        Objects.equals(this.dataSetReaders, readerGroupDataType.dataSetReaders) &&
        Objects.equals(this.name, readerGroupDataType.name) &&
        Objects.equals(this.enabled, readerGroupDataType.enabled) &&
        Objects.equals(this.securityMode, readerGroupDataType.securityMode) &&
        Objects.equals(this.securityGroupId, readerGroupDataType.securityGroupId) &&
        Objects.equals(this.securityKeyServices, readerGroupDataType.securityKeyServices) &&
        Objects.equals(this.maxNetworkMessageSize, readerGroupDataType.maxNetworkMessageSize) &&
        Objects.equals(this.groupProperties, readerGroupDataType.groupProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transportSettings, messageSettings, dataSetReaders, name, enabled, securityMode, securityGroupId, securityKeyServices, maxNetworkMessageSize, groupProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReaderGroupDataType {\n");
    sb.append("    transportSettings: ").append(toIndentedString(transportSettings)).append("\n");
    sb.append("    messageSettings: ").append(toIndentedString(messageSettings)).append("\n");
    sb.append("    dataSetReaders: ").append(toIndentedString(dataSetReaders)).append("\n");
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


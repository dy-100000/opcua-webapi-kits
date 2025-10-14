package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.4).
 */

@Schema(name = "PubSubConfiguration2DataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PubSubConfiguration2DataType {

  @Valid
  private List<@Valid StandaloneSubscribedDataSetDataType> subscribedDataSets = new ArrayList<>();

  @Valid
  private List<@Valid DataSetMetaDataType> dataSetClasses = new ArrayList<>();

  @Valid
  private List<@Valid EndpointDescription> defaultSecurityKeyServices = new ArrayList<>();

  @Valid
  private List<@Valid SecurityGroupDataType> securityGroups = new ArrayList<>();

  @Valid
  private List<@Valid PubSubKeyPushTargetDataType> pubSubKeyPushTargets = new ArrayList<>();

  private Long configurationVersion = 0l;

  @Valid
  private List<@Valid KeyValuePair> configurationProperties = new ArrayList<>();

  @Valid
  private List<@Valid PublishedDataSetDataType> publishedDataSets = new ArrayList<>();

  @Valid
  private List<@Valid PubSubConnectionDataType> connections = new ArrayList<>();

  private Boolean enabled = false;

  public PubSubConfiguration2DataType subscribedDataSets(List<@Valid StandaloneSubscribedDataSetDataType> subscribedDataSets) {
    this.subscribedDataSets = subscribedDataSets;
    return this;
  }

  public PubSubConfiguration2DataType addSubscribedDataSetsItem(StandaloneSubscribedDataSetDataType subscribedDataSetsItem) {
    if (this.subscribedDataSets == null) {
      this.subscribedDataSets = new ArrayList<>();
    }
    this.subscribedDataSets.add(subscribedDataSetsItem);
    return this;
  }

  /**
   * Get subscribedDataSets
   * @return subscribedDataSets
   */
  @Valid 
  @Schema(name = "SubscribedDataSets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SubscribedDataSets")
  public List<@Valid StandaloneSubscribedDataSetDataType> getSubscribedDataSets() {
    return subscribedDataSets;
  }

  public void setSubscribedDataSets(List<@Valid StandaloneSubscribedDataSetDataType> subscribedDataSets) {
    this.subscribedDataSets = subscribedDataSets;
  }

  public PubSubConfiguration2DataType dataSetClasses(List<@Valid DataSetMetaDataType> dataSetClasses) {
    this.dataSetClasses = dataSetClasses;
    return this;
  }

  public PubSubConfiguration2DataType addDataSetClassesItem(DataSetMetaDataType dataSetClassesItem) {
    if (this.dataSetClasses == null) {
      this.dataSetClasses = new ArrayList<>();
    }
    this.dataSetClasses.add(dataSetClassesItem);
    return this;
  }

  /**
   * Get dataSetClasses
   * @return dataSetClasses
   */
  @Valid 
  @Schema(name = "DataSetClasses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DataSetClasses")
  public List<@Valid DataSetMetaDataType> getDataSetClasses() {
    return dataSetClasses;
  }

  public void setDataSetClasses(List<@Valid DataSetMetaDataType> dataSetClasses) {
    this.dataSetClasses = dataSetClasses;
  }

  public PubSubConfiguration2DataType defaultSecurityKeyServices(List<@Valid EndpointDescription> defaultSecurityKeyServices) {
    this.defaultSecurityKeyServices = defaultSecurityKeyServices;
    return this;
  }

  public PubSubConfiguration2DataType addDefaultSecurityKeyServicesItem(EndpointDescription defaultSecurityKeyServicesItem) {
    if (this.defaultSecurityKeyServices == null) {
      this.defaultSecurityKeyServices = new ArrayList<>();
    }
    this.defaultSecurityKeyServices.add(defaultSecurityKeyServicesItem);
    return this;
  }

  /**
   * Get defaultSecurityKeyServices
   * @return defaultSecurityKeyServices
   */
  @Valid 
  @Schema(name = "DefaultSecurityKeyServices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DefaultSecurityKeyServices")
  public List<@Valid EndpointDescription> getDefaultSecurityKeyServices() {
    return defaultSecurityKeyServices;
  }

  public void setDefaultSecurityKeyServices(List<@Valid EndpointDescription> defaultSecurityKeyServices) {
    this.defaultSecurityKeyServices = defaultSecurityKeyServices;
  }

  public PubSubConfiguration2DataType securityGroups(List<@Valid SecurityGroupDataType> securityGroups) {
    this.securityGroups = securityGroups;
    return this;
  }

  public PubSubConfiguration2DataType addSecurityGroupsItem(SecurityGroupDataType securityGroupsItem) {
    if (this.securityGroups == null) {
      this.securityGroups = new ArrayList<>();
    }
    this.securityGroups.add(securityGroupsItem);
    return this;
  }

  /**
   * Get securityGroups
   * @return securityGroups
   */
  @Valid 
  @Schema(name = "SecurityGroups", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SecurityGroups")
  public List<@Valid SecurityGroupDataType> getSecurityGroups() {
    return securityGroups;
  }

  public void setSecurityGroups(List<@Valid SecurityGroupDataType> securityGroups) {
    this.securityGroups = securityGroups;
  }

  public PubSubConfiguration2DataType pubSubKeyPushTargets(List<@Valid PubSubKeyPushTargetDataType> pubSubKeyPushTargets) {
    this.pubSubKeyPushTargets = pubSubKeyPushTargets;
    return this;
  }

  public PubSubConfiguration2DataType addPubSubKeyPushTargetsItem(PubSubKeyPushTargetDataType pubSubKeyPushTargetsItem) {
    if (this.pubSubKeyPushTargets == null) {
      this.pubSubKeyPushTargets = new ArrayList<>();
    }
    this.pubSubKeyPushTargets.add(pubSubKeyPushTargetsItem);
    return this;
  }

  /**
   * Get pubSubKeyPushTargets
   * @return pubSubKeyPushTargets
   */
  @Valid 
  @Schema(name = "PubSubKeyPushTargets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PubSubKeyPushTargets")
  public List<@Valid PubSubKeyPushTargetDataType> getPubSubKeyPushTargets() {
    return pubSubKeyPushTargets;
  }

  public void setPubSubKeyPushTargets(List<@Valid PubSubKeyPushTargetDataType> pubSubKeyPushTargets) {
    this.pubSubKeyPushTargets = pubSubKeyPushTargets;
  }

  public PubSubConfiguration2DataType configurationVersion(Long configurationVersion) {
    this.configurationVersion = configurationVersion;
    return this;
  }

  /**
   * Get configurationVersion
   * minimum: 0
   * maximum: 4294967295
   * @return configurationVersion
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ConfigurationVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ConfigurationVersion")
  public Long getConfigurationVersion() {
    return configurationVersion;
  }

  public void setConfigurationVersion(Long configurationVersion) {
    this.configurationVersion = configurationVersion;
  }

  public PubSubConfiguration2DataType configurationProperties(List<@Valid KeyValuePair> configurationProperties) {
    this.configurationProperties = configurationProperties;
    return this;
  }

  public PubSubConfiguration2DataType addConfigurationPropertiesItem(KeyValuePair configurationPropertiesItem) {
    if (this.configurationProperties == null) {
      this.configurationProperties = new ArrayList<>();
    }
    this.configurationProperties.add(configurationPropertiesItem);
    return this;
  }

  /**
   * Get configurationProperties
   * @return configurationProperties
   */
  @Valid 
  @Schema(name = "ConfigurationProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ConfigurationProperties")
  public List<@Valid KeyValuePair> getConfigurationProperties() {
    return configurationProperties;
  }

  public void setConfigurationProperties(List<@Valid KeyValuePair> configurationProperties) {
    this.configurationProperties = configurationProperties;
  }

  public PubSubConfiguration2DataType publishedDataSets(List<@Valid PublishedDataSetDataType> publishedDataSets) {
    this.publishedDataSets = publishedDataSets;
    return this;
  }

  public PubSubConfiguration2DataType addPublishedDataSetsItem(PublishedDataSetDataType publishedDataSetsItem) {
    if (this.publishedDataSets == null) {
      this.publishedDataSets = new ArrayList<>();
    }
    this.publishedDataSets.add(publishedDataSetsItem);
    return this;
  }

  /**
   * Get publishedDataSets
   * @return publishedDataSets
   */
  @Valid 
  @Schema(name = "PublishedDataSets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("PublishedDataSets")
  public List<@Valid PublishedDataSetDataType> getPublishedDataSets() {
    return publishedDataSets;
  }

  public void setPublishedDataSets(List<@Valid PublishedDataSetDataType> publishedDataSets) {
    this.publishedDataSets = publishedDataSets;
  }

  public PubSubConfiguration2DataType connections(List<@Valid PubSubConnectionDataType> connections) {
    this.connections = connections;
    return this;
  }

  public PubSubConfiguration2DataType addConnectionsItem(PubSubConnectionDataType connectionsItem) {
    if (this.connections == null) {
      this.connections = new ArrayList<>();
    }
    this.connections.add(connectionsItem);
    return this;
  }

  /**
   * Get connections
   * @return connections
   */
  @Valid 
  @Schema(name = "Connections", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Connections")
  public List<@Valid PubSubConnectionDataType> getConnections() {
    return connections;
  }

  public void setConnections(List<@Valid PubSubConnectionDataType> connections) {
    this.connections = connections;
  }

  public PubSubConfiguration2DataType enabled(Boolean enabled) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PubSubConfiguration2DataType pubSubConfiguration2DataType = (PubSubConfiguration2DataType) o;
    return Objects.equals(this.subscribedDataSets, pubSubConfiguration2DataType.subscribedDataSets) &&
        Objects.equals(this.dataSetClasses, pubSubConfiguration2DataType.dataSetClasses) &&
        Objects.equals(this.defaultSecurityKeyServices, pubSubConfiguration2DataType.defaultSecurityKeyServices) &&
        Objects.equals(this.securityGroups, pubSubConfiguration2DataType.securityGroups) &&
        Objects.equals(this.pubSubKeyPushTargets, pubSubConfiguration2DataType.pubSubKeyPushTargets) &&
        Objects.equals(this.configurationVersion, pubSubConfiguration2DataType.configurationVersion) &&
        Objects.equals(this.configurationProperties, pubSubConfiguration2DataType.configurationProperties) &&
        Objects.equals(this.publishedDataSets, pubSubConfiguration2DataType.publishedDataSets) &&
        Objects.equals(this.connections, pubSubConfiguration2DataType.connections) &&
        Objects.equals(this.enabled, pubSubConfiguration2DataType.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscribedDataSets, dataSetClasses, defaultSecurityKeyServices, securityGroups, pubSubKeyPushTargets, configurationVersion, configurationProperties, publishedDataSets, connections, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PubSubConfiguration2DataType {\n");
    sb.append("    subscribedDataSets: ").append(toIndentedString(subscribedDataSets)).append("\n");
    sb.append("    dataSetClasses: ").append(toIndentedString(dataSetClasses)).append("\n");
    sb.append("    defaultSecurityKeyServices: ").append(toIndentedString(defaultSecurityKeyServices)).append("\n");
    sb.append("    securityGroups: ").append(toIndentedString(securityGroups)).append("\n");
    sb.append("    pubSubKeyPushTargets: ").append(toIndentedString(pubSubKeyPushTargets)).append("\n");
    sb.append("    configurationVersion: ").append(toIndentedString(configurationVersion)).append("\n");
    sb.append("    configurationProperties: ").append(toIndentedString(configurationProperties)).append("\n");
    sb.append("    publishedDataSets: ").append(toIndentedString(publishedDataSets)).append("\n");
    sb.append("    connections: ").append(toIndentedString(connections)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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


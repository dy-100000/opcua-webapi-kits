package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.1).
 */

@Schema(name = "PubSubConfigurationDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PubSubConfigurationDataType {

  @Valid
  private List<@Valid PublishedDataSetDataType> publishedDataSets = new ArrayList<>();

  @Valid
  private List<@Valid PubSubConnectionDataType> connections = new ArrayList<>();

  private Boolean enabled = false;

  public PubSubConfigurationDataType publishedDataSets(List<@Valid PublishedDataSetDataType> publishedDataSets) {
    this.publishedDataSets = publishedDataSets;
    return this;
  }

  public PubSubConfigurationDataType addPublishedDataSetsItem(PublishedDataSetDataType publishedDataSetsItem) {
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

  public PubSubConfigurationDataType connections(List<@Valid PubSubConnectionDataType> connections) {
    this.connections = connections;
    return this;
  }

  public PubSubConfigurationDataType addConnectionsItem(PubSubConnectionDataType connectionsItem) {
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

  public PubSubConfigurationDataType enabled(Boolean enabled) {
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
    PubSubConfigurationDataType pubSubConfigurationDataType = (PubSubConfigurationDataType) o;
    return Objects.equals(this.publishedDataSets, pubSubConfigurationDataType.publishedDataSets) &&
        Objects.equals(this.connections, pubSubConfigurationDataType.connections) &&
        Objects.equals(this.enabled, pubSubConfigurationDataType.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publishedDataSets, connections, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PubSubConfigurationDataType {\n");
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


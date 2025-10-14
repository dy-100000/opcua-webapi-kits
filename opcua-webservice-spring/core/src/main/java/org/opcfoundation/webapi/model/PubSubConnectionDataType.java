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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.1).
 */

@Schema(name = "PubSubConnectionDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.1).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class PubSubConnectionDataType {

  private @Nullable String name;

  private Boolean enabled = false;

  private @Nullable Variant publisherId;

  private @Nullable String transportProfileUri;

  private @Nullable Object address;

  @Valid
  private List<@Valid KeyValuePair> connectionProperties = new ArrayList<>();

  private @Nullable Object transportSettings;

  @Valid
  private List<@Valid WriterGroupDataType> writerGroups = new ArrayList<>();

  @Valid
  private List<@Valid ReaderGroupDataType> readerGroups = new ArrayList<>();

  public PubSubConnectionDataType name(String name) {
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

  public PubSubConnectionDataType enabled(Boolean enabled) {
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

  public PubSubConnectionDataType publisherId(Variant publisherId) {
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

  public PubSubConnectionDataType transportProfileUri(String transportProfileUri) {
    this.transportProfileUri = transportProfileUri;
    return this;
  }

  /**
   * Get transportProfileUri
   * @return transportProfileUri
   */
  
  @Schema(name = "TransportProfileUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("TransportProfileUri")
  public String getTransportProfileUri() {
    return transportProfileUri;
  }

  public void setTransportProfileUri(String transportProfileUri) {
    this.transportProfileUri = transportProfileUri;
  }

  public PubSubConnectionDataType address(Object address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   * @return address
   */
  
  @Schema(name = "Address", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Address")
  public Object getAddress() {
    return address;
  }

  public void setAddress(Object address) {
    this.address = address;
  }

  public PubSubConnectionDataType connectionProperties(List<@Valid KeyValuePair> connectionProperties) {
    this.connectionProperties = connectionProperties;
    return this;
  }

  public PubSubConnectionDataType addConnectionPropertiesItem(KeyValuePair connectionPropertiesItem) {
    if (this.connectionProperties == null) {
      this.connectionProperties = new ArrayList<>();
    }
    this.connectionProperties.add(connectionPropertiesItem);
    return this;
  }

  /**
   * Get connectionProperties
   * @return connectionProperties
   */
  @Valid 
  @Schema(name = "ConnectionProperties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ConnectionProperties")
  public List<@Valid KeyValuePair> getConnectionProperties() {
    return connectionProperties;
  }

  public void setConnectionProperties(List<@Valid KeyValuePair> connectionProperties) {
    this.connectionProperties = connectionProperties;
  }

  public PubSubConnectionDataType transportSettings(Object transportSettings) {
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

  public PubSubConnectionDataType writerGroups(List<@Valid WriterGroupDataType> writerGroups) {
    this.writerGroups = writerGroups;
    return this;
  }

  public PubSubConnectionDataType addWriterGroupsItem(WriterGroupDataType writerGroupsItem) {
    if (this.writerGroups == null) {
      this.writerGroups = new ArrayList<>();
    }
    this.writerGroups.add(writerGroupsItem);
    return this;
  }

  /**
   * Get writerGroups
   * @return writerGroups
   */
  @Valid 
  @Schema(name = "WriterGroups", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("WriterGroups")
  public List<@Valid WriterGroupDataType> getWriterGroups() {
    return writerGroups;
  }

  public void setWriterGroups(List<@Valid WriterGroupDataType> writerGroups) {
    this.writerGroups = writerGroups;
  }

  public PubSubConnectionDataType readerGroups(List<@Valid ReaderGroupDataType> readerGroups) {
    this.readerGroups = readerGroups;
    return this;
  }

  public PubSubConnectionDataType addReaderGroupsItem(ReaderGroupDataType readerGroupsItem) {
    if (this.readerGroups == null) {
      this.readerGroups = new ArrayList<>();
    }
    this.readerGroups.add(readerGroupsItem);
    return this;
  }

  /**
   * Get readerGroups
   * @return readerGroups
   */
  @Valid 
  @Schema(name = "ReaderGroups", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ReaderGroups")
  public List<@Valid ReaderGroupDataType> getReaderGroups() {
    return readerGroups;
  }

  public void setReaderGroups(List<@Valid ReaderGroupDataType> readerGroups) {
    this.readerGroups = readerGroups;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PubSubConnectionDataType pubSubConnectionDataType = (PubSubConnectionDataType) o;
    return Objects.equals(this.name, pubSubConnectionDataType.name) &&
        Objects.equals(this.enabled, pubSubConnectionDataType.enabled) &&
        Objects.equals(this.publisherId, pubSubConnectionDataType.publisherId) &&
        Objects.equals(this.transportProfileUri, pubSubConnectionDataType.transportProfileUri) &&
        Objects.equals(this.address, pubSubConnectionDataType.address) &&
        Objects.equals(this.connectionProperties, pubSubConnectionDataType.connectionProperties) &&
        Objects.equals(this.transportSettings, pubSubConnectionDataType.transportSettings) &&
        Objects.equals(this.writerGroups, pubSubConnectionDataType.writerGroups) &&
        Objects.equals(this.readerGroups, pubSubConnectionDataType.readerGroups);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, enabled, publisherId, transportProfileUri, address, connectionProperties, transportSettings, writerGroups, readerGroups);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PubSubConnectionDataType {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    publisherId: ").append(toIndentedString(publisherId)).append("\n");
    sb.append("    transportProfileUri: ").append(toIndentedString(transportProfileUri)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    connectionProperties: ").append(toIndentedString(connectionProperties)).append("\n");
    sb.append("    transportSettings: ").append(toIndentedString(transportSettings)).append("\n");
    sb.append("    writerGroups: ").append(toIndentedString(writerGroups)).append("\n");
    sb.append("    readerGroups: ").append(toIndentedString(readerGroups)).append("\n");
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


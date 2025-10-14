package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.5.7).
 */

@Schema(name = "BrokerDataSetWriterTransportDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.5.7).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrokerDataSetWriterTransportDataType {

  private @Nullable String queueName;

  private @Nullable String resourceUri;

  private @Nullable String authenticationProfileUri;

  private @Nullable Integer requestedDeliveryGuarantee;

  private @Nullable String metaDataQueueName;

  private Double metaDataUpdateTime = 0d;

  public BrokerDataSetWriterTransportDataType queueName(String queueName) {
    this.queueName = queueName;
    return this;
  }

  /**
   * Get queueName
   * @return queueName
   */
  
  @Schema(name = "QueueName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("QueueName")
  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public BrokerDataSetWriterTransportDataType resourceUri(String resourceUri) {
    this.resourceUri = resourceUri;
    return this;
  }

  /**
   * Get resourceUri
   * @return resourceUri
   */
  
  @Schema(name = "ResourceUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ResourceUri")
  public String getResourceUri() {
    return resourceUri;
  }

  public void setResourceUri(String resourceUri) {
    this.resourceUri = resourceUri;
  }

  public BrokerDataSetWriterTransportDataType authenticationProfileUri(String authenticationProfileUri) {
    this.authenticationProfileUri = authenticationProfileUri;
    return this;
  }

  /**
   * Get authenticationProfileUri
   * @return authenticationProfileUri
   */
  
  @Schema(name = "AuthenticationProfileUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AuthenticationProfileUri")
  public String getAuthenticationProfileUri() {
    return authenticationProfileUri;
  }

  public void setAuthenticationProfileUri(String authenticationProfileUri) {
    this.authenticationProfileUri = authenticationProfileUri;
  }

  public BrokerDataSetWriterTransportDataType requestedDeliveryGuarantee(Integer requestedDeliveryGuarantee) {
    this.requestedDeliveryGuarantee = requestedDeliveryGuarantee;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1).
   * @return requestedDeliveryGuarantee
   */
  
  @Schema(name = "RequestedDeliveryGuarantee", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.1).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RequestedDeliveryGuarantee")
  public Integer getRequestedDeliveryGuarantee() {
    return requestedDeliveryGuarantee;
  }

  public void setRequestedDeliveryGuarantee(Integer requestedDeliveryGuarantee) {
    this.requestedDeliveryGuarantee = requestedDeliveryGuarantee;
  }

  public BrokerDataSetWriterTransportDataType metaDataQueueName(String metaDataQueueName) {
    this.metaDataQueueName = metaDataQueueName;
    return this;
  }

  /**
   * Get metaDataQueueName
   * @return metaDataQueueName
   */
  
  @Schema(name = "MetaDataQueueName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MetaDataQueueName")
  public String getMetaDataQueueName() {
    return metaDataQueueName;
  }

  public void setMetaDataQueueName(String metaDataQueueName) {
    this.metaDataQueueName = metaDataQueueName;
  }

  public BrokerDataSetWriterTransportDataType metaDataUpdateTime(Double metaDataUpdateTime) {
    this.metaDataUpdateTime = metaDataUpdateTime;
    return this;
  }

  /**
   * Get metaDataUpdateTime
   * @return metaDataUpdateTime
   */
  
  @Schema(name = "MetaDataUpdateTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MetaDataUpdateTime")
  public Double getMetaDataUpdateTime() {
    return metaDataUpdateTime;
  }

  public void setMetaDataUpdateTime(Double metaDataUpdateTime) {
    this.metaDataUpdateTime = metaDataUpdateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrokerDataSetWriterTransportDataType brokerDataSetWriterTransportDataType = (BrokerDataSetWriterTransportDataType) o;
    return Objects.equals(this.queueName, brokerDataSetWriterTransportDataType.queueName) &&
        Objects.equals(this.resourceUri, brokerDataSetWriterTransportDataType.resourceUri) &&
        Objects.equals(this.authenticationProfileUri, brokerDataSetWriterTransportDataType.authenticationProfileUri) &&
        Objects.equals(this.requestedDeliveryGuarantee, brokerDataSetWriterTransportDataType.requestedDeliveryGuarantee) &&
        Objects.equals(this.metaDataQueueName, brokerDataSetWriterTransportDataType.metaDataQueueName) &&
        Objects.equals(this.metaDataUpdateTime, brokerDataSetWriterTransportDataType.metaDataUpdateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queueName, resourceUri, authenticationProfileUri, requestedDeliveryGuarantee, metaDataQueueName, metaDataUpdateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrokerDataSetWriterTransportDataType {\n");
    sb.append("    queueName: ").append(toIndentedString(queueName)).append("\n");
    sb.append("    resourceUri: ").append(toIndentedString(resourceUri)).append("\n");
    sb.append("    authenticationProfileUri: ").append(toIndentedString(authenticationProfileUri)).append("\n");
    sb.append("    requestedDeliveryGuarantee: ").append(toIndentedString(requestedDeliveryGuarantee)).append("\n");
    sb.append("    metaDataQueueName: ").append(toIndentedString(metaDataQueueName)).append("\n");
    sb.append("    metaDataUpdateTime: ").append(toIndentedString(metaDataUpdateTime)).append("\n");
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


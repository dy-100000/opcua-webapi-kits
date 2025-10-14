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
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/7.2.4/#7.2.4.6.5).
 */

@Schema(name = "ApplicationDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/7.2.4/#7.2.4.6.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ApplicationDescription {

  private @Nullable String applicationUri;

  private @Nullable String productUri;

  private @Nullable LocalizedText applicationName;

  private @Nullable Integer applicationType;

  private @Nullable String gatewayServerUri;

  private @Nullable String discoveryProfileUri;

  @Valid
  private List<String> discoveryUrls = new ArrayList<>();

  public ApplicationDescription applicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
    return this;
  }

  /**
   * Get applicationUri
   * @return applicationUri
   */
  
  @Schema(name = "ApplicationUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ApplicationUri")
  public String getApplicationUri() {
    return applicationUri;
  }

  public void setApplicationUri(String applicationUri) {
    this.applicationUri = applicationUri;
  }

  public ApplicationDescription productUri(String productUri) {
    this.productUri = productUri;
    return this;
  }

  /**
   * Get productUri
   * @return productUri
   */
  
  @Schema(name = "ProductUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ProductUri")
  public String getProductUri() {
    return productUri;
  }

  public void setProductUri(String productUri) {
    this.productUri = productUri;
  }

  public ApplicationDescription applicationName(LocalizedText applicationName) {
    this.applicationName = applicationName;
    return this;
  }

  /**
   * Get applicationName
   * @return applicationName
   */
  @Valid 
  @Schema(name = "ApplicationName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ApplicationName")
  public LocalizedText getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(LocalizedText applicationName) {
    this.applicationName = applicationName;
  }

  public ApplicationDescription applicationType(Integer applicationType) {
    this.applicationType = applicationType;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.4).
   * @return applicationType
   */
  
  @Schema(name = "ApplicationType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.4).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ApplicationType")
  public Integer getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(Integer applicationType) {
    this.applicationType = applicationType;
  }

  public ApplicationDescription gatewayServerUri(String gatewayServerUri) {
    this.gatewayServerUri = gatewayServerUri;
    return this;
  }

  /**
   * Get gatewayServerUri
   * @return gatewayServerUri
   */
  
  @Schema(name = "GatewayServerUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("GatewayServerUri")
  public String getGatewayServerUri() {
    return gatewayServerUri;
  }

  public void setGatewayServerUri(String gatewayServerUri) {
    this.gatewayServerUri = gatewayServerUri;
  }

  public ApplicationDescription discoveryProfileUri(String discoveryProfileUri) {
    this.discoveryProfileUri = discoveryProfileUri;
    return this;
  }

  /**
   * Get discoveryProfileUri
   * @return discoveryProfileUri
   */
  
  @Schema(name = "DiscoveryProfileUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiscoveryProfileUri")
  public String getDiscoveryProfileUri() {
    return discoveryProfileUri;
  }

  public void setDiscoveryProfileUri(String discoveryProfileUri) {
    this.discoveryProfileUri = discoveryProfileUri;
  }

  public ApplicationDescription discoveryUrls(List<String> discoveryUrls) {
    this.discoveryUrls = discoveryUrls;
    return this;
  }

  public ApplicationDescription addDiscoveryUrlsItem(String discoveryUrlsItem) {
    if (this.discoveryUrls == null) {
      this.discoveryUrls = new ArrayList<>();
    }
    this.discoveryUrls.add(discoveryUrlsItem);
    return this;
  }

  /**
   * Get discoveryUrls
   * @return discoveryUrls
   */
  
  @Schema(name = "DiscoveryUrls", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiscoveryUrls")
  public List<String> getDiscoveryUrls() {
    return discoveryUrls;
  }

  public void setDiscoveryUrls(List<String> discoveryUrls) {
    this.discoveryUrls = discoveryUrls;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationDescription applicationDescription = (ApplicationDescription) o;
    return Objects.equals(this.applicationUri, applicationDescription.applicationUri) &&
        Objects.equals(this.productUri, applicationDescription.productUri) &&
        Objects.equals(this.applicationName, applicationDescription.applicationName) &&
        Objects.equals(this.applicationType, applicationDescription.applicationType) &&
        Objects.equals(this.gatewayServerUri, applicationDescription.gatewayServerUri) &&
        Objects.equals(this.discoveryProfileUri, applicationDescription.discoveryProfileUri) &&
        Objects.equals(this.discoveryUrls, applicationDescription.discoveryUrls);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationUri, productUri, applicationName, applicationType, gatewayServerUri, discoveryProfileUri, discoveryUrls);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationDescription {\n");
    sb.append("    applicationUri: ").append(toIndentedString(applicationUri)).append("\n");
    sb.append("    productUri: ").append(toIndentedString(productUri)).append("\n");
    sb.append("    applicationName: ").append(toIndentedString(applicationName)).append("\n");
    sb.append("    applicationType: ").append(toIndentedString(applicationType)).append("\n");
    sb.append("    gatewayServerUri: ").append(toIndentedString(gatewayServerUri)).append("\n");
    sb.append("    discoveryProfileUri: ").append(toIndentedString(discoveryProfileUri)).append("\n");
    sb.append("    discoveryUrls: ").append(toIndentedString(discoveryUrls)).append("\n");
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


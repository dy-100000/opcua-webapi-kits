package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.2.3).
 */

@Schema(name = "BrokerConnectionTransportDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.2.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class BrokerConnectionTransportDataType {

  private @Nullable String resourceUri;

  private @Nullable String authenticationProfileUri;

  public BrokerConnectionTransportDataType resourceUri(String resourceUri) {
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

  public BrokerConnectionTransportDataType authenticationProfileUri(String authenticationProfileUri) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrokerConnectionTransportDataType brokerConnectionTransportDataType = (BrokerConnectionTransportDataType) o;
    return Objects.equals(this.resourceUri, brokerConnectionTransportDataType.resourceUri) &&
        Objects.equals(this.authenticationProfileUri, brokerConnectionTransportDataType.authenticationProfileUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceUri, authenticationProfileUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrokerConnectionTransportDataType {\n");
    sb.append("    resourceUri: ").append(toIndentedString(resourceUri)).append("\n");
    sb.append("    authenticationProfileUri: ").append(toIndentedString(authenticationProfileUri)).append("\n");
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


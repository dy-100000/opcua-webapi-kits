package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.3).
 */

@Schema(name = "NetworkAddressDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class NetworkAddressDataType {

  private @Nullable String networkInterface;

  public NetworkAddressDataType networkInterface(String networkInterface) {
    this.networkInterface = networkInterface;
    return this;
  }

  /**
   * Get networkInterface
   * @return networkInterface
   */
  
  @Schema(name = "NetworkInterface", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NetworkInterface")
  public String getNetworkInterface() {
    return networkInterface;
  }

  public void setNetworkInterface(String networkInterface) {
    this.networkInterface = networkInterface;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NetworkAddressDataType networkAddressDataType = (NetworkAddressDataType) o;
    return Objects.equals(this.networkInterface, networkAddressDataType.networkInterface);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkInterface);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NetworkAddressDataType {\n");
    sb.append("    networkInterface: ").append(toIndentedString(networkInterface)).append("\n");
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


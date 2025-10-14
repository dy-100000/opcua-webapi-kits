package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.6).
 */

@Schema(name = "ConfigurationVersionDataType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.6).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ConfigurationVersionDataType {

  private Long majorVersion = 0l;

  private Long minorVersion = 0l;

  public ConfigurationVersionDataType majorVersion(Long majorVersion) {
    this.majorVersion = majorVersion;
    return this;
  }

  /**
   * Get majorVersion
   * minimum: 0
   * maximum: 4294967295
   * @return majorVersion
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MajorVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MajorVersion")
  public Long getMajorVersion() {
    return majorVersion;
  }

  public void setMajorVersion(Long majorVersion) {
    this.majorVersion = majorVersion;
  }

  public ConfigurationVersionDataType minorVersion(Long minorVersion) {
    this.minorVersion = minorVersion;
    return this;
  }

  /**
   * Get minorVersion
   * minimum: 0
   * maximum: 4294967295
   * @return minorVersion
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "MinorVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MinorVersion")
  public Long getMinorVersion() {
    return minorVersion;
  }

  public void setMinorVersion(Long minorVersion) {
    this.minorVersion = minorVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurationVersionDataType configurationVersionDataType = (ConfigurationVersionDataType) o;
    return Objects.equals(this.majorVersion, configurationVersionDataType.majorVersion) &&
        Objects.equals(this.minorVersion, configurationVersionDataType.minorVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(majorVersion, minorVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurationVersionDataType {\n");
    sb.append("    majorVersion: ").append(toIndentedString(majorVersion)).append("\n");
    sb.append("    minorVersion: ").append(toIndentedString(minorVersion)).append("\n");
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


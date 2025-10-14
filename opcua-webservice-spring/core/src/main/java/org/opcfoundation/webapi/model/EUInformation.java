package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.3/#5.6.3.3).
 */

@Schema(name = "EUInformation", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part8/5.6.3/#5.6.3.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EUInformation {

  private @Nullable String namespaceUri;

  private Integer unitId = 0;

  private @Nullable LocalizedText displayName;

  private @Nullable LocalizedText description;

  public EUInformation namespaceUri(String namespaceUri) {
    this.namespaceUri = namespaceUri;
    return this;
  }

  /**
   * Get namespaceUri
   * @return namespaceUri
   */
  
  @Schema(name = "NamespaceUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NamespaceUri")
  public String getNamespaceUri() {
    return namespaceUri;
  }

  public void setNamespaceUri(String namespaceUri) {
    this.namespaceUri = namespaceUri;
  }

  public EUInformation unitId(Integer unitId) {
    this.unitId = unitId;
    return this;
  }

  /**
   * Get unitId
   * @return unitId
   */
  
  @Schema(name = "UnitId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UnitId")
  public Integer getUnitId() {
    return unitId;
  }

  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  public EUInformation displayName(LocalizedText displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  @Valid 
  @Schema(name = "DisplayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DisplayName")
  public LocalizedText getDisplayName() {
    return displayName;
  }

  public void setDisplayName(LocalizedText displayName) {
    this.displayName = displayName;
  }

  public EUInformation description(LocalizedText description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @Valid 
  @Schema(name = "Description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Description")
  public LocalizedText getDescription() {
    return description;
  }

  public void setDescription(LocalizedText description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EUInformation euInformation = (EUInformation) o;
    return Objects.equals(this.namespaceUri, euInformation.namespaceUri) &&
        Objects.equals(this.unitId, euInformation.unitId) &&
        Objects.equals(this.displayName, euInformation.displayName) &&
        Objects.equals(this.description, euInformation.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(namespaceUri, unitId, displayName, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EUInformation {\n");
    sb.append("    namespaceUri: ").append(toIndentedString(namespaceUri)).append("\n");
    sb.append("    unitId: ").append(toIndentedString(unitId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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


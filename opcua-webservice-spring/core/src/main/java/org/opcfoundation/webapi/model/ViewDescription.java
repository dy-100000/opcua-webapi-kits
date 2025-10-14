package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.45).
 */

@Schema(name = "ViewDescription", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.45).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ViewDescription {

  private @Nullable String viewId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private Long viewVersion = 0l;

  public ViewDescription viewId(String viewId) {
    this.viewId = viewId;
    return this;
  }

  /**
   * Get viewId
   * @return viewId
   */
  
  @Schema(name = "ViewId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ViewId")
  public String getViewId() {
    return viewId;
  }

  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  public ViewDescription timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "Timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ViewDescription viewVersion(Long viewVersion) {
    this.viewVersion = viewVersion;
    return this;
  }

  /**
   * Get viewVersion
   * minimum: 0
   * maximum: 4294967295
   * @return viewVersion
   */
  @Min(0L) @Max(4294967295L) 
  @Schema(name = "ViewVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ViewVersion")
  public Long getViewVersion() {
    return viewVersion;
  }

  public void setViewVersion(Long viewVersion) {
    this.viewVersion = viewVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ViewDescription viewDescription = (ViewDescription) o;
    return Objects.equals(this.viewId, viewDescription.viewId) &&
        Objects.equals(this.timestamp, viewDescription.timestamp) &&
        Objects.equals(this.viewVersion, viewDescription.viewVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(viewId, timestamp, viewVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ViewDescription {\n");
    sb.append("    viewId: ").append(toIndentedString(viewId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    viewVersion: ").append(toIndentedString(viewVersion)).append("\n");
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


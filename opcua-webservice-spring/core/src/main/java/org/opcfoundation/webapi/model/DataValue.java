package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.2.18).
 */

@Schema(name = "DataValue", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.2.18).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataValue {

  private Integer uaType = 0;

  private @Nullable Object value = null;

  @Valid
  private List<@Min(0)Integer> dimensions = new ArrayList<>();

  private @Nullable StatusCode statusCode;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime sourceTimestamp;

  private @Nullable Integer sourcePicoseconds;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime serverTimestamp;

  private @Nullable Integer serverPicoseconds;

  public DataValue uaType(Integer uaType) {
    this.uaType = uaType;
    return this;
  }

  /**
   * Get uaType
   * minimum: 0
   * maximum: 255
   * @return uaType
   */
  @Min(0) @Max(255) 
  @Schema(name = "UaType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UaType")
  public Integer getUaType() {
    return uaType;
  }

  public void setUaType(Integer uaType) {
    this.uaType = uaType;
  }

  public DataValue value(Object value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "Value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Value")
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public DataValue dimensions(List<@Min(0)Integer> dimensions) {
    this.dimensions = dimensions;
    return this;
  }

  public DataValue addDimensionsItem(Integer dimensionsItem) {
    if (this.dimensions == null) {
      this.dimensions = new ArrayList<>();
    }
    this.dimensions.add(dimensionsItem);
    return this;
  }

  /**
   * Get dimensions
   * @return dimensions
   */
  
  @Schema(name = "Dimensions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Dimensions")
  public List<@Min(0)Integer> getDimensions() {
    return dimensions;
  }

  public void setDimensions(List<@Min(0)Integer> dimensions) {
    this.dimensions = dimensions;
  }

  public DataValue statusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  /**
   * Get statusCode
   * @return statusCode
   */
  @Valid 
  @Schema(name = "StatusCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("StatusCode")
  public StatusCode getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public DataValue sourceTimestamp(OffsetDateTime sourceTimestamp) {
    this.sourceTimestamp = sourceTimestamp;
    return this;
  }

  /**
   * Get sourceTimestamp
   * @return sourceTimestamp
   */
  @Valid 
  @Schema(name = "SourceTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SourceTimestamp")
  public OffsetDateTime getSourceTimestamp() {
    return sourceTimestamp;
  }

  public void setSourceTimestamp(OffsetDateTime sourceTimestamp) {
    this.sourceTimestamp = sourceTimestamp;
  }

  public DataValue sourcePicoseconds(Integer sourcePicoseconds) {
    this.sourcePicoseconds = sourcePicoseconds;
    return this;
  }

  /**
   * Get sourcePicoseconds
   * minimum: 0
   * maximum: 65535
   * @return sourcePicoseconds
   */
  @Min(0) @Max(65535) 
  @Schema(name = "SourcePicoseconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SourcePicoseconds")
  public Integer getSourcePicoseconds() {
    return sourcePicoseconds;
  }

  public void setSourcePicoseconds(Integer sourcePicoseconds) {
    this.sourcePicoseconds = sourcePicoseconds;
  }

  public DataValue serverTimestamp(OffsetDateTime serverTimestamp) {
    this.serverTimestamp = serverTimestamp;
    return this;
  }

  /**
   * Get serverTimestamp
   * @return serverTimestamp
   */
  @Valid 
  @Schema(name = "ServerTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerTimestamp")
  public OffsetDateTime getServerTimestamp() {
    return serverTimestamp;
  }

  public void setServerTimestamp(OffsetDateTime serverTimestamp) {
    this.serverTimestamp = serverTimestamp;
  }

  public DataValue serverPicoseconds(Integer serverPicoseconds) {
    this.serverPicoseconds = serverPicoseconds;
    return this;
  }

  /**
   * Get serverPicoseconds
   * minimum: 0
   * maximum: 65535
   * @return serverPicoseconds
   */
  @Min(0) @Max(65535) 
  @Schema(name = "ServerPicoseconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ServerPicoseconds")
  public Integer getServerPicoseconds() {
    return serverPicoseconds;
  }

  public void setServerPicoseconds(Integer serverPicoseconds) {
    this.serverPicoseconds = serverPicoseconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataValue dataValue = (DataValue) o;
    return Objects.equals(this.uaType, dataValue.uaType) &&
        Objects.equals(this.value, dataValue.value) &&
        Objects.equals(this.dimensions, dataValue.dimensions) &&
        Objects.equals(this.statusCode, dataValue.statusCode) &&
        Objects.equals(this.sourceTimestamp, dataValue.sourceTimestamp) &&
        Objects.equals(this.sourcePicoseconds, dataValue.sourcePicoseconds) &&
        Objects.equals(this.serverTimestamp, dataValue.serverTimestamp) &&
        Objects.equals(this.serverPicoseconds, dataValue.serverPicoseconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaType, value, dimensions, statusCode, sourceTimestamp, sourcePicoseconds, serverTimestamp, serverPicoseconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataValue {\n");
    sb.append("    uaType: ").append(toIndentedString(uaType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    dimensions: ").append(toIndentedString(dimensions)).append("\n");
    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    sourceTimestamp: ").append(toIndentedString(sourceTimestamp)).append("\n");
    sb.append("    sourcePicoseconds: ").append(toIndentedString(sourcePicoseconds)).append("\n");
    sb.append("    serverTimestamp: ").append(toIndentedString(serverTimestamp)).append("\n");
    sb.append("    serverPicoseconds: ").append(toIndentedString(serverPicoseconds)).append("\n");
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


package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.4).
 */

@Schema(name = "StatusChangeNotification", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.4).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class StatusChangeNotification {

  private @Nullable StatusCode status;

  private @Nullable DiagnosticInfo diagnosticInfo;

  public StatusChangeNotification status(StatusCode status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @Valid 
  @Schema(name = "Status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Status")
  public StatusCode getStatus() {
    return status;
  }

  public void setStatus(StatusCode status) {
    this.status = status;
  }

  public StatusChangeNotification diagnosticInfo(DiagnosticInfo diagnosticInfo) {
    this.diagnosticInfo = diagnosticInfo;
    return this;
  }

  /**
   * Get diagnosticInfo
   * @return diagnosticInfo
   */
  @Valid 
  @Schema(name = "DiagnosticInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiagnosticInfo")
  public DiagnosticInfo getDiagnosticInfo() {
    return diagnosticInfo;
  }

  public void setDiagnosticInfo(DiagnosticInfo diagnosticInfo) {
    this.diagnosticInfo = diagnosticInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusChangeNotification statusChangeNotification = (StatusChangeNotification) o;
    return Objects.equals(this.status, statusChangeNotification.status) &&
        Objects.equals(this.diagnosticInfo, statusChangeNotification.diagnosticInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, diagnosticInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusChangeNotification {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    diagnosticInfo: ").append(toIndentedString(diagnosticInfo)).append("\n");
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


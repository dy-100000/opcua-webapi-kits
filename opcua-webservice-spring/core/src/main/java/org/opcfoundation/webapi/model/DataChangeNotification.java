package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2).
 */

@Schema(name = "DataChangeNotification", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DataChangeNotification {

  @Valid
  private List<@Valid MonitoredItemNotification> monitoredItems = new ArrayList<>();

  @Valid
  private List<@Valid DiagnosticInfo> diagnosticInfos = new ArrayList<>();

  public DataChangeNotification monitoredItems(List<@Valid MonitoredItemNotification> monitoredItems) {
    this.monitoredItems = monitoredItems;
    return this;
  }

  public DataChangeNotification addMonitoredItemsItem(MonitoredItemNotification monitoredItemsItem) {
    if (this.monitoredItems == null) {
      this.monitoredItems = new ArrayList<>();
    }
    this.monitoredItems.add(monitoredItemsItem);
    return this;
  }

  /**
   * Get monitoredItems
   * @return monitoredItems
   */
  @Valid 
  @Schema(name = "MonitoredItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("MonitoredItems")
  public List<@Valid MonitoredItemNotification> getMonitoredItems() {
    return monitoredItems;
  }

  public void setMonitoredItems(List<@Valid MonitoredItemNotification> monitoredItems) {
    this.monitoredItems = monitoredItems;
  }

  public DataChangeNotification diagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
    return this;
  }

  public DataChangeNotification addDiagnosticInfosItem(DiagnosticInfo diagnosticInfosItem) {
    if (this.diagnosticInfos == null) {
      this.diagnosticInfos = new ArrayList<>();
    }
    this.diagnosticInfos.add(diagnosticInfosItem);
    return this;
  }

  /**
   * Get diagnosticInfos
   * @return diagnosticInfos
   */
  @Valid 
  @Schema(name = "DiagnosticInfos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("DiagnosticInfos")
  public List<@Valid DiagnosticInfo> getDiagnosticInfos() {
    return diagnosticInfos;
  }

  public void setDiagnosticInfos(List<@Valid DiagnosticInfo> diagnosticInfos) {
    this.diagnosticInfos = diagnosticInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataChangeNotification dataChangeNotification = (DataChangeNotification) o;
    return Objects.equals(this.monitoredItems, dataChangeNotification.monitoredItems) &&
        Objects.equals(this.diagnosticInfos, dataChangeNotification.diagnosticInfos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(monitoredItems, diagnosticInfos);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataChangeNotification {\n");
    sb.append("    monitoredItems: ").append(toIndentedString(monitoredItems)).append("\n");
    sb.append("    diagnosticInfos: ").append(toIndentedString(diagnosticInfos)).append("\n");
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


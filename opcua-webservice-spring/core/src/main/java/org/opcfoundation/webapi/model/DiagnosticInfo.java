package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part4/v105/docs/7.12).
 */

@Schema(name = "DiagnosticInfo", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part4/v105/docs/7.12).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class DiagnosticInfo {

  private @Nullable Integer symbolicId;

  private @Nullable Integer namespaceUri;

  private @Nullable Integer locale;

  private @Nullable Integer localizedText;

  private @Nullable String additionalInfo;

  private @Nullable StatusCode innerStatusCode;

  private @Nullable DiagnosticInfo innerDiagnosticInfo;

  public DiagnosticInfo symbolicId(Integer symbolicId) {
    this.symbolicId = symbolicId;
    return this;
  }

  /**
   * Get symbolicId
   * @return symbolicId
   */
  
  @Schema(name = "SymbolicId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SymbolicId")
  public Integer getSymbolicId() {
    return symbolicId;
  }

  public void setSymbolicId(Integer symbolicId) {
    this.symbolicId = symbolicId;
  }

  public DiagnosticInfo namespaceUri(Integer namespaceUri) {
    this.namespaceUri = namespaceUri;
    return this;
  }

  /**
   * Get namespaceUri
   * @return namespaceUri
   */
  
  @Schema(name = "NamespaceUri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NamespaceUri")
  public Integer getNamespaceUri() {
    return namespaceUri;
  }

  public void setNamespaceUri(Integer namespaceUri) {
    this.namespaceUri = namespaceUri;
  }

  public DiagnosticInfo locale(Integer locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "Locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Locale")
  public Integer getLocale() {
    return locale;
  }

  public void setLocale(Integer locale) {
    this.locale = locale;
  }

  public DiagnosticInfo localizedText(Integer localizedText) {
    this.localizedText = localizedText;
    return this;
  }

  /**
   * Get localizedText
   * @return localizedText
   */
  
  @Schema(name = "LocalizedText", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("LocalizedText")
  public Integer getLocalizedText() {
    return localizedText;
  }

  public void setLocalizedText(Integer localizedText) {
    this.localizedText = localizedText;
  }

  public DiagnosticInfo additionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
    return this;
  }

  /**
   * Get additionalInfo
   * @return additionalInfo
   */
  
  @Schema(name = "AdditionalInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("AdditionalInfo")
  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public DiagnosticInfo innerStatusCode(StatusCode innerStatusCode) {
    this.innerStatusCode = innerStatusCode;
    return this;
  }

  /**
   * Get innerStatusCode
   * @return innerStatusCode
   */
  @Valid 
  @Schema(name = "InnerStatusCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("InnerStatusCode")
  public StatusCode getInnerStatusCode() {
    return innerStatusCode;
  }

  public void setInnerStatusCode(StatusCode innerStatusCode) {
    this.innerStatusCode = innerStatusCode;
  }

  public DiagnosticInfo innerDiagnosticInfo(DiagnosticInfo innerDiagnosticInfo) {
    this.innerDiagnosticInfo = innerDiagnosticInfo;
    return this;
  }

  /**
   * Get innerDiagnosticInfo
   * @return innerDiagnosticInfo
   */
  @Valid 
  @Schema(name = "InnerDiagnosticInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("InnerDiagnosticInfo")
  public DiagnosticInfo getInnerDiagnosticInfo() {
    return innerDiagnosticInfo;
  }

  public void setInnerDiagnosticInfo(DiagnosticInfo innerDiagnosticInfo) {
    this.innerDiagnosticInfo = innerDiagnosticInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DiagnosticInfo diagnosticInfo = (DiagnosticInfo) o;
    return Objects.equals(this.symbolicId, diagnosticInfo.symbolicId) &&
        Objects.equals(this.namespaceUri, diagnosticInfo.namespaceUri) &&
        Objects.equals(this.locale, diagnosticInfo.locale) &&
        Objects.equals(this.localizedText, diagnosticInfo.localizedText) &&
        Objects.equals(this.additionalInfo, diagnosticInfo.additionalInfo) &&
        Objects.equals(this.innerStatusCode, diagnosticInfo.innerStatusCode) &&
        Objects.equals(this.innerDiagnosticInfo, diagnosticInfo.innerDiagnosticInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbolicId, namespaceUri, locale, localizedText, additionalInfo, innerStatusCode, innerDiagnosticInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DiagnosticInfo {\n");
    sb.append("    symbolicId: ").append(toIndentedString(symbolicId)).append("\n");
    sb.append("    namespaceUri: ").append(toIndentedString(namespaceUri)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    localizedText: ").append(toIndentedString(localizedText)).append("\n");
    sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
    sb.append("    innerStatusCode: ").append(toIndentedString(innerStatusCode)).append("\n");
    sb.append("    innerDiagnosticInfo: ").append(toIndentedString(innerDiagnosticInfo)).append("\n");
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


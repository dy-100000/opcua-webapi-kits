package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part3/v105/docs/8.5).
 */

@Schema(name = "LocalizedText", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part3/v105/docs/8.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class LocalizedText {

  private @Nullable String locale;

  private @Nullable String text;

  public LocalizedText locale(String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "Locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Locale")
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public LocalizedText text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
   */
  
  @Schema(name = "Text", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("Text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocalizedText localizedText = (LocalizedText) o;
    return Objects.equals(this.locale, localizedText.locale) &&
        Objects.equals(this.text, localizedText.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locale, text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocalizedText {\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
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


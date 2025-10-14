package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import org.springframework.lang.Nullable;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.2.16).
 */

@Schema(name = "ExtensionObject", description = "[Link to specification](https://reference.opcfoundation.org/Core/Part6/v105/docs/5.4.2.16).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ExtensionObject {

  private @Nullable String uaTypeId;

  private @Nullable Integer uaEncoding;

  private @Nullable byte[] uaBody;

  public ExtensionObject uaTypeId(String uaTypeId) {
    this.uaTypeId = uaTypeId;
    return this;
  }

  /**
   * Get uaTypeId
   * @return uaTypeId
   */
  
  @Schema(name = "UaTypeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UaTypeId")
  public String getUaTypeId() {
    return uaTypeId;
  }

  public void setUaTypeId(String uaTypeId) {
    this.uaTypeId = uaTypeId;
  }

  public ExtensionObject uaEncoding(Integer uaEncoding) {
    this.uaEncoding = uaEncoding;
    return this;
  }

  /**
   * Get uaEncoding
   * minimum: 0
   * maximum: 255
   * @return uaEncoding
   */
  @Min(0) @Max(255) 
  @Schema(name = "UaEncoding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UaEncoding")
  public Integer getUaEncoding() {
    return uaEncoding;
  }

  public void setUaEncoding(Integer uaEncoding) {
    this.uaEncoding = uaEncoding;
  }

  public ExtensionObject uaBody(byte[] uaBody) {
    this.uaBody = uaBody;
    return this;
  }

  /**
   * Get uaBody
   * @return uaBody
   */
  
  @Schema(name = "UaBody", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UaBody")
  public byte[] getUaBody() {
    return uaBody;
  }

  public void setUaBody(byte[] uaBody) {
    this.uaBody = uaBody;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtensionObject extensionObject = (ExtensionObject) o;
    return Objects.equals(this.uaTypeId, extensionObject.uaTypeId) &&
        Objects.equals(this.uaEncoding, extensionObject.uaEncoding) &&
        Arrays.equals(this.uaBody, extensionObject.uaBody);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaTypeId, uaEncoding, Arrays.hashCode(uaBody));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtensionObject {\n");
    sb.append("    uaTypeId: ").append(toIndentedString(uaTypeId)).append("\n");
    sb.append("    uaEncoding: ").append(toIndentedString(uaEncoding)).append("\n");
    sb.append("    uaBody: ").append(toIndentedString(uaBody)).append("\n");
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


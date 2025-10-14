package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.5).
 */

@Schema(name = "ModificationInfo", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.5).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class ModificationInfo {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime modificationTime = OffsetDateTime.parse("0001-01-01T08:05:43+08:05:43[Asia/Shanghai]", java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(java.time.ZoneId.systemDefault()));

  private @Nullable Integer updateType;

  private @Nullable String userName;

  public ModificationInfo modificationTime(OffsetDateTime modificationTime) {
    this.modificationTime = modificationTime;
    return this;
  }

  /**
   * Get modificationTime
   * @return modificationTime
   */
  @Valid 
  @Schema(name = "ModificationTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ModificationTime")
  public OffsetDateTime getModificationTime() {
    return modificationTime;
  }

  public void setModificationTime(OffsetDateTime modificationTime) {
    this.modificationTime = modificationTime;
  }

  public ModificationInfo updateType(Integer updateType) {
    this.updateType = updateType;
    return this;
  }

  /**
   * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.7).
   * @return updateType
   */
  
  @Schema(name = "UpdateType", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part11/6.7).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UpdateType")
  public Integer getUpdateType() {
    return updateType;
  }

  public void setUpdateType(Integer updateType) {
    this.updateType = updateType;
  }

  public ModificationInfo userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
   */
  
  @Schema(name = "UserName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("UserName")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModificationInfo modificationInfo = (ModificationInfo) o;
    return Objects.equals(this.modificationTime, modificationInfo.modificationTime) &&
        Objects.equals(this.updateType, modificationInfo.updateType) &&
        Objects.equals(this.userName, modificationInfo.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modificationTime, updateType, userName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModificationInfo {\n");
    sb.append("    modificationTime: ").append(toIndentedString(modificationTime)).append("\n");
    sb.append("    updateType: ").append(toIndentedString(updateType)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
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


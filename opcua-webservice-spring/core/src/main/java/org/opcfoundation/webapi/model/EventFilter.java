package org.opcfoundation.webapi.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * [Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3).
 */

@Schema(name = "EventFilter", description = "[Link to specification](https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.3).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.12.0")
public class EventFilter {

  @Valid
  private List<@Valid SimpleAttributeOperand> selectClauses = new ArrayList<>();

  private @Nullable ContentFilter whereClause;

  public EventFilter selectClauses(List<@Valid SimpleAttributeOperand> selectClauses) {
    this.selectClauses = selectClauses;
    return this;
  }

  public EventFilter addSelectClausesItem(SimpleAttributeOperand selectClausesItem) {
    if (this.selectClauses == null) {
      this.selectClauses = new ArrayList<>();
    }
    this.selectClauses.add(selectClausesItem);
    return this;
  }

  /**
   * Get selectClauses
   * @return selectClauses
   */
  @Valid 
  @Schema(name = "SelectClauses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("SelectClauses")
  public List<@Valid SimpleAttributeOperand> getSelectClauses() {
    return selectClauses;
  }

  public void setSelectClauses(List<@Valid SimpleAttributeOperand> selectClauses) {
    this.selectClauses = selectClauses;
  }

  public EventFilter whereClause(ContentFilter whereClause) {
    this.whereClause = whereClause;
    return this;
  }

  /**
   * Get whereClause
   * @return whereClause
   */
  @Valid 
  @Schema(name = "WhereClause", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("WhereClause")
  public ContentFilter getWhereClause() {
    return whereClause;
  }

  public void setWhereClause(ContentFilter whereClause) {
    this.whereClause = whereClause;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventFilter eventFilter = (EventFilter) o;
    return Objects.equals(this.selectClauses, eventFilter.selectClauses) &&
        Objects.equals(this.whereClause, eventFilter.whereClause);
  }

  @Override
  public int hashCode() {
    return Objects.hash(selectClauses, whereClause);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventFilter {\n");
    sb.append("    selectClauses: ").append(toIndentedString(selectClauses)).append("\n");
    sb.append("    whereClause: ").append(toIndentedString(whereClause)).append("\n");
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


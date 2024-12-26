package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * Pass
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-23T20:10:50.412641800+02:00[Europe/Athens]", comments = "Generator version: 7.9.0")
public class Pass {

  private Integer passIndex;

  private String passID;

  private String timestamp;

  private String tagID;

  private String tagProvider;

  /**
   * Gets or Sets passType
   */
  public enum PassTypeEnum {
    HOME("home"),
    
    VISITOR("visitor");

    private String value;

    PassTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PassTypeEnum fromValue(String value) {
      for (PassTypeEnum b : PassTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PassTypeEnum passType;

  private String passCharge;

  public Pass passIndex(Integer passIndex) {
    this.passIndex = passIndex;
    return this;
  }

  /**
   * Get passIndex
   * @return passIndex
   */
  
  @Schema(name = "passIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passIndex")
  public Integer getPassIndex() {
    return passIndex;
  }

  public void setPassIndex(Integer passIndex) {
    this.passIndex = passIndex;
  }

  public Pass passID(String passID) {
    this.passID = passID;
    return this;
  }

  /**
   * Get passID
   * @return passID
   */
  
  @Schema(name = "passID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passID")
  public String getPassID() {
    return passID;
  }

  public void setPassID(String passID) {
    this.passID = passID;
  }

  public Pass timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public Pass tagID(String tagID) {
    this.tagID = tagID;
    return this;
  }

  /**
   * Get tagID
   * @return tagID
   */
  
  @Schema(name = "tagID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tagID")
  public String getTagID() {
    return tagID;
  }

  public void setTagID(String tagID) {
    this.tagID = tagID;
  }

  public Pass tagProvider(String tagProvider) {
    this.tagProvider = tagProvider;
    return this;
  }

  /**
   * Get tagProvider
   * @return tagProvider
   */
  
  @Schema(name = "tagProvider", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tagProvider")
  public String getTagProvider() {
    return tagProvider;
  }

  public void setTagProvider(String tagProvider) {
    this.tagProvider = tagProvider;
  }

  public Pass passType(PassTypeEnum passType) {
    this.passType = passType;
    return this;
  }

  /**
   * Get passType
   * @return passType
   */
  
  @Schema(name = "passType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passType")
  public PassTypeEnum getPassType() {
    return passType;
  }

  public void setPassType(PassTypeEnum passType) {
    this.passType = passType;
  }

  public Pass passCharge(String passCharge) {
    this.passCharge = passCharge;
    return this;
  }

  /**
   * Get passCharge
   * @return passCharge
   */
  
  @Schema(name = "passCharge", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passCharge")
  public String getPassCharge() {
    return passCharge;
  }

  public void setPassCharge(String passCharge) {
    this.passCharge = passCharge;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pass pass = (Pass) o;
    return Objects.equals(this.passIndex, pass.passIndex) &&
        Objects.equals(this.passID, pass.passID) &&
        Objects.equals(this.timestamp, pass.timestamp) &&
        Objects.equals(this.tagID, pass.tagID) &&
        Objects.equals(this.tagProvider, pass.tagProvider) &&
        Objects.equals(this.passType, pass.passType) &&
        Objects.equals(this.passCharge, pass.passCharge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(passIndex, passID, timestamp, tagID, tagProvider, passType, passCharge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pass {\n");
    sb.append("    passIndex: ").append(toIndentedString(passIndex)).append("\n");
    sb.append("    passID: ").append(toIndentedString(passID)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    tagID: ").append(toIndentedString(tagID)).append("\n");
    sb.append("    tagProvider: ").append(toIndentedString(tagProvider)).append("\n");
    sb.append("    passType: ").append(toIndentedString(passType)).append("\n");
    sb.append("    passCharge: ").append(toIndentedString(passCharge)).append("\n");
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


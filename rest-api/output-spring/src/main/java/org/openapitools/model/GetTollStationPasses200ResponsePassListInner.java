package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * GetTollStationPasses200ResponsePassListInner
 */

@JsonTypeName("getTollStationPasses_200_response_passList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-23T20:10:50.412641800+02:00[Europe/Athens]", comments = "Generator version: 7.9.0")
public class GetTollStationPasses200ResponsePassListInner {

  private Integer passIndex;

  private String passID;

  private String timestamp;

  private String tagID;

  private String tagProvider;

  /**
   * Type of pass
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

  public GetTollStationPasses200ResponsePassListInner passIndex(Integer passIndex) {
    this.passIndex = passIndex;
    return this;
  }

  /**
   * The pass number
   * @return passIndex
   */
  
  @Schema(name = "passIndex", description = "The pass number", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passIndex")
  public Integer getPassIndex() {
    return passIndex;
  }

  public void setPassIndex(Integer passIndex) {
    this.passIndex = passIndex;
  }

  public GetTollStationPasses200ResponsePassListInner passID(String passID) {
    this.passID = passID;
    return this;
  }

  /**
   * ID of the pass
   * @return passID
   */
  
  @Schema(name = "passID", description = "ID of the pass", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passID")
  public String getPassID() {
    return passID;
  }

  public void setPassID(String passID) {
    this.passID = passID;
  }

  public GetTollStationPasses200ResponsePassListInner timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Timestamp when the pass was recorded
   * @return timestamp
   */
  
  @Schema(name = "timestamp", description = "Timestamp when the pass was recorded", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public GetTollStationPasses200ResponsePassListInner tagID(String tagID) {
    this.tagID = tagID;
    return this;
  }

  /**
   * Tag ID associated with the pass
   * @return tagID
   */
  
  @Schema(name = "tagID", description = "Tag ID associated with the pass", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tagID")
  public String getTagID() {
    return tagID;
  }

  public void setTagID(String tagID) {
    this.tagID = tagID;
  }

  public GetTollStationPasses200ResponsePassListInner tagProvider(String tagProvider) {
    this.tagProvider = tagProvider;
    return this;
  }

  /**
   * The provider of the tag
   * @return tagProvider
   */
  
  @Schema(name = "tagProvider", description = "The provider of the tag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tagProvider")
  public String getTagProvider() {
    return tagProvider;
  }

  public void setTagProvider(String tagProvider) {
    this.tagProvider = tagProvider;
  }

  public GetTollStationPasses200ResponsePassListInner passType(PassTypeEnum passType) {
    this.passType = passType;
    return this;
  }

  /**
   * Type of pass
   * @return passType
   */
  
  @Schema(name = "passType", description = "Type of pass", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passType")
  public PassTypeEnum getPassType() {
    return passType;
  }

  public void setPassType(PassTypeEnum passType) {
    this.passType = passType;
  }

  public GetTollStationPasses200ResponsePassListInner passCharge(String passCharge) {
    this.passCharge = passCharge;
    return this;
  }

  /**
   * Charging details for the pass
   * @return passCharge
   */
  
  @Schema(name = "passCharge", description = "Charging details for the pass", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    GetTollStationPasses200ResponsePassListInner getTollStationPasses200ResponsePassListInner = (GetTollStationPasses200ResponsePassListInner) o;
    return Objects.equals(this.passIndex, getTollStationPasses200ResponsePassListInner.passIndex) &&
        Objects.equals(this.passID, getTollStationPasses200ResponsePassListInner.passID) &&
        Objects.equals(this.timestamp, getTollStationPasses200ResponsePassListInner.timestamp) &&
        Objects.equals(this.tagID, getTollStationPasses200ResponsePassListInner.tagID) &&
        Objects.equals(this.tagProvider, getTollStationPasses200ResponsePassListInner.tagProvider) &&
        Objects.equals(this.passType, getTollStationPasses200ResponsePassListInner.passType) &&
        Objects.equals(this.passCharge, getTollStationPasses200ResponsePassListInner.passCharge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(passIndex, passID, timestamp, tagID, tagProvider, passType, passCharge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTollStationPasses200ResponsePassListInner {\n");
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


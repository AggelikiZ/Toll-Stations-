package com.payway.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeName("ResetStations_400_response")
public class ResetStations400Response {

  private String status;
  private String info;

  public ResetStations400Response status(String status) {
    this.status = status;
    return this;
  }

  @Schema(name = "status", example = "failed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ResetStations400Response info(String info) {
    this.info = info;
    return this;
  }

  @Schema(name = "info", example = "Invalid input: Check file structure or parameters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("info")
  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetStations400Response resetStations400Response = (ResetStations400Response) o;
    return Objects.equals(this.status, resetStations400Response.status) &&
        Objects.equals(this.info, resetStations400Response.info);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, info);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetStations400Response {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
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


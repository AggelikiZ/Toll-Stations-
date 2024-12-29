package com.payway.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.payway.model.GetTollStationPasses200ResponsePassListInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetTollStationPasses200Response
 */

@JsonTypeName("getTollStationPasses_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-29T20:20:36.405994100+02:00[Europe/Athens]")
public class GetTollStationPasses200Response {

  private String stationID;

  private String stationOperator;

  private String requestTimestamp;

  private String periodFrom;

  private String periodTo;

  private Integer nPasses;

  @Valid
  private List<@Valid GetTollStationPasses200ResponsePassListInner> passList;

  public GetTollStationPasses200Response stationID(String stationID) {
    this.stationID = stationID;
    return this;
  }

  /**
   * ID of the station
   * @return stationID
  */
  
  @Schema(name = "stationID", description = "ID of the station", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stationID")
  public String getStationID() {
    return stationID;
  }

  public void setStationID(String stationID) {
    this.stationID = stationID;
  }

  public GetTollStationPasses200Response stationOperator(String stationOperator) {
    this.stationOperator = stationOperator;
    return this;
  }

  /**
   * Name of the worker at the station
   * @return stationOperator
  */
  
  @Schema(name = "stationOperator", description = "Name of the worker at the station", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stationOperator")
  public String getStationOperator() {
    return stationOperator;
  }

  public void setStationOperator(String stationOperator) {
    this.stationOperator = stationOperator;
  }

  public GetTollStationPasses200Response requestTimestamp(String requestTimestamp) {
    this.requestTimestamp = requestTimestamp;
    return this;
  }

  /**
   * Timestamp of the request
   * @return requestTimestamp
  */
  
  @Schema(name = "requestTimestamp", description = "Timestamp of the request", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requestTimestamp")
  public String getRequestTimestamp() {
    return requestTimestamp;
  }

  public void setRequestTimestamp(String requestTimestamp) {
    this.requestTimestamp = requestTimestamp;
  }

  public GetTollStationPasses200Response periodFrom(String periodFrom) {
    this.periodFrom = periodFrom;
    return this;
  }

  /**
   * Start of the period (YYYYMMDD)
   * @return periodFrom
  */
  
  @Schema(name = "periodFrom", description = "Start of the period (YYYYMMDD)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("periodFrom")
  public String getPeriodFrom() {
    return periodFrom;
  }

  public void setPeriodFrom(String periodFrom) {
    this.periodFrom = periodFrom;
  }

  public GetTollStationPasses200Response periodTo(String periodTo) {
    this.periodTo = periodTo;
    return this;
  }

  /**
   * End of the period (YYYYMMDD)
   * @return periodTo
  */
  
  @Schema(name = "periodTo", description = "End of the period (YYYYMMDD)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("periodTo")
  public String getPeriodTo() {
    return periodTo;
  }

  public void setPeriodTo(String periodTo) {
    this.periodTo = periodTo;
  }

  public GetTollStationPasses200Response nPasses(Integer nPasses) {
    this.nPasses = nPasses;
    return this;
  }

  /**
   * Number of passes in the given period
   * @return nPasses
  */
  
  @Schema(name = "nPasses", description = "Number of passes in the given period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nPasses")
  public Integer getnPasses() {
    return nPasses;
  }

  public void setnPasses(Integer nPasses) {
    this.nPasses = nPasses;
  }

  public GetTollStationPasses200Response passList(List<@Valid GetTollStationPasses200ResponsePassListInner> passList) {
    this.passList = passList;
    return this;
  }

  public GetTollStationPasses200Response addPassListItem(GetTollStationPasses200ResponsePassListInner passListItem) {
    if (this.passList == null) {
      this.passList = new ArrayList<>();
    }
    this.passList.add(passListItem);
    return this;
  }

  /**
   * Get passList
   * @return passList
  */
  @Valid 
  @Schema(name = "passList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passList")
  public List<@Valid GetTollStationPasses200ResponsePassListInner> getPassList() {
    return passList;
  }

  public void setPassList(List<@Valid GetTollStationPasses200ResponsePassListInner> passList) {
    this.passList = passList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetTollStationPasses200Response getTollStationPasses200Response = (GetTollStationPasses200Response) o;
    return Objects.equals(this.stationID, getTollStationPasses200Response.stationID) &&
        Objects.equals(this.stationOperator, getTollStationPasses200Response.stationOperator) &&
        Objects.equals(this.requestTimestamp, getTollStationPasses200Response.requestTimestamp) &&
        Objects.equals(this.periodFrom, getTollStationPasses200Response.periodFrom) &&
        Objects.equals(this.periodTo, getTollStationPasses200Response.periodTo) &&
        Objects.equals(this.nPasses, getTollStationPasses200Response.nPasses) &&
        Objects.equals(this.passList, getTollStationPasses200Response.passList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stationID, stationOperator, requestTimestamp, periodFrom, periodTo, nPasses, passList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetTollStationPasses200Response {\n");
    sb.append("    stationID: ").append(toIndentedString(stationID)).append("\n");
    sb.append("    stationOperator: ").append(toIndentedString(stationOperator)).append("\n");
    sb.append("    requestTimestamp: ").append(toIndentedString(requestTimestamp)).append("\n");
    sb.append("    periodFrom: ").append(toIndentedString(periodFrom)).append("\n");
    sb.append("    periodTo: ").append(toIndentedString(periodTo)).append("\n");
    sb.append("    nPasses: ").append(toIndentedString(nPasses)).append("\n");
    sb.append("    passList: ").append(toIndentedString(passList)).append("\n");
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


package com.payway.models;

import java.sql.Timestamp;
import java.util.List;
import com.payway.models.PassDetails;
import io.swagger.v3.oas.annotations.media.Schema;

// Class to represent the main toll station pass details
@Schema(description = "Details of a toll station pass")
public class TollStationPassesDetails {
    private String stationID;
    private String stationOperator;
    private String requestTimestamp;
    private String periodFrom;
    private String periodTo;
    private Long nPasses;
    private List<PassDetails> passList;

    // Getters and Setters
    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getStationOperator() {
        return stationOperator;
    }

    public void setStationOperator(String stationOperator) {
        this.stationOperator = stationOperator;
    }

    public String getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(String requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public Long getnPasses() {
        return nPasses;
    }

    public void setnPasses(Long nPasses) {
        this.nPasses = nPasses;
    }

    public List<PassDetails> getPassList() {
        return passList;
    }

    public void setPassList(List<PassDetails> passList) {
        this.passList = passList;
    }
}

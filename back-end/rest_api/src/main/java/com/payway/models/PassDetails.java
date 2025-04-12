package com.payway.models;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

// Class to represent each pass detail
@Schema(description = "Pass details associated with a toll station")
public class PassDetails {
    private Long passIndex;
    private String passID;
    private Timestamp timestamp;
    private String tagID;
    private String tagProvider;
    private String passType;
    private BigDecimal passCharge;

    // Getters and Setters
    public Long getPassIndex() {
        return passIndex;
    }

    public void setPassIndex(Long passIndex) {
        this.passIndex = passIndex;
    }

    public String getPassID() {
        return passID;
    }

    public void setPassID(String passID) {
        this.passID = passID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getTagProvider() {
        return tagProvider;
    }

    public void setTagProvider(String tagProvider) {
        this.tagProvider = tagProvider;
    }

    public String getPassType() {
        return passType;
    }

    public void setPassType(String passType) {
        this.passType = passType;
    }

    public BigDecimal getPassCharge() {
        return passCharge;
    }

    public void setPassCharge(BigDecimal passCharge) {
        this.passCharge = passCharge;
    }
}


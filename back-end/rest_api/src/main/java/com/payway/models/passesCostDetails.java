package com.payway.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class passesCostDetails {
    private String tollOpID;
    private String tagOpID;
    private Timestamp requestTimestamp;
    private String periodFrom;
    private String periodTo;
    private Long nPasses;
    private BigDecimal totalCost;

    public String gettollOpID() {
        return tollOpID;
    }

    public void settollOpID(String tollOpID) {
        this.tollOpID = tollOpID;
    }

    public String gettagOpID() {
        return tagOpID;
    }

    public void settagOpID(String tagOpID) {
        this.tagOpID = tagOpID;
    }

    public Timestamp getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(Timestamp requestTimestamp) {
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

    public BigDecimal getTotalCost() {return totalCost;}

    public void setTotalCost(BigDecimal totalCost) {this.totalCost = totalCost;}

}

package com.payway.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pass")
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for primary key
    @Column(name = "pass_id")
    private Long passId;

    @Column(name = "pass_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime passTime;

    @Column(name = "tag_ref", nullable = false, length = 20)
    private String tagRef;

    @Column(name = "station_id", nullable = false, columnDefinition = "CHAR(4)")
    private String stationId;

    @Column(name = "charge", nullable = false, precision = 4, scale = 2)
    private BigDecimal charge;

    // Getters and Setters
    public Long getPassId() {
        return passId;
    }

    public void setPassId(Long passId) {
        this.passId = passId;
    }

    public LocalDateTime getPassTime() {
        return passTime;
    }

    public void setPassTime(LocalDateTime passTime) {
        this.passTime = passTime;
    }

    public String getTagRef() {
        return tagRef;
    }

    public void setTagRef(String tagRef) {
        this.tagRef = tagRef;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    @Override
    public String toString() {
        return "Pass{" +
                "passId=" + passId +
                ", passTime=" + passTime +
                ", tagRef='" + tagRef + '\'' +
                ", stationId='" + stationId + '\'' +
                ", charge=" + charge +
                '}';
    }
}


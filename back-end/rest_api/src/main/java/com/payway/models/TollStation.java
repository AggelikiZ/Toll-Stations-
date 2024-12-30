package com.payway.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tollstation")
public class TollStation {
    @Id
    @Column(name = "station_id")
    private String id;

    @Column(name = "op_id", nullable = false)
    private String opId;

    @Column(name = "station_name", nullable = false)
    private String name;

    @Column(name = "station_type", columnDefinition = "CHAR", nullable = false)
    private String type;

    @Column(name = "locality", nullable = false)
    private String locality;

    @Column(name = "road", nullable = false)
    private String road;

    @Column(name = "latitude", nullable = false)
    private BigDecimal lat;

    @Column(name = "longitude", nullable = false)
    private BigDecimal  lng;

    @Column(name = "price1", nullable = false)
    private BigDecimal  price1;

    @Column(name = "price2", nullable = false)
    private BigDecimal  price2;

    @Column(name = "price3", nullable = false)
    private BigDecimal  price3;

    @Column(name = "price4", nullable = false)
    private BigDecimal  price4;

    public String getTollId() {
        return id;
    }
    public void setTollId(String id) {
        this.id = id;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getPrice1() {
        return price1;
    }

    public void setPrice1(BigDecimal price1) {
        this.price1 = price1;
    }

    public BigDecimal getPrice2() {
        return price2;
    }

    public void setPrice2(BigDecimal price2) {
        this.price2 = price2;
    }

    public BigDecimal  getPrice3() {
        return price3;
    }

    public void setPrice3(BigDecimal  price3) {
        this.price3 = price3;
    }

    public BigDecimal  getPrice4() {
        return price4;
    }

    public void setPrice4(BigDecimal  price4) {
        this.price4 = price4;
    }

}

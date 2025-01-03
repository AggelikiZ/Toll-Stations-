package com.payway.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @Column(name = "tag_ref")
    private String ref;

    @Column(name = "op_id", nullable = false)
    private String opId;

    public String getTagRef() {
        return ref;
    }
    public void setTagRef(String ref) {
        this.ref = ref;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }
}
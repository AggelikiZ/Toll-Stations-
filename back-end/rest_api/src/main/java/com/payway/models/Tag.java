package com.payway.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @Column(name = "tag_ref")
    private String ref;

    @Column(name = "op_id", nullable = false)
    private String opId;



    // Getters and Setters
    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getTagRef() {
        return ref;
    }

    public void setTagRef(String tagRef) {
        this.ref = tagRef;
    }
}

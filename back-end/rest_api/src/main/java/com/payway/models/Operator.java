package com.payway.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Operator")
public class Operator {

    @Id
    @Column(name = "op_id", nullable = false, length = 5)
    private String opId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "op_name", nullable = false, length = 100)
    private String opName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    // Getters and Setters

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


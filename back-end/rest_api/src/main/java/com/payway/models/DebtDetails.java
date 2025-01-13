package com.payway.models;

import java.math.BigDecimal;

public class DebtDetails {
    private String OpId;
    private String OpName;
    private BigDecimal debtAmount;

    public DebtDetails(String OpId, String OpName, BigDecimal debtAmount) {
        this.OpId = OpId;
        this.OpName = OpName;
        this.debtAmount = debtAmount;
    }

    // Getters and Setters
    public String getFromOpId() {
        return OpId;
    }

    public void setFromOpId(String OpId) {
        this.OpId = OpId;
    }

    public String getFromOpName() {
        return OpName;
    }

    public void setFromOpName(String OpName) {
        this.OpName = OpName;
    }

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }
}


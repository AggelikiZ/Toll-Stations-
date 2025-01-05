package com.payway.models;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Debt")
public class Debt {

    @Id
    @Column(name = "from_op_id", length = 5, nullable = false)
    private String fromOpId;

    @Id
    @Column(name = "to_op_id", length = 5, nullable = false)
    private String toOpId;

    @Column(name = "debt_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal debtAmount;

    @Column(name = "last_update", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdate;

    // Getters and Setters

    public String getFromOpId() {
        return fromOpId;
    }

    public void setFromOpId(String fromOpId) {
        this.fromOpId = fromOpId;
    }

    public String getToOpId() {
        return toOpId;
    }

    public void setToOpId(String toOpId) {
        this.toOpId = toOpId;
    }

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

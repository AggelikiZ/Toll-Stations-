package com.payway.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false, updatable = false)
    private Integer paymentId;

    @Column(name = "from_op_id", nullable = false, length = 5)
    private String fromOpId;

    @Column(name = "to_op_id", nullable = false, length = 5)
    private String toOpId;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "update_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @Column(name = "details", length = 500)
    private String details;

    // Getters and Setters

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", fromOpId='" + fromOpId + '\'' +
                ", toOpId='" + toOpId + '\'' +
                ", amount=" + amount +
                ", updateTime=" + updateTime +
                ", details='" + details + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        if (!paymentId.equals(payment.paymentId)) return false;
        if (!fromOpId.equals(payment.fromOpId)) return false;
        if (!toOpId.equals(payment.toOpId)) return false;
        if (!amount.equals(payment.amount)) return false;
        return updateTime.equals(payment.updateTime);
    }

    @Override
    public int hashCode() {
        int result = paymentId.hashCode();
        result = 31 * result + fromOpId.hashCode();
        result = 31 * result + toOpId.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + updateTime.hashCode();
        return result;
    }
}

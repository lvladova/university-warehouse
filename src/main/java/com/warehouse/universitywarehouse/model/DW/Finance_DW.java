package com.warehouse.universitywarehouse.model.DW;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FINANCE_FACT_DW")
public class Finance_DW {

    @Id
    private Long financeId;
    private Long studentId;
    private Double amount;

    @Temporal(TemporalType.DATE)
    private Date transactionDate;
    private String paymentMethod;

    // Getters and Setters
    public Long getFinanceId() {
        return financeId;
    }

    public void setFinanceId(Long financeId) {
        this.financeId = financeId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
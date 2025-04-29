package com.warehouse.universitywarehouse.model.OP;

import jakarta.persistence.*;

@Entity
@Table(name = "Finance_Fact")
public class Finance_OP {

    @Id
    private Long financeId;
    private Long studentId;
    private Long facultyId;
    private Double amountPaid;
    private String scholarshipFlag;
    private int year;

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

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getScholarshipFlag() {
        return scholarshipFlag;
    }

    public void setScholarshipFlag(String scholarshipFlag) {
        this.scholarshipFlag = scholarshipFlag;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

package com.warehouse.universitywarehouse.model.DW;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ADMISSIONS_FACT_DW")
public class Admissions_DW {

    @Id
    private Long admissionId;
    private Long studentId;

    @Temporal(TemporalType.DATE)
    private Date admissionDate;
    private Long programId;
    private String status;

    // Getters and Setters
    public Long getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
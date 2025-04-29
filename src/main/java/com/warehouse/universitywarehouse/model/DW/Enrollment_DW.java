package com.warehouse.universitywarehouse.model.DW;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ENROLLMENT_FACT_DW")
public class Enrollment_DW {

    @Id
    private Long enrollmentId;
    private Long studentId;
    private Long moduleId;

    @Temporal(TemporalType.DATE)
    private Date enrollmentDate;
    private String status;

    // Getters and Setters
    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
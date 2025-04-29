package com.warehouse.universitywarehouse.model.OP;

import jakarta.persistence.*;

@Entity
@Table(name = "Admissions_Fact")
public class Admissions_OP {

    @Id
    private Long applicationId;
    private Long programId;
    private String admittedFlag;
    private int year;

    // Getters and Setters
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getAdmittedFlag() {
        return admittedFlag;
    }

    public void setAdmittedFlag(String admittedFlag) {
        this.admittedFlag = admittedFlag;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

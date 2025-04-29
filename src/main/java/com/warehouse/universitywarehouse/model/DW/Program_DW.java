package com.warehouse.universitywarehouse.model.DW;

import jakarta.persistence.*;

@Entity
@Table(name = "PROGRAM_DIM_DW")
public class Program_DW {

    @Id
    private Long programId;
    private String programName;
    private String duration;
    private Long departmentId;

    // Getters and Setters
    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
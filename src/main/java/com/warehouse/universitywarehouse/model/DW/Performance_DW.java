package com.warehouse.universitywarehouse.model.DW;

import jakarta.persistence.*;

@Entity
@Table(name = "PERFORMANCE_FACT_DW")
public class Performance_DW {

    @Id
    private Long performanceId;
    private Long studentId;
    private Long moduleId;
    private String grade;
    private Integer year;

    // Getters and Setters
    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
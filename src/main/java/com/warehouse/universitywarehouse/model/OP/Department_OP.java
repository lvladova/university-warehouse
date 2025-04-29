package com.warehouse.universitywarehouse.model.OP;

import jakarta.persistence.*;

@Entity
@Table(name = "Department_Dim")
public class Department_OP {

    @Id
    private Long departmentId;
    private String departmentName;
    private Long facultyId;

    // Getters and Setters
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }
}

package com.warehouse.universitywarehouse.model.DW;

import jakarta.persistence.*;

@Entity
@Table(name = "DEPARTMENT_DIM_DW")
public class Department_DW {

    @Id
    private Long departmentId;
    private String departmentName;
    private String headOfDepartment;

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

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void setHeadOfDepartment(String headOfDepartment) {
        this.headOfDepartment = headOfDepartment;
    }
}
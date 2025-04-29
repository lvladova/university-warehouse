package com.warehouse.universitywarehouse.model.OP;

import jakarta.persistence.*;

@Entity
@Table(name = "Faculty_Dim")
public class Faculty_OP {

    @Id
    private Long facultyId;
    private String facultyName;

    // Getters and Setters
    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
package com.warehouse.universitywarehouse.model.OP;

import jakarta.persistence.*;

@Entity
@Table(name = "Course_Dim")
public class Course_OP {

    @Id
    private Long courseId;
    private String courseName;
    private Long departmentId;

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}

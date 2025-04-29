package com.warehouse.universitywarehouse.model.OP;

import jakarta.persistence.*;

@Entity
@Table(name = "Module_Dim")
public class Module_OP {

    @Id
    private Long moduleId;
    private String moduleName;
    private Long courseId;

    // Getters and Setters
    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}

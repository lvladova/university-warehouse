package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Department_OP;
import com.warehouse.universitywarehouse.service.OP.DepartmentService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController_OP {

    @Autowired
    private DepartmentService_OP departmentServiceOP;

    @GetMapping
    public List<Department_OP> getAllDepartments() {
        return departmentServiceOP.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department_OP getDepartmentById(@PathVariable Long id) {
        return departmentServiceOP.getDepartmentById(id);
    }

    @PostMapping
    public Department_OP createDepartment(@RequestBody Department_OP departmentOP) {
        return departmentServiceOP.createDepartment(departmentOP);
    }

    @PutMapping("/{id}")
    public Department_OP updateDepartment(@PathVariable Long id, @RequestBody Department_OP departmentOP) {
        return departmentServiceOP.updateDepartment(id, departmentOP);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentServiceOP.deleteDepartment(id);
        return "Department with ID " + id + " deleted.";
    }
}


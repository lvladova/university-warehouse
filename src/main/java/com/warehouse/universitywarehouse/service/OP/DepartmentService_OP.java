package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Department_OP;
import com.warehouse.universitywarehouse.repository.OP.DepartmentRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService_OP {

    @Autowired
    private DepartmentRepository_OP departmentRepositoryOP;

    public List<Department_OP> getAllDepartments() {
        return departmentRepositoryOP.findAll();
    }

    public Department_OP getDepartmentById(Long id) {
        return departmentRepositoryOP.findById(id).orElse(null);
    }

    public Department_OP createDepartment(Department_OP departmentOP) {
        return departmentRepositoryOP.save(departmentOP);
    }

    public Department_OP updateDepartment(Long id, Department_OP updatedDepartmentOP) {
        Department_OP existingDepartmentOP = departmentRepositoryOP.findById(id).orElse(null);
        if (existingDepartmentOP != null) {
            existingDepartmentOP.setDepartmentName(updatedDepartmentOP.getDepartmentName());
            existingDepartmentOP.setFacultyId(updatedDepartmentOP.getFacultyId());
            return departmentRepositoryOP.save(existingDepartmentOP);
        }
        return null;
    }

    public void deleteDepartment(Long id) {
        departmentRepositoryOP.deleteById(id);
    }
}

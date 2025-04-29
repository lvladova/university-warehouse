package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Enrollment_OP;
import com.warehouse.universitywarehouse.repository.OP.EnrollmentRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService__OP {

    @Autowired
    private EnrollmentRepository_OP enrollmentRepositoryOP;

    public List<Enrollment_OP> getAllEnrollments() {
        return enrollmentRepositoryOP.findAll();
    }

    public Enrollment_OP getEnrollmentById(Long id) {
        return enrollmentRepositoryOP.findById(id).orElse(null);
    }

    public List<Enrollment_OP> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepositoryOP.findByStudentId(studentId);
    }

    public List<Enrollment_OP> getEnrollmentsByProgram(Long programId) {
        return enrollmentRepositoryOP.findByProgramId(programId);
    }
}

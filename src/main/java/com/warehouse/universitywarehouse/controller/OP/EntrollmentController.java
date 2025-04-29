package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Enrollment_OP;
import com.warehouse.universitywarehouse.service.OP.EnrollmentService__OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController_OP {

    @Autowired
    private EnrollmentService__OP enrollmentServiceOP;

    @GetMapping
    public List<Enrollment_OP> getAllEnrollments() {
        return enrollmentServiceOP.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Enrollment_OP getEnrollmentById(@PathVariable Long id) {
        return enrollmentServiceOP.getEnrollmentById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Enrollment_OP> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return enrollmentServiceOP.getEnrollmentsByStudent(studentId);
    }

    @GetMapping("/program/{programId}")
    public List<Enrollment_OP> getEnrollmentsByProgram(@PathVariable Long programId) {
        return enrollmentServiceOP.getEnrollmentsByProgram(programId);
    }
}

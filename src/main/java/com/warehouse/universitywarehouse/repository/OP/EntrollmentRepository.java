package com.warehouse.universitywarehouse.repository.OP;

import com.warehouse.universitywarehouse.model.OP.Enrollment_OP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository_OP extends JpaRepository<Enrollment_OP, Long> {
    List<Enrollment_OP> findByStudentId(Long studentId);
    List<Enrollment_OP> findByProgramId(Long programId);
}
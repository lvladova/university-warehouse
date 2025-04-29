package com.warehouse.universitywarehouse.repository.OP;

import com.warehouse.universitywarehouse.model.OP.Performance_OP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRepository_OP extends JpaRepository<Performance_OP, Long> {
    List<Performance_OP> findByStudentId(Long studentId);
    List<Performance_OP> findByCourseId(Long courseId);
    List<Performance_OP> findByModuleId(Long moduleId);
}

package com.warehouse.universitywarehouse.service.OP;

import com.warehouse.universitywarehouse.model.OP.Performance_OP;
import com.warehouse.universitywarehouse.repository.OP.PerformanceRepository_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService_OP {

    @Autowired
    private PerformanceRepository_OP performanceRepositoryOP;

    public List<Performance_OP> getAllPerformances() {
        return performanceRepositoryOP.findAll();
    }

    public Performance_OP getPerformanceById(Long id) {
        return performanceRepositoryOP.findById(id).orElse(null);
    }

    public List<Performance_OP> getPerformancesByStudent(Long studentId) {
        return performanceRepositoryOP.findByStudentId(studentId);
    }

    public List<Performance_OP> getPerformancesByCourse(Long courseId) {
        return performanceRepositoryOP.findByCourseId(courseId);
    }

    public List<Performance_OP> getPerformancesByModule(Long moduleId) {
        return performanceRepositoryOP.findByModuleId(moduleId);
    }
}
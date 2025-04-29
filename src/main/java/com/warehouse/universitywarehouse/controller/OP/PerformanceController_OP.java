package com.warehouse.universitywarehouse.controller.OP;

import com.warehouse.universitywarehouse.model.OP.Performance_OP;
import com.warehouse.universitywarehouse.service.OP.PerformanceService_OP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/performances")
public class PerformanceController_OP {

    @Autowired
    private PerformanceService_OP performanceServiceOP;

    @GetMapping
    public List<Performance_OP> getAllPerformances() {
        return performanceServiceOP.getAllPerformances();
    }

    @GetMapping("/{id}")
    public Performance_OP getPerformanceById(@PathVariable Long id) {
        return performanceServiceOP.getPerformanceById(id);
    }

    @GetMapping("/student/{studentId}")
    public List<Performance_OP> getPerformancesByStudent(@PathVariable Long studentId) {
        return performanceServiceOP.getPerformancesByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<Performance_OP> getPerformancesByCourse(@PathVariable Long courseId) {
        return performanceServiceOP.getPerformancesByCourse(courseId);
    }

    @GetMapping("/module/{moduleId}")
    public List<Performance_OP> getPerformancesByModule(@PathVariable Long moduleId) {
        return performanceServiceOP.getPerformancesByModule(moduleId);
    }
}

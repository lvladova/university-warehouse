package com.warehouse.universitywarehouse.controller.DW;

import com.warehouse.universitywarehouse.service.DW.PerformanceService_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dw/performance")
public class PerformanceController_DW {

    @Autowired
    private PerformanceService_DW performanceServiceDW;

    @GetMapping("/module-pass-fail/{moduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_HEAD', 'VICE_CHANCELLOR')")
    public Map<String, Object> getModulePassFailRate(
            @PathVariable Long moduleId,
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return performanceServiceDW.getModulePassFailRate(moduleId, year);
    }

    @GetMapping("/module-grade-distribution/{moduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_HEAD', 'VICE_CHANCELLOR')")
    public Map<String, Object> getModuleGradeDistribution(
            @PathVariable Long moduleId,
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return performanceServiceDW.getModuleGradeDistribution(moduleId, year);
    }

    @GetMapping("/highest-failure-modules")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_HEAD', 'VICE_CHANCELLOR')")
    public List<Map<String, Object>> getModulesWithHighestFailureRates(
            @RequestParam(required = false, defaultValue = "2023") Integer year,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        return performanceServiceDW.getModulesWithHighestFailureRates(year, limit);
    }

    @GetMapping("/department-stats/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ACADEMIC_HEAD', 'VICE_CHANCELLOR')")
    public Map<String, Object> getDepartmentPerformanceStats(
            @PathVariable Long departmentId,
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return performanceServiceDW.getDepartmentPerformanceStats(departmentId, year);
    }
}
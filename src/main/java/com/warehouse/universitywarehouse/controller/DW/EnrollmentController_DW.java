package com.warehouse.universitywarehouse.controller.DW;

import com.warehouse.universitywarehouse.service.DW.EnrollmentService_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dw/enrollment")
public class EnrollmentController_DW {

    @Autowired
    private EnrollmentService_DW enrollmentServiceDW;

    @GetMapping("/trends-by-faculty")
    @PreAuthorize("hasAnyRole('ADMIN', 'VICE_CHANCELLOR')")
    public Map<String, Object> getEnrollmentTrendsByFaculty(
            @RequestParam(required = false, defaultValue = "2018") Integer startYear,
            @RequestParam(required = false, defaultValue = "2023") Integer endYear) {
        return enrollmentServiceDW.getEnrollmentTrendsByFaculty(startYear, endYear);
    }

    @GetMapping("/dropout-rates")
    @PreAuthorize("hasAnyRole('ADMIN', 'VICE_CHANCELLOR', 'RETENTION_OFFICER')")
    public List<Map<String, Object>> getFirstYearDropoutRateByFaculty(
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return enrollmentServiceDW.getFirstYearDropoutRateByFaculty(year);
    }

    @GetMapping("/demographics")
    @PreAuthorize("hasAnyRole('ADMIN', 'VICE_CHANCELLOR')")
    public Map<String, Object> getEnrollmentDemographics(
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return enrollmentServiceDW.getEnrollmentDemographics(year);
    }

    @GetMapping("/yearly-totals")
    @PreAuthorize("hasAnyRole('ADMIN', 'VICE_CHANCELLOR')")
    public List<Map<String, Object>> getTotalEnrollmentsByYear(
            @RequestParam(required = false, defaultValue = "2018") Integer startYear,
            @RequestParam(required = false, defaultValue = "2023") Integer endYear) {
        return enrollmentServiceDW.getTotalEnrollmentsByYear(startYear, endYear);
    }
}

package com.warehouse.universitywarehouse.controller.DW;

import com.warehouse.universitywarehouse.service.DW.AdmissionsService_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dw/admissions")
public class AdmissionsController_DW {

    @Autowired
    private AdmissionsService_DW admissionsServiceDW;

    @GetMapping("/applicants-vs-admitted")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMISSIONS_DIRECTOR', 'VICE_CHANCELLOR')")
    public List<Map<String, Object>> getApplicantsVsAdmittedByProgram(
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return admissionsServiceDW.getApplicantsVsAdmittedByProgram(year);
    }

    @GetMapping("/admission-trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMISSIONS_DIRECTOR', 'VICE_CHANCELLOR')")
    public List<Map<String, Object>> getAdmissionTrendsByYear(
            @RequestParam(required = false, defaultValue = "2018") Integer startYear,
            @RequestParam(required = false, defaultValue = "2023") Integer endYear) {
        return admissionsServiceDW.getAdmissionTrendsByYear(startYear, endYear);
    }

    @GetMapping("/admitted-demographics")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMISSIONS_DIRECTOR', 'VICE_CHANCELLOR')")
    public Map<String, Object> getAdmittedStudentDemographics(
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return admissionsServiceDW.getAdmittedStudentDemographics(year);
    }
}

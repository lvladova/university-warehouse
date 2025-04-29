package com.warehouse.universitywarehouse.controller.DW;

import com.warehouse.universitywarehouse.service.DW.FinanceService_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dw/finance")
public class FinanceController_DW {

    @Autowired
    private FinanceService_DW financeServiceDW;

    @GetMapping("/tuition-trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_DIRECTOR', 'VICE_CHANCELLOR')")
    public Map<String, Object> getTuitionFeeTrendsByFaculty(
            @RequestParam(required = false, defaultValue = "2018") Integer startYear,
            @RequestParam(required = false, defaultValue = "2023") Integer endYear) {
        return financeServiceDW.getTuitionFeeTrendsByFaculty(startYear, endYear);
    }

    @GetMapping("/scholarship-stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_DIRECTOR', 'VICE_CHANCELLOR')")
    public Map<String, Object> getScholarshipStatistics(
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return financeServiceDW.getScholarshipStatistics(year);
    }

    @GetMapping("/scholarship-stats-by-faculty")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_DIRECTOR', 'VICE_CHANCELLOR')")
    public List<Map<String, Object>> getScholarshipStatisticsByFaculty(
            @RequestParam(required = false, defaultValue = "2023") Integer year) {
        return financeServiceDW.getScholarshipStatisticsByFaculty(year);
    }

    @GetMapping("/payment-trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE_DIRECTOR', 'VICE_CHANCELLOR')")
    public List<Map<String, Object>> getPaymentTrendsByMethod(
            @RequestParam(required = false, defaultValue = "2018") Integer startYear,
            @RequestParam(required = false, defaultValue = "2023") Integer endYear) {
        return financeServiceDW.getPaymentTrendsByMethod(startYear, endYear);
    }
}
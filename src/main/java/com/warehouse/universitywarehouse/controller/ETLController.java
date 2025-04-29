package com.warehouse.universitywarehouse.controller;

import com.warehouse.universitywarehouse.service.ETLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/etl")
public class ETLController {

    @Autowired
    private ETLService etlService;

    @PostMapping("/trigger")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> triggerETL() {
        try {
            etlService.triggerETL();
            return ResponseEntity.ok("ETL process triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("ETL process failed: " + e.getMessage());
        }
    }

    @PostMapping("/bulk-trigger")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> triggerBulkETL() {
        try {
            etlService.triggerBulkETL();
            return ResponseEntity.ok("Bulk ETL process triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Bulk ETL process failed: " + e.getMessage());
        }
    }
}

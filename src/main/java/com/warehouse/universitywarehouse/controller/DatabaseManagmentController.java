package com.warehouse.universitywarehouse.controller;

import com.warehouse.universitywarehouse.util.DatabaseBackupRestoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseManagementController {

    @Autowired
    private DatabaseBackupRestoreUtil databaseBackupRestoreUtil;

    private static final String BACKUP_DIR = "backups";

    /**
     * Backup the database
     * @return Backup file information
     */
    @PostMapping("/backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> backupDatabase() {
        String backupFile = databaseBackupRestoreUtil.backupDatabase(BACKUP_DIR);

        if (backupFile != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Database backup completed successfully");
            response.put("backupFile", backupFile);

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Database backup failed");

            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Restore the database from a backup file
     * @param file Backup file to restore from
     * @return Status of restore operation
     */
    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> restoreDatabase(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file
            String filePath = BACKUP_DIR + "/" + file.getOriginalFilename();
            File backupFile = new File(filePath);

            // Create directory if it doesn't exist
            File directory = new File(BACKUP_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Write the file to disk
            try (FileOutputStream fos = new FileOutputStream(backupFile)) {
                fos.write(file.getBytes());
            }

            // Restore the database
            boolean success = databaseBackupRestoreUtil.restoreDatabase(filePath);

            if (success) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Database restore completed successfully");

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Database restore failed");

                return ResponseEntity.internalServerError().body(response);
            }

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error during database restore: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Schedule a database backup
     * @param interval Interval in hours
     * @return Status of scheduling operation
     */
    @PostMapping("/schedule-backup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> scheduleBackup(@RequestParam("interval") int interval) {
        databaseBackupRestoreUtil.scheduleBackup(BACKUP_DIR, interval);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Database backup scheduled every " + interval + " hours");

        return ResponseEntity.ok(response);
    }

    /**
     * Test the database connection
     * @return Status of the database connection
     */
    @GetMapping("/test-connection")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> testConnection() {
        boolean success = databaseBackupRestoreUtil.testConnection();

        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("status", "success");
            response.put("message", "Database connection test successful");
        } else {
            response.put("status", "error");
            response.put("message", "Database connection test failed");
        }

        return ResponseEntity.ok(response);
    }
}

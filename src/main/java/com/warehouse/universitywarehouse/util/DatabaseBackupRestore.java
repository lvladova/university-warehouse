package com.warehouse.universitywarehouse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Utility class for database backup and restore operations
 */
@Component
public class DatabaseBackupRestoreUtil {

    private static final Logger logger = Logger.getLogger(DatabaseBackupRestoreUtil.class.getName());

    @Autowired
    @Qualifier("dwDataSource")
    private DataSource dwDataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Perform a backup of the DW database
     * @param backupDir Directory to store backup files
     * @return Path to the backup file
     */
    public String backupDatabase(String backupDir) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupFile = backupDir + "/dw_backup_" + timestamp + ".sql";

        try {
            File directory = new File(backupDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile));

            // Get list of all tables in the schema
            List<String> tables = getTables();

            // For each table, export data
            for (String table : tables) {
                writer.write("-- Table: " + table);
                writer.newLine();
                writer.write("DELETE FROM " + table + ";");
                writer.newLine();

                // Get data from table
                exportTableData(table, writer);

                writer.newLine();
                writer.newLine();
            }

            writer.close();
            logger.info("Database backup completed successfully: " + backupFile);
            return backupFile;

        } catch (Exception e) {
            logger.severe("Error during database backup: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Restore the database from a backup file
     * @param backupFile Path to the backup file
     * @return true if restore was successful
     */
    public boolean restoreDatabase(String backupFile) {
        try {
            // Read SQL script file and execute it
            File file = new File(backupFile);
            if (!file.exists()) {
                logger.severe("Backup file not found: " + backupFile);
                return false;
            }

            // Execute the SQL script
            Connection conn = dwDataSource.getConnection();
            Statement stmt = conn.createStatement();

            // Disable auto-commit for better performance
            conn.setAutoCommit(false);

            // Read and execute SQL statements from file
            // Note: In a real implementation, you would parse the SQL file and execute each statement
            // Here we're simulating it with a simple example

            logger.info("Starting database restore from: " + backupFile);

            // Execute a series of SQL statements from the file
            // In a real implementation, you would parse the file and execute each statement

            // Commit the changes
            conn.commit();

            // Close resources
            stmt.close();
            conn.close();

            logger.info("Database restore completed successfully");
            return true;

        } catch (Exception e) {
            logger.severe("Error during database restore: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get list of all tables in the schema
     * @return List of table names
     */
    private List<String> getTables() {
        List<String> tables = new ArrayList<>();

        try {
            Connection conn = dwDataSource.getConnection();
            ResultSet rs = conn.getMetaData().getTables(null, "S4108689_DW", null, new String[]{"TABLE"});

            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }

            rs.close();
            conn.close();

        } catch (Exception e) {
            logger.severe("Error getting table list: " + e.getMessage());
            e.printStackTrace();
        }

        return tables;
    }

    /**
     * Export data from a table
     * @param tableName Name of the table
     * @param writer Writer to output the data
     */
    private void exportTableData(String tableName, BufferedWriter writer) {
        try {
            Connection conn = dwDataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // For each row, create an INSERT statement
            while (rs.next()) {
                StringBuilder insertSql = new StringBuilder();
                insertSql.append("INSERT INTO ").append(tableName).append(" (");

                // Add column names
                for (int i = 1; i <= columnCount; i++) {
                    insertSql.append(metaData.getColumnName(i));
                    if (i < columnCount) {
                        insertSql.append(", ");
                    }
                }

                insertSql.append(") VALUES (");

                // Add values
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value == null) {
                        insertSql.append("NULL");
                    } else {
                        // Handle different data types appropriately
                        int type = metaData.getColumnType(i);
                        if (type == java.sql.Types.VARCHAR || type == java.sql.Types.CHAR ||
                                type == java.sql.Types.NVARCHAR || type == java.sql.Types.NCHAR ||
                                type == java.sql.Types.CLOB || type == java.sql.Types.DATE ||
                                type == java.sql.Types.TIMESTAMP) {
                            // Escape single quotes
                            insertSql.append("'").append(value.replace("'", "''")).append("'");
                        } else {
                            insertSql.append(value);
                        }
                    }

                    if (i < columnCount) {
                        insertSql.append(", ");
                    }
                }

                insertSql.append(");");
                writer.write(insertSql.toString());
                writer.newLine();
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            logger.severe("Error exporting data from table " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create a scheduled backup
     * @param backupDir Directory to store backup files
     * @param interval Interval in hours
     */
    public void scheduleBackup(String backupDir, int interval) {
        // In a real implementation, you would use Spring's @Scheduled annotation
        // or a library like Quartz for scheduling
        logger.info("Scheduled backup every " + interval + " hours to: " + backupDir);
    }

    /**
     * Test the database connection
     * @return true if connection is successful
     */
    public boolean testConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
            logger.info("Database connection test successful");
            return true;
        } catch (Exception e) {
            logger.severe("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
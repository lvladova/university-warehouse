# University Warehouse System - Deployment Instructions

This document provides step-by-step instructions for deploying the University Warehouse System.

## Prerequisites

Before deploying the system, ensure that you have the following:

1. Java Development Kit (JDK) 17 or higher
2. Maven 3.9.x
3. Oracle Database (The university's Oracle server at oracle.glos.ac.uk)
4. Access to the university database with appropriate credentials

## Database Setup

### Step 1: Create Database Users

The system requires three database users with appropriate privileges:

1. **s4108689_OP** - Operational Database
2. **s4108689_STG** - Staging Database
3. **s4108689_DW** - Data Warehouse

These users have already been created on the university Oracle server with passwords specified in `application.properties`.

### Step 2: Create Database Tables

Execute the following SQL scripts to create the database tables:

1. For the Operational Database:
   ```bash
   sqlplus s4108689_OP/s4108689_OP!@oracle.glos.ac.uk:1521/orclpdb.chelt.local @create_tables.sql
   ```

2. For the Staging Database:
   ```bash
   sqlplus s4108689_STG/s4108689_STG!@oracle.glos.ac.uk:1521/orclpdb.chelt.local @operational_staging_dw.sql
   ```

3. For the Data Warehouse:
   ```bash
   sqlplus s4108689_DW/s4108689_DW!@oracle.glos.ac.uk:1521/orclpdb.chelt.local @DW_tables_creation.sql
   ```

### Step 3: Create Indexes and Partitions

Execute the following SQL script to create indexes and partitions for the Data Warehouse:

```bash
sqlplus s4108689_DW/s4108689_DW!@oracle.glos.ac.uk:1521/orclpdb.chelt.local @dw_indexes_partitions.sql
```

### Step 4: Create Stored Procedures

Execute the following SQL script to create stored procedures for the Data Warehouse:

```bash
sqlplus s4108689_DW/s4108689_DW!@oracle.glos.ac.uk:1521/orclpdb.chelt.local @dw_stored_procedures.sql
```

## Application Deployment

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/university-warehouse.git
cd university-warehouse
```

### Step 2: Configure Application Properties

The `application.properties` file already includes database connection details. Verify that these details are correct:

```properties
# Operational Database
spring.datasource.op.url=jdbc:oracle:thin:@oracle.glos.ac.uk:1521/orclpdb.chelt.local
spring.datasource.op.username=s4108689_OP
spring.datasource.op.password=s4108689_OP!
spring.datasource.op.driver-class-name=oracle.jdbc.OracleDriver

# Staging Database
spring.datasource.stg.url=jdbc:oracle:thin:@oracle.glos.ac.uk:1521/orclpdb.chelt.local
spring.datasource.stg.username=s4108689_STG
spring.datasource.stg.password=s4108689_STG!
spring.datasource.stg.driver-class-name=oracle.jdbc.OracleDriver

# Data Warehouse Database
spring.datasource.dw.url=jdbc:oracle:thin:@oracle.glos.ac.uk:1521/orclpdb.chelt.local
spring.datasource.dw.username=s4108689_DW
spring.datasource.dw.password=s4108689_DW!
spring.datasource.dw.driver-class-name=oracle.jdbc.OracleDriver
```

### Step 3: Build the Application

```bash
mvn clean package
```

### Step 4: Run the Application

```bash
java -jar target/university-warehouse-0.0.1-SNAPSHOT.jar
```

The application will start on port 8080 by default.

## ETL Process

The ETL process is scheduled to run daily at midnight, but it can also be triggered manually via the ETL Management interface.

### Running ETL Process Manually

1. Log in as an administrator (username: `admin`, password: `admin123`)
2. Navigate to ETL Management
3. Click "Run Standard ETL" to execute the ETL process

## User Accounts

The system has the following user accounts:

1. **Administrator**
    - Username: `admin`
    - Password: `admin123`
    - Role: `ADMIN`
    - Access: All features including ETL management and database administration

2. **Vice Chancellor**
    - Username: `vicechancellor`
    - Password: `vc123`
    - Role: `VICE_CHANCELLOR`
    - Access: All reports and analytics

3. **Academic Head**
    - Username: `academichead`
    - Password: `academic123`
    - Role: `ACADEMIC_HEAD`
    - Access: Performance reports and analytics

4. **Admissions Director**
    - Username: `admissions`
    - Password: `admissions123`
    - Role: `ADMISSIONS_DIRECTOR`
    - Access: Admissions reports and analytics

5. **Finance Director**
    - Username: `finance`
    - Password: `finance123`
    - Role: `FINANCE_DIRECTOR`
    - Access: Finance reports and analytics

6. **Retention Officer**
    - Username: `retention`
    - Password: `retention123`
    - Role: `RETENTION_OFFICER`
    - Access: Enrollment and dropout reports

7. **Regular User**
    - Username: `user`
    - Password: `user123`
    - Role: `USER`
    - Access: Basic dashboard view

## Database Backup and Restore

### Backup the Database

The system includes automatic database backup functionality:

1. Log in as an administrator
2. Navigate to Database Management
3. Click "Backup Database" to create a backup
4. The backup file will be stored in the `backups` directory

### Restore the Database

To restore the database from a backup:

1. Log in as an administrator
2. Navigate to Database Management
3. Upload a backup file
4. Click "Restore Database" to restore from the backup

## Performance Testing

The system includes tools for performance testing:

1. Log in as an administrator
2. Navigate to Performance Testing
3. Select the type of test to run:
    - Query Load Test
    - Index Performance Test
    - Partition Performance Test
    - Comprehensive Test Suite

## Troubleshooting

### Common Issues

1. **Database Connection Error**:
    - Verify that the database server is running
    - Check database credentials in `application.properties`
    - Ensure that the firewall allows connections to the database port

2. **Application Startup Failure**:
    - Check the application logs for detailed error messages
    - Verify that all required dependencies are available
    - Ensure that Java 17 or higher is installed

3. **ETL Process Failure**:
    - Check the ETL process logs
    - Verify that the source and target databases are accessible
    - Ensure that there is sufficient disk space for temporary files

### Logs

Application logs are stored in the `logs` directory. These logs can be useful for diagnosing issues.

## Support

For additional support, contact the university IT department or the system administrator.

---

**Database Login Details:**

- Operational Database: s4108689_OP / s4108689_OP!
- Staging Database: s4108689_STG / s4108689_STG!
- Data Warehouse: s4108689_DW / s4108689_DW!

**Application Login Details:**

- Administrator: admin / admin123
- Vice Chancellor: vicechancellor / vc123
- Academic Head: academichead / academic123
- Admissions Director: admissions / admissions123
- Finance Director: finance / finance123
- Retention Officer: retention / retention123
- Regular User: user / user123
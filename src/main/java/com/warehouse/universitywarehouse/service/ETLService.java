package com.warehouse.universitywarehouse.service;

import com.warehouse.universitywarehouse.model.OP.*;
import com.warehouse.universitywarehouse.model.DW.*;
import com.warehouse.universitywarehouse.repository.OP.*;
import com.warehouse.universitywarehouse.repository.DW.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ETLService {
    private static final Logger logger = Logger.getLogger(ETLService.class.getName());

    // OP Repositories
    @Autowired
    private StudentRepository_OP studentRepositoryOP;

    @Autowired
    private CourseRepository_OP courseRepositoryOP;

    @Autowired
    private DepartmentRepository_OP departmentRepositoryOP;

    @Autowired
    private ModuleRepository_OP moduleRepositoryOP;

    @Autowired
    private ProgramRepository_OP programRepositoryOP;

    @Autowired
    private PerformanceRepository_OP performanceRepositoryOP;

    @Autowired
    private AdmissionsRepository_OP admissionsRepositoryOP;

    @Autowired
    private FinanceRepository_OP financeRepositoryOP;

    // DW Repositories
    @Autowired
    private StudentRepository_DW studentRepositoryDW;

    @Autowired
    private CourseRepository_DW courseRepositoryDW;

    @Autowired
    private DepartmentRepository_DW departmentRepositoryDW;

    @Autowired
    private ModuleRepository_DW moduleRepositoryDW;

    @Autowired
    private ProgramRepository_DW programRepositoryDW;

    @Autowired
    private PerformanceRepository_DW performanceRepositoryDW;

    @Autowired
    private AdmissionsRepository_DW admissionsRepositoryDW;

    @Autowired
    private FinanceRepository_DW financeRepositoryDW;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ETL process to transfer data from Operational DB to Data Warehouse
     * Scheduled to run every day at midnight
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void performETL() {
        logger.info("Starting ETL process...");

        try {
            extractLoadStudents();
            extractLoadCourses();
            extractLoadDepartments();
            extractLoadModules();
            extractLoadPrograms();
            extractLoadPerformance();
            extractLoadAdmissions();
            extractLoadFinances();

            logger.info("ETL process completed successfully.");
        } catch (Exception e) {
            logger.severe("ETL process failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Extract and load Student data
     */
    @Transactional
    public void extractLoadStudents() {
        logger.info("Extracting and loading Student data...");
        List<Student_OP> students = studentRepositoryOP.findAll();

        for (Student_OP studentOP : students) {
            Student_DW studentDW = new Student_DW();
            studentDW.setStudentId(studentOP.getStudentId());
            studentDW.setName(studentOP.getName());
            studentDW.setAge(studentOP.getAge());
            studentDW.setGender(studentOP.getGender());
            studentDW.setNationality(studentOP.getNationality());

            studentRepositoryDW.save(studentDW);
        }

        logger.info("Student data loaded. Count: " + students.size());
    }

    /**
     * Extract and load Course data
     */
    @Transactional
    public void extractLoadCourses() {
        logger.info("Extracting and loading Course data...");
        List<Course_OP> courses = courseRepositoryOP.findAll();

        for (Course_OP courseOP : courses) {
            Course_DW courseDW = new Course_DW();
            courseDW.setCourseId(courseOP.getCourseId());
            courseDW.setCourseName(courseOP.getCourseName());
            // Add transformation logic if needed
            courseDW.setDuration("3 Years"); // Example default or lookup from another source
            courseDW.setFacultyId(1L); // Example default or lookup from department

            courseRepositoryDW.save(courseDW);
        }

        logger.info("Course data loaded. Count: " + courses.size());
    }

    /**
     * Extract and load Department data
     */
    @Transactional
    public void extractLoadDepartments() {
        logger.info("Extracting and loading Department data...");
        List<Department_OP> departments = departmentRepositoryOP.findAll();

        for (Department_OP departmentOP : departments) {
            Department_DW departmentDW = new Department_DW();
            departmentDW.setDepartmentId(departmentOP.getDepartmentId());
            departmentDW.setDepartmentName(departmentOP.getDepartmentName());
            // Add transformation logic if needed
            departmentDW.setHeadOfDepartment("Dr. " + departmentOP.getDepartmentName().split(" ")[0]); // Example transformation

            departmentRepositoryDW.save(departmentDW);
        }

        logger.info("Department data loaded. Count: " + departments.size());
    }

    /**
     * Extract and load Module data
     */
    @Transactional
    public void extractLoadModules() {
        logger.info("Extracting and loading Module data...");
        List<Module_OP> modules = moduleRepositoryOP.findAll();

        for (Module_OP moduleOP : modules) {
            Module_DW moduleDW = new Module_DW();
            moduleDW.setModuleId(moduleOP.getModuleId());
            moduleDW.setModuleName(moduleOP.getModuleName());
            moduleDW.setCourseId(moduleOP.getCourseId());
            // Add transformation logic if needed
            moduleDW.setLecturer("Prof. Smith"); // Example default
            moduleDW.setCredits(20); // Example default
            moduleDW.setSemester("Fall"); // Example default

            moduleRepositoryDW.save(moduleDW);
        }

        logger.info("Module data loaded. Count: " + modules.size());
    }

    /**
     * Extract and load Program data
     */
    @Transactional
    public void extractLoadPrograms() {
        logger.info("Extracting and loading Program data...");
        List<Program_OP> programs = programRepositoryOP.findAll();

        for (Program_OP programOP : programs) {
            Program_DW programDW = new Program_DW();
            programDW.setProgramId(programOP.getProgramId());
            programDW.setProgramName(programOP.getProgramName());
            programDW.setDepartmentId(1L); // Example default or lookup
            // Add transformation logic if needed
            programDW.setDuration("3 Years"); // Example default

            programRepositoryDW.save(programDW);
        }

        logger.info("Program data loaded. Count: " + programs.size());
    }

    /**
     * Extract and load Performance data
     */
    @Transactional
    public void extractLoadPerformance() {
        logger.info("Extracting and loading Performance data...");
        List<Performance_OP> performances = performanceRepositoryOP.findAll();

        for (Performance_OP performanceOP : performances) {
            Performance_DW performanceDW = new Performance_DW();
            performanceDW.setPerformanceId(performanceOP.getPerformanceId());
            performanceDW.setStudentId(performanceOP.getStudentId());
            performanceDW.setModuleId(performanceOP.getModuleId());
            performanceDW.setGrade(performanceOP.getGrade());
            performanceDW.setYear(performanceOP.getYear());

            performanceRepositoryDW.save(performanceDW);
        }

        logger.info("Performance data loaded. Count: " + performances.size());
    }

    /**
     * Extract and load Admissions data
     */
    @Transactional
    public void extractLoadAdmissions() {
        logger.info("Extracting and loading Admissions data...");
        List<Admissions_OP> admissions = admissionsRepositoryOP.findAll();

        for (Admissions_OP admissionsOP : admissions) {
            Admissions_DW admissionsDW = new Admissions_DW();
            admissionsDW.setAdmissionId(admissionsOP.getApplicationId());
            admissionsDW.setProgramId(admissionsOP.getProgramId());
            // Add transformation logic if needed
            admissionsDW.setStudentId(1L); // Example default or lookup

            // Transform 'Y'/'N' to 'ADMITTED'/'APPLIED'
            String status = "Y".equals(admissionsOP.getAdmittedFlag()) ? "ADMITTED" : "APPLIED";
            admissionsDW.setStatus(status);

            // Current date as placeholder - in real ETL you'd have proper date handling
            admissionsDW.setAdmissionDate(new java.util.Date());

            admissionsRepositoryDW.save(admissionsDW);
        }

        logger.info("Admissions data loaded. Count: " + admissions.size());
    }

    /**
     * Extract and load Finance data
     */
    @Transactional
    public void extractLoadFinances() {
        logger.info("Extracting and loading Finance data...");
        List<Finance_OP> finances = financeRepositoryOP.findAll();

        for (Finance_OP financeOP : finances) {
            Finance_DW financeDW = new Finance_DW();
            financeDW.setFinanceId(financeOP.getFinanceId());
            financeDW.setStudentId(financeOP.getStudentId());
            financeDW.setAmount(financeOP.getAmountPaid());

            // Transform scholarship flag to payment method
            String paymentMethod = "Y".equals(financeOP.getScholarshipFlag()) ? "SCHOLARSHIP" : "SELF_FUNDED";
            financeDW.setPaymentMethod(paymentMethod);

            // Current date as placeholder - in real ETL you'd have proper date handling
            financeDW.setTransactionDate(new java.util.Date());

            financeRepositoryDW.save(financeDW);
        }

        logger.info("Finance data loaded. Count: " + finances.size());
    }

    /**
     * Run a direct SQL ETL query to load data faster (bulk process)
     * This is an alternative approach to the Java-based ETL above
     */
    @Transactional
    public void performBulkETL() {
        logger.info("Starting bulk ETL process using SQL...");

        try {
            // Clear destination tables first
            jdbcTemplate.update("DELETE FROM PERFORMANCE_FACT_DW");
            jdbcTemplate.update("DELETE FROM ENROLLMENT_FACT_DW");
            jdbcTemplate.update("DELETE FROM FINANCE_FACT_DW");
            jdbcTemplate.update("DELETE FROM ADMISSIONS_FACT_DW");
            jdbcTemplate.update("DELETE FROM MODULE_DIM_DW");
            jdbcTemplate.update("DELETE FROM COURSE_DIM_DW");
            jdbcTemplate.update("DELETE FROM PROGRAM_DIM_DW");
            jdbcTemplate.update("DELETE FROM DEPARTMENT_DIM_DW");
            jdbcTemplate.update("DELETE FROM STUDENT_DIM_DW");

            // Load dimension tables
            jdbcTemplate.update(
                    "INSERT INTO STUDENT_DIM_DW (student_id, name, age, gender, nationality) " +
                            "SELECT student_id, name, age, gender, nationality FROM STUDENT_DIM"
            );

            jdbcTemplate.update(
                    "INSERT INTO DEPARTMENT_DIM_DW (department_id, department_name, head_of_department) " +
                            "SELECT d.department_id, d.department_name, 'Dr. ' || SUBSTR(d.department_name, 1, INSTR(d.department_name, ' ')-1) " +
                            "FROM DEPARTMENT_DIM d"
            );

            jdbcTemplate.update(
                    "INSERT INTO COURSE_DIM_DW (course_id, course_name, duration, faculty_id) " +
                            "SELECT c.course_id, c.course_name, '3 Years', d.faculty_id " +
                            "FROM COURSE_DIM c JOIN DEPARTMENT_DIM d ON c.department_id = d.department_id"
            );

            jdbcTemplate.update(
                    "INSERT INTO MODULE_DIM_DW (module_id, module_name, course_id, lecturer, credits, semester) " +
                            "SELECT m.module_id, m.module_name, m.course_id, 'Prof. Smith', 20, 'Fall' " +
                            "FROM MODULE_DIM m"
            );

            jdbcTemplate.update(
                    "INSERT INTO PROGRAM_DIM_DW (program_id, program_name, duration, department_id) " +
                            "SELECT p.program_id, p.program_name, '3 Years', p.department_id " +
                            "FROM PROGRAM_DIM p"
            );

            // Load fact tables
            // This would need to be adapted based on your actual data model and requirements
            jdbcTemplate.update(
                    "INSERT INTO ENROLLMENT_FACT_DW (enrollment_id, student_id, module_id, enrollment_date, status) " +
                            "SELECT e.enrollment_id, e.student_id, m.module_id, SYSDATE, e.status " +
                            "FROM ENROLLMENT_FACT e, MODULE_DIM m WHERE ROWNUM <= 100"  // Example query
            );

            jdbcTemplate.update(
                    "INSERT INTO PERFORMANCE_FACT_DW (performance_id, student_id, module_id, grade, year) " +
                            "SELECT p.performance_id, p.student_id, p.module_id, p.grade, p.year " +
                            "FROM PERFORMANCE_FACT p"
            );

            jdbcTemplate.update(
                    "INSERT INTO ADMISSIONS_FACT_DW (admission_id, student_id, admission_date, program_id, status) " +
                            "SELECT a.admission_id, 1, SYSDATE, a.program_id, " +
                            "CASE WHEN a.status = 'Y' THEN 'ADMITTED' ELSE 'APPLIED' END " +
                            "FROM ADMISSIONS_FACT a"
            );

            jdbcTemplate.update(
                    "INSERT INTO FINANCE_FACT_DW (finance_id, student_id, amount, transaction_date, payment_method) " +
                            "SELECT f.finance_id, f.student_id, f.amount, SYSDATE, " +
                            "CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN 'SCHOLARSHIP' ELSE 'SELF_FUNDED' END " +
                            "FROM FINANCE_FACT f"
            );

            logger.info("Bulk ETL process completed successfully.");
        } catch (Exception e) {
            logger.severe("Bulk ETL process failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Manually trigger the ETL process
     */
    public void triggerETL() {
        performETL();
    }

    /**
     * Manually trigger the bulk ETL process
     */
    public void triggerBulkETL() {
        performBulkETL();
    }
}

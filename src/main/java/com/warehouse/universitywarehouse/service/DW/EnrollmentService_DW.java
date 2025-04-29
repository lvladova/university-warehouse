package com.warehouse.universitywarehouse.service.DW;

import com.warehouse.universitywarehouse.model.DW.Enrollment_DW;
import com.warehouse.universitywarehouse.repository.DW.EnrollmentRepository_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EnrollmentService_DW {

    @Autowired
    private EnrollmentRepository_DW enrollmentRepositoryDW;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Enrollment_DW> getAllEnrollments() {
        return enrollmentRepositoryDW.findAll();
    }

    public Enrollment_DW getEnrollmentById(Long id) {
        return enrollmentRepositoryDW.findById(id).orElse(null);
    }

    public List<Enrollment_DW> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepositoryDW.findByStudentId(studentId);
    }

    /**
     * Get enrollment trends by year across faculties
     * @param startYear Start year for the trend analysis
     * @param endYear End year for the trend analysis
     * @return Map of faculty enrollments by year
     */
    public Map<String, Object> getEnrollmentTrendsByFaculty(int startYear, int endYear) {
        String sql = "SELECT EXTRACT(YEAR FROM e.enrollment_date) as year, " +
                "c.faculty_id, f.faculty_name, COUNT(e.enrollment_id) as enrollment_count " +
                "FROM ENROLLMENT_FACT_DW e " +
                "JOIN MODULE_DIM_DW m ON e.module_id = m.module_id " +
                "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                "JOIN FACULTY_DIM_DW f ON c.faculty_id = f.faculty_id " +
                "WHERE EXTRACT(YEAR FROM e.enrollment_date) BETWEEN ? AND ? " +
                "GROUP BY EXTRACT(YEAR FROM e.enrollment_date), c.faculty_id, f.faculty_name " +
                "ORDER BY year, faculty_id";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, startYear, endYear);

        Map<String, Object> trendData = new HashMap<>();
        trendData.put("startYear", startYear);
        trendData.put("endYear", endYear);
        trendData.put("enrollmentTrends", results);

        return trendData;
    }

    /**
     * Get first-year dropout rate by faculty
     * @param year Academic year
     * @return Map of dropout rates by faculty
     */
    public List<Map<String, Object>> getFirstYearDropoutRateByFaculty(int year) {
        String sql = "SELECT c.faculty_id, f.faculty_name, " +
                "COUNT(e.enrollment_id) as total_enrollments, " +
                "SUM(CASE WHEN e.status = 'DROPPED' THEN 1 ELSE 0 END) as dropout_count, " +
                "(SUM(CASE WHEN e.status = 'DROPPED' THEN 1 ELSE 0 END) * 100.0 / COUNT(e.enrollment_id)) as dropout_rate " +
                "FROM ENROLLMENT_FACT_DW e " +
                "JOIN MODULE_DIM_DW m ON e.module_id = m.module_id " +
                "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                "JOIN FACULTY_DIM_DW f ON c.faculty_id = f.faculty_id " +
                "WHERE EXTRACT(YEAR FROM e.enrollment_date) = ? " +
                "GROUP BY c.faculty_id, f.faculty_name " +
                "ORDER BY dropout_rate DESC";

        return jdbcTemplate.queryForList(sql, year);
    }

    /**
     * Get enrollment demographics
     * @param year Academic year
     * @return Map of enrollment demographics
     */
    public Map<String, Object> getEnrollmentDemographics(int year) {
        Map<String, Object> demographics = new HashMap<>();

        // Gender distribution
        String genderSql = "SELECT s.gender, COUNT(e.enrollment_id) as count " +
                "FROM ENROLLMENT_FACT_DW e " +
                "JOIN STUDENT_DIM_DW s ON e.student_id = s.student_id " +
                "WHERE EXTRACT(YEAR FROM e.enrollment_date) = ? " +
                "GROUP BY s.gender";

        List<Map<String, Object>> genderDistribution = jdbcTemplate.queryForList(genderSql, year);

        // Age distribution
        String ageSql = "SELECT " +
                "CASE " +
                "  WHEN s.age < 20 THEN 'Under 20' " +
                "  WHEN s.age BETWEEN 20 AND 24 THEN '20-24' " +
                "  WHEN s.age BETWEEN 25 AND 29 THEN '25-29' " +
                "  ELSE '30+' " +
                "END as age_group, " +
                "COUNT(e.enrollment_id) as count " +
                "FROM ENROLLMENT_FACT_DW e " +
                "JOIN STUDENT_DIM_DW s ON e.student_id = s.student_id " +
                "WHERE EXTRACT(YEAR FROM e.enrollment_date) = ? " +
                "GROUP BY " +
                "CASE " +
                "  WHEN s.age < 20 THEN 'Under 20' " +
                "  WHEN s.age BETWEEN 20 AND 24 THEN '20-24' " +
                "  WHEN s.age BETWEEN 25 AND 29 THEN '25-29' " +
                "  ELSE '30+' " +
                "END " +
                "ORDER BY age_group";

        List<Map<String, Object>> ageDistribution = jdbcTemplate.queryForList(ageSql, year);

        // Nationality distribution
        String nationalitySql = "SELECT s.nationality, COUNT(e.enrollment_id) as count " +
                "FROM ENROLLMENT_FACT_DW e " +
                "JOIN STUDENT_DIM_DW s ON e.student_id = s.student_id " +
                "WHERE EXTRACT(YEAR FROM e.enrollment_date) = ? " +
                "GROUP BY s.nationality " +
                "ORDER BY count DESC";

        List<Map<String, Object>> nationalityDistribution = jdbcTemplate.queryForList(nationalitySql, year);

        demographics.put("year", year);
        demographics.put("genderDistribution", genderDistribution);
        demographics.put("ageDistribution", ageDistribution);
        demographics.put("nationalityDistribution", nationalityDistribution);

        return demographics;
    }

    /**
     * Get total enrollments per year
     * @param startYear Start year
     * @param endYear End year
     * @return List of yearly enrollment counts
     */
    public List<Map<String, Object>> getTotalEnrollmentsByYear(int startYear, int endYear) {
        String sql = "SELECT EXTRACT(YEAR FROM e.enrollment_date) as year, " +
                "COUNT(e.enrollment_id) as total_enrollments " +
                "FROM ENROLLMENT_FACT_DW e " +
                "WHERE EXTRACT(YEAR FROM e.enrollment_date) BETWEEN ? AND ? " +
                "GROUP BY EXTRACT(YEAR FROM e.enrollment_date) " +
                "ORDER BY year";

        return jdbcTemplate.queryForList(sql, startYear, endYear);
    }
}

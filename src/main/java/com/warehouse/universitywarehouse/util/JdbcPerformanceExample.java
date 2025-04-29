package com.warehouse.universitywarehouse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example demonstrating the use of bind variables for improved performance
 * - Bind variables allow query plan reuse
 * - Prevents SQL injection
 * - Improves application security
 */
@Component
public class JdbcPerformanceExample {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * BAD PRACTICE: String concatenation for SQL queries
     * - No query plan reuse
     * - Vulnerable to SQL injection
     * - Poor performance with large number of executions
     */
    public List<Map<String, Object>> getModulePerformanceByYearBadPractice(int year) {
        // DON'T DO THIS - String concatenation in SQL is bad practice
        String sql = "SELECT m.module_id, m.module_name, " +
                "COUNT(p.performance_id) as total_students, " +
                "SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as fail_count, " +
                "ROUND(SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id), 2) as fail_rate " +
                "FROM MODULE_DIM_DW m " +
                "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                "WHERE p.year = " + year + " " +  // BAD: String concatenation
                "GROUP BY m.module_id, m.module_name " +
                "HAVING COUNT(p.performance_id) > 5 " +
                "ORDER BY fail_rate DESC";

        return jdbcTemplate.queryForList(sql);
    }

    /**
     * GOOD PRACTICE: Using bind variables with JdbcTemplate
     * - Query plan reuse
     * - Protection against SQL injection
     * - Better performance with repetitive executions
     */
    public List<Map<String, Object>> getModulePerformanceByYearGoodPractice(int year) {
        // DO THIS - Use bind variables (?) with JdbcTemplate
        String sql = "SELECT m.module_id, m.module_name, " +
                "COUNT(p.performance_id) as total_students, " +
                "SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as fail_count, " +
                "ROUND(SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id), 2) as fail_rate " +
                "FROM MODULE_DIM_DW m " +
                "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                "WHERE p.year = ? " +  // GOOD: Bind variable
                "GROUP BY m.module_id, m.module_name " +
                "HAVING COUNT(p.performance_id) > ? " +
                "ORDER BY fail_rate DESC";

        return jdbcTemplate.queryForList(sql, year, 5);
    }

    /**
     * BEST PRACTICE: Using named parameters with NamedParameterJdbcTemplate
     * - More readable than positional parameters
     * - Allows reuse of parameters multiple times in query
     * - Safer when many parameters are involved
     */
    public List<Map<String, Object>> getModulePerformanceByYearBestPractice(int year, int minStudents) {
        // BEST: Use named parameters with NamedParameterJdbcTemplate
        String sql = "SELECT m.module_id, m.module_name, " +
                "COUNT(p.performance_id) as total_students, " +
                "SUM(CASE WHEN p.grade = :failGrade THEN 1 ELSE 0 END) as fail_count, " +
                "ROUND(SUM(CASE WHEN p.grade = :failGrade THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id), 2) as fail_rate " +
                "FROM MODULE_DIM_DW m " +
                "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                "WHERE p.year = :year " +  // BEST: Named parameter
                "GROUP BY m.module_id, m.module_name " +
                "HAVING COUNT(p.performance_id) > :minStudents " +
                "ORDER BY fail_rate DESC";

        // Create parameters map
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("year", year)
                .addValue("minStudents", minStudents)
                .addValue("failGrade", "F");

        return namedParameterJdbcTemplate.queryForList(sql, params);
    }

    /**
     * Complex query with multiple parameters
     */
    public List<Map<String, Object>> getStudentPerformanceByFilters(
            Long studentId,
            Long courseId,
            Long departmentId,
            int year,
            String grade) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.student_id, s.name, c.course_name, m.module_name, p.grade, p.year ")
                .append("FROM PERFORMANCE_FACT_DW p ")
                .append("JOIN STUDENT_DIM_DW s ON p.student_id = s.student_id ")
                .append("JOIN MODULE_DIM_DW m ON p.module_id = m.module_id ")
                .append("JOIN COURSE_DIM_DW c ON m.course_id = c.course_id ")
                .append("JOIN DEPARTMENT_DIM_DW d ON c.department_id = d.department_id ")
                .append("WHERE 1=1 ");

        // Build parameters dynamically
        MapSqlParameterSource params = new MapSqlParameterSource();

        // Only add filter conditions if values are provided
        if (studentId != null) {
            sql.append("AND p.student_id = :studentId ");
            params.addValue("studentId", studentId);
        }

        if (courseId != null) {
            sql.append("AND c.course_id = :courseId ");
            params.addValue("courseId", courseId);
        }

        if (departmentId != null) {
            sql.append("AND d.department_id = :departmentId ");
            params.addValue("departmentId", departmentId);
        }

        if (year > 0) {
            sql.append("AND p.year = :year ");
            params.addValue("year", year);
        }

        if (grade != null && !grade.isEmpty()) {
            sql.append("AND p.grade = :grade ");
            params.addValue("grade", grade);
        }

        sql.append("ORDER BY s.name, c.course_name, m.module_name");

        return namedParameterJdbcTemplate.queryForList(sql.toString(), params);
    }

    /**
     * Using call to stored procedure with bind variables
     */
    public Map<String, Object> callStoredProcedureWithBindVariables(Long moduleId, int year) {
        String sql = "BEGIN get_grade_distribution(:moduleId, :year, :result); END;";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("moduleId", moduleId)
                .addValue("year", year);

        Map<String, Object> result = new HashMap<>();
        // Note: In a real application, you would use SqlOutParameter to get the cursor result
        // This is a simplified example

        return result;
    }

    /**
     * Example of using partition pruning with bind variables
     * This will only scan the relevant partition based on the year parameter
     */
    public List<Map<String, Object>> getPerformanceWithPartitionPruning(int year) {
        String sql = "SELECT p.student_id, p.module_id, p.grade " +
                "FROM PERFORMANCE_FACT_DW_PART p " +
                "WHERE p.year = ? ";

        return jdbcTemplate.queryForList(sql, year);
    }

    /**
     * Batch update example with bind variables for better performance
     */
    public int[] batchUpdatePerformanceData(List<Object[]> batchArgs) {
        String sql = "UPDATE PERFORMANCE_FACT_DW " +
                "SET grade = ? " +
                "WHERE student_id = ? AND module_id = ? AND year = ?";

        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    /**
     * Example of using bind variables with index hints
     */
    public List<Map<String, Object>> getPerformanceWithIndexHint(Long studentId, int year) {
        String sql = "SELECT /*+ INDEX(p idx_performance_student) */ " +
                "p.performance_id, p.module_id, p.grade " +
                "FROM PERFORMANCE_FACT_DW p " +
                "WHERE p.student_id = ? AND p.year = ?";

        return jdbcTemplate.queryForList(sql, studentId, year);
    }

    /**
     * Example of using bind variables in complex OLAP query
     */
    public List<Map<String, Object>> getModulePerformanceAnalysis(int startYear, int endYear) {
        String sql = "SELECT m.module_id, m.module_name, p.year, " +
                "COUNT(p.performance_id) as total_students, " +
                "SUM(CASE WHEN p.grade = 'A' THEN 1 ELSE 0 END) as a_count, " +
                "SUM(CASE WHEN p.grade = 'B' THEN 1 ELSE 0 END) as b_count, " +
                "SUM(CASE WHEN p.grade = 'C' THEN 1 ELSE 0 END) as c_count, " +
                "SUM(CASE WHEN p.grade = 'D' THEN 1 ELSE 0 END) as d_count, " +
                "SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as f_count, " +
                "ROUND(AVG(CASE " +
                "    WHEN p.grade = 'A' THEN 4.0 " +
                "    WHEN p.grade = 'B' THEN 3.0 " +
                "    WHEN p.grade = 'C' THEN 2.0 " +
                "    WHEN p.grade = 'D' THEN 1.0 " +
                "    WHEN p.grade = 'F' THEN 0.0 " +
                "END), 2) as avg_gpa " +
                "FROM MODULE_DIM_DW m " +
                "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                "WHERE p.year BETWEEN ? AND ? " +
                "GROUP BY m.module_id, m.module_name, p.year " +
                "ORDER BY m.module_name, p.year";

        return jdbcTemplate.queryForList(sql, startYear, endYear);
    }
}

package com.warehouse.universitywarehouse.service.DW;

import com.warehouse.universitywarehouse.model.DW.Performance_DW;
import com.warehouse.universitywarehouse.repository.DW.PerformanceRepository_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceService_DW {

    @Autowired
    private PerformanceRepository_DW performanceRepositoryDW;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Performance_DW> getAllPerformances() {
        return performanceRepositoryDW.findAll();
    }

    public Performance_DW getPerformanceById(Long id) {
        return performanceRepositoryDW.findById(id).orElse(null);
    }

    public List<Performance_DW> getPerformancesByStudent(Long studentId) {
        return performanceRepositoryDW.findByStudentId(studentId);
    }

    public List<Performance_DW> getPerformancesByModule(Long moduleId) {
        return performanceRepositoryDW.findByModuleId(moduleId);
    }

    /**
     * Get pass/fail rate for a specific module
     * @param moduleId Module ID
     * @param year Academic year
     * @return Map containing pass rate, fail rate, and total count
     */
    public Map<String, Object> getModulePassFailRate(Long moduleId, Integer year) {
        Map<String, Object> result = new HashMap<>();

        Integer totalCount = performanceRepositoryDW.countByModuleAndYear(moduleId, year);
        Integer failCount = performanceRepositoryDW.countFailuresByModuleAndYear(moduleId, year);
        Integer passCount = totalCount - failCount;

        double passRate = (totalCount > 0) ? (double) passCount / totalCount * 100 : 0;
        double failRate = (totalCount > 0) ? (double) failCount / totalCount * 100 : 0;

        result.put("moduleId", moduleId);
        result.put("year", year);
        result.put("totalStudents", totalCount);
        result.put("passCount", passCount);
        result.put("failCount", failCount);
        result.put("passRate", passRate);
        result.put("failRate", failRate);

        return result;
    }

    /**
     * Get the grade distribution for a module
     * @param moduleId Module ID
     * @param year Academic year
     * @return Map containing counts for each grade
     */
    public Map<String, Object> getModuleGradeDistribution(Long moduleId, Integer year) {
        String sql = "SELECT grade, COUNT(*) as count FROM PERFORMANCE_FACT_DW " +
                "WHERE module_id = ? AND year = ? GROUP BY grade ORDER BY grade";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, moduleId, year);

        Map<String, Object> distribution = new HashMap<>();
        distribution.put("moduleId", moduleId);
        distribution.put("year", year);
        distribution.put("gradeDistribution", results);

        return distribution;
    }

    /**
     * Get modules with the highest failure rates
     * @param year Academic year
     * @param limit Number of modules to return
     * @return List of modules sorted by failure rate (highest first)
     */
    public List<Map<String, Object>> getModulesWithHighestFailureRates(Integer year, Integer limit) {
        String sql = "SELECT m.module_id, m.module_name, " +
                "COUNT(p.performance_id) as total_students, " +
                "SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as fail_count, " +
                "(SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id)) as fail_rate " +
                "FROM MODULE_DIM_DW m " +
                "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                "WHERE p.year = ? " +
                "GROUP BY m.module_id, m.module_name " +
                "HAVING COUNT(p.performance_id) > 5 " +  // Only include modules with enough students
                "ORDER BY fail_rate DESC";

        if (limit != null && limit > 0) {
            sql += " FETCH FIRST " + limit + " ROWS ONLY";
        }

        return jdbcTemplate.queryForList(sql, year);
    }

    /**
     * Get department performance statistics
     * @param departmentId Department ID
     * @param year Academic year
     * @return Map containing performance statistics for the department
     */
    public Map<String, Object> getDepartmentPerformanceStats(Long departmentId, Integer year) {
        String sql = "SELECT d.department_id, d.department_name, " +
                "COUNT(p.performance_id) as total_grades, " +
                "SUM(CASE WHEN p.grade = 'A' THEN 1 ELSE 0 END) as a_count, " +
                "SUM(CASE WHEN p.grade = 'B' THEN 1 ELSE 0 END) as b_count, " +
                "SUM(CASE WHEN p.grade = 'C' THEN 1 ELSE 0 END) as c_count, " +
                "SUM(CASE WHEN p.grade = 'D' THEN 1 ELSE 0 END) as d_count, " +
                "SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as f_count, " +
                "(SUM(CASE WHEN p.grade IN ('A', 'B', 'C', 'D') THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id)) as pass_rate " +
                "FROM DEPARTMENT_DIM_DW d " +
                "JOIN COURSE_DIM_DW c ON d.department_id = ? " +
                "JOIN MODULE_DIM_DW m ON c.course_id = m.course_id " +
                "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                "WHERE p.year = ? " +
                "GROUP BY d.department_id, d.department_name";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, departmentId, year);

        if (results.isEmpty()) {
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("departmentId", departmentId);
            emptyResult.put("year", year);
            emptyResult.put("message", "No data found");
            return emptyResult;
        }

        return results.get(0);
    }
}
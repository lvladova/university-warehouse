package com.warehouse.universitywarehouse.service.DW;

import com.warehouse.universitywarehouse.model.DW.Admissions_DW;
import com.warehouse.universitywarehouse.repository.DW.AdmissionsRepository_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdmissionsService_DW {

    @Autowired
    private AdmissionsRepository_DW admissionsRepositoryDW;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Admissions_DW> getAllAdmissions() {
        return admissionsRepositoryDW.findAll();
    }

    public Admissions_DW getAdmissionById(Long id) {
        return admissionsRepositoryDW.findById(id).orElse(null);
    }

    public List<Admissions_DW> getAdmissionsByProgram(Long programId) {
        return admissionsRepositoryDW.findByProgramId(programId);
    }

    /**
     * Get applicant vs admitted statistics by program
     * @param year Academic year
     * @return Map of applicant vs admitted statistics by program
     */
    public List<Map<String, Object>> getApplicantsVsAdmittedByProgram(int year) {
        String sql = "SELECT p.program_id, p.program_name, d.department_name, " +
                "SUM(CASE WHEN a.status = 'APPLIED' THEN 1 ELSE 0 END) as applicant_count, " +
                "SUM(CASE WHEN a.status = 'ADMITTED' THEN 1 ELSE 0 END) as admitted_count, " +
                "(SUM(CASE WHEN a.status = 'ADMITTED' THEN 1 ELSE 0 END) * 100.0 / NULLIF(SUM(CASE WHEN a.status = 'APPLIED' THEN 1 ELSE 0 END), 0)) as admission_rate " +
                "FROM ADMISSIONS_FACT_DW a " +
                "JOIN PROGRAM_DIM_DW p ON a.program_id = p.program_id " +
                "JOIN DEPARTMENT_DIM_DW d ON p.department_id = d.department_id " +
                "WHERE EXTRACT(YEAR FROM a.admission_date) = ? " +
                "GROUP BY p.program_id, p.program_name, d.department_name " +
                "ORDER BY applicant_count DESC";

        return jdbcTemplate.queryForList(sql, year);
    }

    /**
     * Get admission trends by year
     * @param startYear Start year
     * @param endYear End year
     * @return List of admission statistics by year
     */
    public List<Map<String, Object>> getAdmissionTrendsByYear(int startYear, int endYear) {
        String sql = "SELECT EXTRACT(YEAR FROM a.admission_date) as year, " +
                "SUM(CASE WHEN a.status = 'APPLIED' THEN 1 ELSE 0 END) as applicant_count, " +
                "SUM(CASE WHEN a.status = 'ADMITTED' THEN 1 ELSE 0 END) as admitted_count, " +
                "(SUM(CASE WHEN a.status = 'ADMITTED' THEN 1 ELSE 0 END) * 100.0 / NULLIF(SUM(CASE WHEN a.status = 'APPLIED' THEN 1 ELSE 0 END), 0)) as admission_rate " +
                "FROM ADMISSIONS_FACT_DW a " +
                "WHERE EXTRACT(YEAR FROM a.admission_date) BETWEEN ? AND ? " +
                "GROUP BY EXTRACT(YEAR FROM a.admission_date) " +
                "ORDER BY year";

        return jdbcTemplate.queryForList(sql, startYear, endYear);
    }

    /**
     * Get demographic breakdown of admitted students
     * @param year Academic year
     * @return Map of admitted student demographics
     */
    public Map<String, Object> getAdmittedStudentDemographics(int year) {
        Map<String, Object> demographics = new HashMap<>();

        // Gender distribution
        String genderSql = "SELECT s.gender, COUNT(a.admission_id) as count " +
                "FROM ADMISSIONS_FACT_DW a " +
                "JOIN STUDENT_DIM_DW s ON a.student_id = s.student_id " +
                "WHERE EXTRACT(YEAR FROM a.admission_date) = ? " +
                "AND a.status = 'ADMITTED' " +
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
                "COUNT(a.admission_id) as count " +
                "FROM ADMISSIONS_FACT_DW a " +
                "JOIN STUDENT_DIM_DW s ON a.student_id = s.student_id " +
                "WHERE EXTRACT(YEAR FROM a.admission_date) = ? " +
                "AND a.status = 'ADMITTED' " +
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
        String nationalitySql = "SELECT s.nationality, COUNT(a.admission_id) as count " +
                "FROM ADMISSIONS_FACT_DW a " +
                "JOIN STUDENT_DIM_DW s ON a.student_id = s.student_id " +
                "WHERE EXTRACT(YEAR FROM a.admission_date) = ? " +
                "AND a.status = 'ADMITTED' " +
                "GROUP BY s.nationality " +
                "ORDER BY count DESC";

        List<Map<String, Object>> nationalityDistribution = jdbcTemplate.queryForList(nationalitySql, year);

        // Faculty distribution
        String facultySql = "SELECT c.faculty_id, f.faculty_name, COUNT(a.admission_id) as count " +
                "FROM ADMISSIONS_FACT_DW a " +
                "JOIN PROGRAM_DIM_DW p ON a.program_id = p.program_id " +
                "JOIN DEPARTMENT_DIM_DW d ON p.department_id = d.department_id " +
                "JOIN COURSE_DIM_DW c ON d.department_id = c.department_id " +
                "JOIN FACULTY_DIM_DW f ON c.faculty_id = f.faculty_id " +
                "WHERE EXTRACT(YEAR FROM a.admission_date) = ? " +
                "AND a.status = 'ADMITTED' " +
                "GROUP BY c.faculty_id, f.faculty_name " +
                "ORDER BY count DESC";

        List<Map<String, Object>> facultyDistribution = jdbcTemplate.queryForList(facultySql, year);

        demographics.put("year", year);
        demographics.put("genderDistribution", genderDistribution);
        demographics.put("ageDistribution", ageDistribution);
        demographics.put("nationalityDistribution", nationalityDistribution);
        demographics.put("facultyDistribution", facultyDistribution);

        return demographics;
    }
}
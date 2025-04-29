package com.warehouse.universitywarehouse.service.DW;

import com.warehouse.universitywarehouse.model.DW.Finance_DW;
import com.warehouse.universitywarehouse.repository.DW.FinanceRepository_DW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceService_DW {

    @Autowired
    private FinanceRepository_DW financeRepositoryDW;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Finance_DW> getAllFinances() {
        return financeRepositoryDW.findAll();
    }

    public Finance_DW getFinanceById(Long id) {
        return financeRepositoryDW.findById(id).orElse(null);
    }

    /**
     * Get tuition fee collection trends by year and faculty
     * @param startYear Start year for the trend analysis
     * @param endYear End year for the trend analysis
     * @return Map of tuition fee collections by faculty and year
     */
    public Map<String, Object> getTuitionFeeTrendsByFaculty(int startYear, int endYear) {
        String sql = "SELECT EXTRACT(YEAR FROM f.transaction_date) as year, " +
                "c.faculty_id, fd.faculty_name, SUM(f.amount) as total_fees " +
                "FROM FINANCE_FACT_DW f " +
                "JOIN STUDENT_DIM_DW s ON f.student_id = s.student_id " +
                "JOIN ENROLLMENT_FACT_DW e ON s.student_id = e.student_id " +
                "JOIN MODULE_DIM_DW m ON e.module_id = m.module_id " +
                "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                "JOIN FACULTY_DIM_DW fd ON c.faculty_id = fd.faculty_id " +
                "WHERE EXTRACT(YEAR FROM f.transaction_date) BETWEEN ? AND ? " +
                "GROUP BY EXTRACT(YEAR FROM f.transaction_date), c.faculty_id, fd.faculty_name " +
                "ORDER BY year, faculty_id";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, startYear, endYear);

        Map<String, Object> trendData = new HashMap<>();
        trendData.put("startYear", startYear);
        trendData.put("endYear", endYear);
        trendData.put("feeTrends", results);

        return trendData;
    }

    /**
     * Get scholarship statistics
     * @param year Academic year
     * @return Map of scholarship statistics
     */
    public Map<String, Object> getScholarshipStatistics(int year) {
        String sql = "SELECT EXTRACT(YEAR FROM f.transaction_date) as year, " +
                "COUNT(DISTINCT f.student_id) as total_students, " +
                "SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN 1 ELSE 0 END) as scholarship_count, " +
                "(SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN 1 ELSE 0 END) * 100.0 / COUNT(DISTINCT f.student_id)) as scholarship_percentage, " +
                "SUM(f.amount) as total_fees, " +
                "SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN f.amount ELSE 0 END) as scholarship_amount, " +
                "(SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN f.amount ELSE 0 END) * 100.0 / SUM(f.amount)) as scholarship_amount_percentage " +
                "FROM FINANCE_FACT_DW f " +
                "WHERE EXTRACT(YEAR FROM f.transaction_date) = ? " +
                "GROUP BY EXTRACT(YEAR FROM f.transaction_date)";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, year);

        if (results.isEmpty()) {
            Map<String, Object> emptyResult = new HashMap<>();
            emptyResult.put("year", year);
            emptyResult.put("message", "No data found");
            return emptyResult;
        }

        return results.get(0);
    }

    /**
     * Get scholarship statistics by faculty
     * @param year Academic year
     * @return List of scholarship statistics by faculty
     */
    public List<Map<String, Object>> getScholarshipStatisticsByFaculty(int year) {
        String sql = "SELECT c.faculty_id, fd.faculty_name, " +
                "COUNT(DISTINCT f.student_id) as total_students, " +
                "SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN 1 ELSE 0 END) as scholarship_count, " +
                "(SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN 1 ELSE 0 END) * 100.0 / COUNT(DISTINCT f.student_id)) as scholarship_percentage, " +
                "SUM(f.amount) as total_fees, " +
                "SUM(CASE WHEN f.payment_method = 'SCHOLARSHIP' THEN f.amount ELSE 0 END) as scholarship_amount " +
                "FROM FINANCE_FACT_DW f " +
                "JOIN STUDENT_DIM_DW s ON f.student_id = s.student_id " +
                "JOIN ENROLLMENT_FACT_DW e ON s.student_id = e.student_id " +
                "JOIN MODULE_DIM_DW m ON e.module_id = m.module_id " +
                "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                "JOIN FACULTY_DIM_DW fd ON c.faculty_id = fd.faculty_id " +
                "WHERE EXTRACT(YEAR FROM f.transaction_date) = ? " +
                "GROUP BY c.faculty_id, fd.faculty_name " +
                "ORDER BY scholarship_percentage DESC";

        return jdbcTemplate.queryForList(sql, year);
    }

    /**
     * Get payment trends by method
     * @param startYear Start year
     * @param endYear End year
     * @return List of payment amounts by method and year
     */
    public List<Map<String, Object>> getPaymentTrendsByMethod(int startYear, int endYear) {
        String sql = "SELECT EXTRACT(YEAR FROM f.transaction_date) as year, " +
                "f.payment_method, " +
                "COUNT(f.finance_id) as transaction_count, " +
                "SUM(f.amount) as total_amount " +
                "FROM FINANCE_FACT_DW f " +
                "WHERE EXTRACT(YEAR FROM f.transaction_date) BETWEEN ? AND ? " +
                "GROUP BY EXTRACT(YEAR FROM f.transaction_date), f.payment_method " +
                "ORDER BY year, payment_method";

        return jdbcTemplate.queryForList(sql, startYear, endYear);
    }
}

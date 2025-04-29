package com.warehouse.universitywarehouse.controller;

import com.warehouse.universitywarehouse.util.LoadTestingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/loadtest")
public class LoadTestController {

    private static final Logger logger = Logger.getLogger(LoadTestController.class.getName());

    @Autowired
    private LoadTestingUtil loadTestingUtil;

    /**
     * Run a query load test
     * @param numThreads Number of concurrent threads
     * @param numQueries Number of queries per thread
     * @param queryType Type of query to run (simple, complex, bind-variable, no-bind-variable)
     * @return Load test results
     */
    @PostMapping("/query")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runQueryLoadTest(
            @RequestParam(defaultValue = "5") int numThreads,
            @RequestParam(defaultValue = "100") int numQueries,
            @RequestParam(defaultValue = "simple") String queryType) {

        logger.info("Starting query load test: threads=" + numThreads + ", queries=" + numQueries + ", type=" + queryType);

        Map<String, Object> testResults = loadTestingUtil.runQueryLoadTest(numThreads, numQueries, queryType);

        // Generate a report file
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportFile = "loadtest_query_" + timestamp + ".md";
        loadTestingUtil.generateLoadTestReport(testResults, reportFile);

        testResults.put("reportFile", reportFile);

        return ResponseEntity.ok(testResults);
    }

    /**
     * Run an index performance test
     * @return Test results
     */
    @GetMapping("/index-performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runIndexPerformanceTest() {
        logger.info("Starting index performance test");

        Map<String, Object> results = loadTestingUtil.runIndexPerformanceTest();

        logger.info("Index performance test completed: " + results);

        return ResponseEntity.ok(results);
    }

    /**
     * Run a partition performance test
     * @return Test results
     */
    @GetMapping("/partition-performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runPartitionPerformanceTest() {
        logger.info("Starting partition performance test");

        Map<String, Object> results = loadTestingUtil.runPartitionPerformanceTest();

        logger.info("Partition performance test completed: " + results);

        return ResponseEntity.ok(results);
    }

    /**
     * Run a comprehensive performance test suite
     * @return Aggregated test results
     */
    @PostMapping("/comprehensive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runComprehensiveTest() {
        logger.info("Starting comprehensive performance test suite");

        Map<String, Object> aggregatedResults = new HashMap<>();

        // Run simple query test
        Map<String, Object> simpleQueryResults = loadTestingUtil.runQueryLoadTest(5, 100, "simple");
        aggregatedResults.put("simpleQueryTest", simpleQueryResults);

        // Run complex query test
        Map<String, Object> complexQueryResults = loadTestingUtil.runQueryLoadTest(5, 50, "complex");
        aggregatedResults.put("complexQueryTest", complexQueryResults);

        // Run bind variable performance comparison
        Map<String, Object> bindVarResults = loadTestingUtil.runQueryLoadTest(5, 100, "bind-variable");
        Map<String, Object> noBindVarResults = loadTestingUtil.runQueryLoadTest(5, 100, "no-bind-variable");
        aggregatedResults.put("bindVariableTest", bindVarResults);
        aggregatedResults.put("noBindVariableTest", noBindVarResults);

        // Calculate bind variable performance improvement
        double bindVarQPS = (double) bindVarResults.get("queriesPerSecond");
        double noBindVarQPS = (double) noBindVarResults.get("queriesPerSecond");
        aggregatedResults.put("bindVariableImprovement", bindVarQPS / noBindVarQPS);

        // Run index performance test
        Map<String, Object> indexResults = loadTestingUtil.runIndexPerformanceTest();
        aggregatedResults.put("indexPerformanceTest", indexResults);

        // Run partition performance test
        Map<String, Object> partitionResults = loadTestingUtil.runPartitionPerformanceTest();
        aggregatedResults.put("partitionPerformanceTest", partitionResults);

        // Generate a comprehensive report
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportFile = "comprehensive_test_" + timestamp + ".md";
        // In a real implementation, you would create a more detailed report here

        aggregatedResults.put("reportFile", reportFile);

        logger.info("Comprehensive performance test suite completed");

        return ResponseEntity.ok(aggregatedResults);
    }

    /**
     * Run a comparison test between standard and optimized queries
     * @return Comparison results
     */
    @GetMapping("/optimization-comparison")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> runOptimizationComparison() {
        logger.info("Starting optimization comparison test");

        Map<String, Object> results = new HashMap<>();

        // Test standard query (no optimizations)
        long startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(
                "SELECT s.student_id, s.name, d.department_name, c.course_name, m.module_name, p.grade " +
                        "FROM STUDENT_DIM_DW s " +
                        "JOIN PERFORMANCE_FACT_DW p ON s.student_id = p.student_id " +
                        "JOIN MODULE_DIM_DW m ON p.module_id = m.module_id " +
                        "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                        "JOIN DEPARTMENT_DIM_DW d ON c.department_id = d.department_id " +
                        "WHERE p.year = 2023"
        );
        long standardTime = System.currentTimeMillis() - startTime;

        // Test optimized query (with index hints and bind variables)
        startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(
                "SELECT /*+ INDEX(p idx_performance_year) */ " +
                        "s.student_id, s.name, d.department_name, c.course_name, m.module_name, p.grade " +
                        "FROM PERFORMANCE_FACT_DW p " +
                        "JOIN STUDENT_DIM_DW s ON p.student_id = s.student_id " +
                        "JOIN MODULE_DIM_DW m ON p.module_id = m.module_id " +
                        "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                        "JOIN DEPARTMENT_DIM_DW d ON c.department_id = d.department_id " +
                        "WHERE p.year = ?",
                2023
        );
        long optimizedTime = System.currentTimeMillis() - startTime;

        results.put("standardQueryTime", standardTime);
        results.put("optimizedQueryTime", optimizedTime);
        results.put("improvement", ((double) standardTime / optimizedTime));

        logger.info("Optimization comparison completed: " + results);

        return ResponseEntity.ok(results);
    }
}
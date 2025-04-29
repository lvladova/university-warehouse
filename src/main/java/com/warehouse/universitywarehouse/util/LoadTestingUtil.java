package com.warehouse.universitywarehouse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Utility class for load testing the data warehouse
 */
@Component
public class LoadTestingUtil {

    private static final Logger logger = Logger.getLogger(LoadTestingUtil.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * Run a simple query load test
     * @param numThreads Number of concurrent threads
     * @param numQueries Number of queries per thread
     * @param queryType Type of query to run (simple, complex)
     * @return Load test results
     */
    public Map<String, Object> runQueryLoadTest(int numThreads, int numQueries, String queryType) {
        logger.info("Starting query load test with " + numThreads + " threads, " + numQueries + " queries per thread, type: " + queryType);

        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<QueryThreadResult>> futures = new ArrayList<>();

        // Submit tasks
        for (int i = 0; i < numThreads; i++) {
            int threadId = i;
            futures.add(executor.submit(() -> {
                return runQueriesInThread(threadId, numQueries, queryType);
            }));
        }

        // Collect results
        List<QueryThreadResult> results = new ArrayList<>();
        for (Future<QueryThreadResult> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                logger.severe("Error executing query thread: " + e.getMessage());
            }
        }

        // Shutdown executor
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.severe("Error waiting for threads to complete: " + e.getMessage());
        }

        // Calculate metrics
        long totalDuration = System.currentTimeMillis() - startTime;
        long totalQueries = numThreads * numQueries;
        double queriesPerSecond = (double) totalQueries / (totalDuration / 1000.0);

        long successfulQueries = results.stream().mapToLong(r -> r.getSuccessfulQueries()).sum();
        long totalQueryTime = results.stream().mapToLong(r -> r.getTotalQueryTime()).sum();
        double avgQueryTime = (double) totalQueryTime / successfulQueries;

        // Create result object
        Map<String, Object> testResult = new HashMap<>();
        testResult.put("numThreads", numThreads);
        testResult.put("numQueriesPerThread", numQueries);
        testResult.put("totalQueries", totalQueries);
        testResult.put("successfulQueries", successfulQueries);
        testResult.put("failedQueries", totalQueries - successfulQueries);
        testResult.put("totalDuration", totalDuration);
        testResult.put("queriesPerSecond", queriesPerSecond);
        testResult.put("avgQueryTime", avgQueryTime);
        testResult.put("threadResults", results);

        logger.info("Load test completed: " + queriesPerSecond + " queries/second, avg query time: " + avgQueryTime + "ms");

        return testResult;
    }

    /**
     * Run queries in a thread
     * @param threadId ID of the thread
     * @param numQueries Number of queries to run
     * @param queryType Type of query to run
     * @return Thread result
     */
    private QueryThreadResult runQueriesInThread(int threadId, int numQueries, String queryType) {
        QueryThreadResult result = new QueryThreadResult(threadId);

        for (int i = 0; i < numQueries; i++) {
            try {
                long queryStartTime = System.currentTimeMillis();

                // Execute query based on type
                if ("simple".equals(queryType)) {
                    executeSimpleQuery(threadId, i);
                } else if ("complex".equals(queryType)) {
                    executeComplexQuery(threadId, i);
                } else if ("bind-variable".equals(queryType)) {
                    executeBindVariableQuery(threadId, i);
                } else if ("no-bind-variable".equals(queryType)) {
                    executeNoBindVariableQuery(threadId, i);
                } else {
                    throw new IllegalArgumentException("Unknown query type: " + queryType);
                }

                long queryTime = System.currentTimeMillis() - queryStartTime;
                result.addSuccessfulQuery(queryTime);

                // Log periodic progress
                if (i > 0 && i % 100 == 0) {
                    logger.info("Thread " + threadId + " completed " + i + " queries");
                }

            } catch (Exception e) {
                result.addFailedQuery();
                logger.warning("Thread " + threadId + " failed query " + i + ": " + e.getMessage());
            }
        }

        logger.info("Thread " + threadId + " completed " + numQueries + " queries: " +
                result.getSuccessfulQueries() + " successful, " +
                result.getFailedQueries() + " failed, avg time: " +
                result.getAverageQueryTime() + "ms");

        return result;
    }

    /**
     * Execute a simple query
     */
    private void executeSimpleQuery(int threadId, int queryId) {
        jdbcTemplate.queryForList(
                "SELECT s.student_id, s.name " +
                        "FROM STUDENT_DIM_DW s " +
                        "ORDER BY s.student_id " +
                        "OFFSET ? ROWS FETCH NEXT 10 ROWS ONLY",
                queryId % 100
        );
    }

    /**
     * Execute a complex query
     */
    private void executeComplexQuery(int threadId, int queryId) {
        int year = 2019 + (queryId % 5);  // Years 2019-2023

        jdbcTemplate.queryForList(
                "SELECT m.module_id, m.module_name, c.course_name, " +
                        "COUNT(p.performance_id) as total_students, " +
                        "SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) as fail_count, " +
                        "ROUND(SUM(CASE WHEN p.grade = 'F' THEN 1 ELSE 0 END) * 100.0 / COUNT(p.performance_id), 2) as fail_rate " +
                        "FROM MODULE_DIM_DW m " +
                        "JOIN COURSE_DIM_DW c ON m.course_id = c.course_id " +
                        "JOIN PERFORMANCE_FACT_DW p ON m.module_id = p.module_id " +
                        "WHERE p.year = ? " +
                        "GROUP BY m.module_id, m.module_name, c.course_name " +
                        "HAVING COUNT(p.performance_id) > 5 " +
                        "ORDER BY fail_rate DESC",
                year
        );
    }

    /**
     * Execute a query with bind variables
     */
    private void executeBindVariableQuery(int threadId, int queryId) {
        int year = 2019 + (queryId % 5);  // Years 2019-2023

        jdbcTemplate.queryForList(
                "SELECT s.student_id, s.name, m.module_name, p.grade " +
                        "FROM PERFORMANCE_FACT_DW p " +
                        "JOIN STUDENT_DIM_DW s ON p.student_id = s.student_id " +
                        "JOIN MODULE_DIM_DW m ON p.module_id = m.module_id " +
                        "WHERE p.year = ? AND s.student_id > ?",
                year, threadId * 100
        );
    }

    /**
     * Execute a query without bind variables (BAD PRACTICE - for comparison only)
     */
    private void executeNoBindVariableQuery(int threadId, int queryId) {
        int year = 2019 + (queryId % 5);  // Years 2019-2023

        // NOTE: This is BAD PRACTICE - shown for comparison only
        jdbcTemplate.queryForList(
                "SELECT s.student_id, s.name, m.module_name, p.grade " +
                        "FROM PERFORMANCE_FACT_DW p " +
                        "JOIN STUDENT_DIM_DW s ON p.student_id = s.student_id " +
                        "JOIN MODULE_DIM_DW m ON p.module_id = m.module_id " +
                        "WHERE p.year = " + year + " AND s.student_id > " + (threadId * 100)
        );
    }

    /**
     * Run an index performance test comparing queries with and without indexes
     * @return Test results
     */
    public Map<String, Object> runIndexPerformanceTest() {
        Map<String, Object> results = new HashMap<>();

        // Test query with index
        long startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(
                "SELECT /*+ INDEX(p idx_performance_student) */ " +
                        "p.performance_id, p.module_id, p.grade " +
                        "FROM PERFORMANCE_FACT_DW p " +
                        "WHERE p.student_id = ? AND p.year = ?",
                1, 2023
        );
        long withIndexTime = System.currentTimeMillis() - startTime;

        // Test query without index hint
        startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(
                "SELECT p.performance_id, p.module_id, p.grade " +
                        "FROM PERFORMANCE_FACT_DW p " +
                        "WHERE p.student_id = ? AND p.year = ?",
                1, 2023
        );
        long withoutIndexHintTime = System.currentTimeMillis() - startTime;

        results.put("withIndexTime", withIndexTime);
        results.put("withoutIndexHintTime", withoutIndexHintTime);
        results.put("improvement", ((double) withoutIndexHintTime / withIndexTime));

        return results;
    }

    /**
     * Run a partition performance test
     * @return Test results
     */
    public Map<String, Object> runPartitionPerformanceTest() {
        Map<String, Object> results = new HashMap<>();

        // Test query with partition (partition pruning)
        long startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(
                "SELECT p.student_id, p.module_id, p.grade " +
                        "FROM PERFORMANCE_FACT_DW_PART p " +
                        "WHERE p.year = ?",
                2023
        );
        long withPartitionTime = System.currentTimeMillis() - startTime;

        // Test query without partition
        startTime = System.currentTimeMillis();
        jdbcTemplate.queryForList(
                "SELECT p.student_id, p.module_id, p.grade " +
                        "FROM PERFORMANCE_FACT_DW p " +
                        "WHERE p.year = ?",
                2023
        );
        long withoutPartitionTime = System.currentTimeMillis() - startTime;

        results.put("withPartitionTime", withPartitionTime);
        results.put("withoutPartitionTime", withoutPartitionTime);
        results.put("improvement", ((double) withoutPartitionTime / withPartitionTime));

        return results;
    }

    /**
     * Generate a load test report
     * @param result Load test result
     * @param outputFile Output file for the report
     */
    public void generateLoadTestReport(Map<String, Object> result, String outputFile) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            writer.write("# Data Warehouse Load Test Report\n\n");
            writer.write("## Test Configuration\n");
            writer.write("- Number of Threads: " + result.get("numThreads") + "\n");
            writer.write("- Queries per Thread: " + result.get("numQueriesPerThread") + "\n");
            writer.write("- Total Queries: " + result.get("totalQueries") + "\n\n");

            writer.write("## Test Results\n");
            writer.write("- Successful Queries: " + result.get("successfulQueries") + "\n");
            writer.write("- Failed Queries: " + result.get("failedQueries") + "\n");
            writer.write("- Total Duration: " + result.get("totalDuration") + "ms\n");
            writer.write("- Queries Per Second: " + result.get("queriesPerSecond") + "\n");
            writer.write("- Average Query Time: " + result.get("avgQueryTime") + "ms\n\n");

            writer.write("## Thread Results\n");
            @SuppressWarnings("unchecked")
            List<QueryThreadResult> threadResults = (List<QueryThreadResult>) result.get("threadResults");
            for (QueryThreadResult threadResult : threadResults) {
                writer.write("### Thread " + threadResult.getThreadId() + "\n");
                writer.write("- Successful Queries: " + threadResult.getSuccessfulQueries() + "\n");
                writer.write("- Failed Queries: " + threadResult.getFailedQueries() + "\n");
                writer.write("- Total Query Time: " + threadResult.getTotalQueryTime() + "ms\n");
                writer.write("- Average Query Time: " + threadResult.getAverageQueryTime() + "ms\n\n");
            }

            writer.close();
            logger.info("Load test report generated: " + outputFile);

        } catch (Exception e) {
            logger.severe("Error generating load test report: " + e.getMessage());
        }
    }

    /**
     * Inner class for query thread results
     */
    public static class QueryThreadResult {
        private int threadId;
        private long successfulQueries;
        private long failedQueries;
        private long totalQueryTime;

        public QueryThreadResult(int threadId) {
            this.threadId = threadId;
            this.successfulQueries = 0;
            this.failedQueries = 0;
            this.totalQueryTime = 0;
        }

        public void addSuccessfulQuery(long queryTime) {
            successfulQueries++;
            totalQueryTime += queryTime;
        }

        public void addFailedQuery() {
            failedQueries++;
        }

        public int getThreadId() {
            return threadId;
        }

        public long getSuccessfulQueries() {
            return successfulQueries;
        }

        public long getFailedQueries() {
            return failedQueries;
        }

        public long getTotalQueryTime() {
            return totalQueryTime;
        }

        public double getAverageQueryTime() {
            return successfulQueries > 0 ? (double) totalQueryTime / successfulQueries : 0;
        }
    }
}